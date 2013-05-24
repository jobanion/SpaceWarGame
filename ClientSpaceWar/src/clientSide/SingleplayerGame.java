
package clientSide;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class SingleplayerGame extends BasicGameState {

	String backgroundImage = "SpaceWormHole.jpg";
	String explosionImage = "explosion.gif";
	public static Image background = null, explosion = null;
	public static String shipImage = "spaceship1.gif";

	public static List<Ship> ships = new CopyOnWriteArrayList<Ship>(); 	// List of all ships
	public static List<AISP> aiShips = new CopyOnWriteArrayList<AISP>(); 	// list of all AI ships
	
	// The ships (and post-initialization stuff)
	Ship playerShip;
	private boolean initial = true;

	public static ShotSP aiShot = null;
	public static boolean menu = false, exploded = false;
	public static long respawnTimer = 0;
	public static int width, height, killCount = 0, deathCount = 0;	
	public static float kdr;
	public static int count = 0, numOfAI = 1;
	public static boolean init = false, playerDead = false;
	public String respawn = "Respawn (R)", resume = "Resume (R)", mainMenu = "Main Menu (M)", exit = "Quit Game (Q)";
	
	public SingleplayerGame(int state) {
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		if(!init){
			if (!initial) {
				initial();
			}

			background = new Image("res/" + backgroundImage);
			explosion = new Image("res/" + explosionImage);
			width = Main.width;
			height = Main.height;
			init = true;

		}
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		background.draw(0, 0);
		
		for(Ship current : ships){
			current.render(gc, sbg, g);
		}

		if(playerDead == true) {
			g.drawString(respawn,  	(Main.width/2) - respawn.length(), 200);
			g.drawString(mainMenu, 	(Main.width/2) - mainMenu.length(), 250);
			g.drawString(exit, 		(Main.width/2) - exit.length(), 300);
			if(playerDead == false){
				g.clear();
			}
		}

		if(menu == true && playerDead != true){
			g.drawString(resume,    (Main.width/2) - resume.length(), 	200);
			g.drawString(mainMenu, 	(Main.width/2) - mainMenu.length(), 250);
			g.drawString(exit, 		(Main.width/2) - exit.length(), 	300);
			if(menu == false){
				g.clear();
			}
		}	

		g.drawString("Enemies Left: " + count, Main.width - 200, 10);
		g.drawString("Kills: " + killCount, Main.width - 200, 30);
		g.drawString("Deaths: " + deathCount, Main.width - 200, 50);
		if(deathCount > 0) {
			kdr = (float)killCount/deathCount;
		} else {
			kdr = killCount;
		}
		g.drawString("SP K/D Ratio: " + kdr, Main.width - 200, 70);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (initial) {
			initial();
		}
		if(!menu && !initial) {
			aiDoStuff(delta);
		}
		Input input = gc.getInput();

		if(input.isKeyPressed(Input.KEY_P)) {
			if(gc.isPaused()){
				gc.setPaused(false);
			} else {
				gc.setPaused(true);
			}
		}

		// update player ship
		for(Ship current : ships){
			if(current.getClass() != AISP.class) {
				current.update(gc, sbg, delta, input);
			}
		}		

		// Escape key to open the menus in game
		if(input.isKeyDown(Input.KEY_ESCAPE)){
			menu = true;
		}      

		if(playerDead==true){
			if(gc.isPaused() == false){ // Tells the game to pause when the menu is brought up
				gc.setPaused(true);
			}
			if(input.isKeyDown(Input.KEY_R)){ // Respawns the player
				playerDead = false;
				respawnPlayer();
				gc.setPaused(false);
			}
			if(input.isKeyDown(Input.KEY_M)){ // Brings the user back to the menu
				playerDead = false;
				gc.setPaused(false);
				reset();
				initial = true; 				// so it will repeat when you get back on (re-initialize with new ship)
				sbg.enterState(Main.gameState.MENU.value);
			}

			if(input.isKeyDown(Input.KEY_Q)){  // Closes the program
				Menu.exit();
			}
		}

		// When escape is hit, this tells the menu that it can open
		if(menu == true){
			if(gc.isPaused() == false){ // Tells the game to pause when the menu is brought up
				gc.setPaused(true);
			}
			if(input.isKeyDown(Input.KEY_R)){ // Resumes the game
				menu = false;
				gc.setPaused(false);
			}
			if(input.isKeyDown(Input.KEY_M)){ // Brings the user back to the menu
				menu = false;
				gc.setPaused(false);
				reset();
				initial = true; 				// so it will repeat when you get back on (re-initialize with new ship)
				sbg.enterState(Main.gameState.MENU.value);
			}
			if(input.isKeyDown(Input.KEY_Q)){  // Closes the program
				Menu.exit();
			}
		}
	}

	private static void respawnPlayer() {
		Random gen1 = new Random(), gen2 = new Random();
		try {
			ships.add(new Ship(Main.user.ups.getUsername(), gen1.nextInt(Main.width), gen2.nextInt(Main.height), shipImage));
		} catch (SlickException e) {
		}
	}

	private void reset() {
		ships.clear();
		aiShips.clear();
		numOfAI = 1;
		initial = true;
	}

	public static void aiDoStuff(int delta) {
		if(aiShips.isEmpty() && System.currentTimeMillis() >= respawnTimer + 2000){
			respawnAI();
		}
		aiRotate();
		aiMove(delta);
		aiShot();
	}

	static void respawnAI() {
		for(int i = 0; i < numOfAI; i++){
			Random gen1 = new Random(), gen2 = new Random();
			AISP ai;
			try {
				ai = new AISP(gen1.nextInt(Main.width), gen2.nextInt(Main.height));
				ships.add(ai);
				aiShips.add(ai);
				count++;
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		numOfAI += 2;
	}
	
	private static void aiShot() {
		for(AISP current : aiShips ){
			if(!current.isHit && current.target != null){
				if(!current.shotFired) {
					current.currentShotSP = new ShotSP(1, current.x+10, current.y+5, current.ship.getRotation(), 3);
					current.shotFired = true;
				} else if(!current.currentShotSP.shotVisible) {
					current.currentShotSP = new ShotSP(1, current.x+10, current.y+5, current.ship.getRotation(), 3);
				}
			}
		}
	}

	private static void aiMove(int delta) {
		for(AISP current : aiShips ){
			current.move(delta * 0.3f, current.ship.getRotation());
		}
	}

	private static void aiRotate() {
		for(AISP current : aiShips ){
			current.rotate(current.leftOrRight() * 2f, current.ship);
		}
	}

	public void initial() throws SlickException{
		initial = false; 	// set boolean variable = false, so it does not repeat initialization, and reset to true when go to main
		shipImage = "spaceship" + Main.user.ups.shipNum + ".gif";
		playerShip = new Ship(Main.user.ups.getUsername(), 600, 300, shipImage);
		ships.add(playerShip);
		numOfAI = 1;
		respawnAI();
	}

	// State based stuff thingie
	public int getID(){
		return Main.gameState.SINGLEPLAYERGAME.value;
	}
}