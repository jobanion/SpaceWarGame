package clientSide;

import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

// the left(left) and shoot(space) cancel each other out for the player1 
// the right(d) and shoot(f) on player2 cancel each other out as well

@SuppressWarnings("deprecation")
public class SpaceGame extends BasicGameState implements ServerTALK{

	String backgroundImage = "SpaceWormHole.jpg";

	public static List<Ship> ships = new CopyOnWriteArrayList<Ship>();
	public static List<AI> aiShips = new CopyOnWriteArrayList<AI>();
	public static List<Ship> deadShips = new CopyOnWriteArrayList<Ship>();

	// Server communication stuff
	//int port = 3865;
	public static Socket client;
	public static DataOutputStream out;
	public static DataInputStream in;
	boolean canTalk;

	//The ships (and post-initialization stuff)
	Ship pShip1;// = new Ship("player", 400, 300, shipName1), 
	private boolean initial = false;

	public static float x, y, rotation;
	public static String shipName, myName;		/** note: set myName!!!!!!		*/
	//	static Ship newShip1, newShip2;
	//	static AI newAIShip;
	public static Image ship1 = null, ship2 = null, aiShip = null, background = null, shot = null, explosion = null, smoody = null;
	static Shot currentShot1 = null, currentShot2 = null, aiShot = null;
	static boolean menu = false, shotFired1 = false, shotFired2 = false, shotFiredAI = false, exploded1 = false, exploded2 = false;
	public static String shipName1 = "spaceship1.gif", shipName2 = "spaceship1.gif";
	public static long respawnTimer = 0, playerScore = 0;
	public static int width = 800, height = 500, d = 0, killCount = 0, deathCount = 0;	
	public static float kdr;
	public static int count = 0, numOfAI = 1, wonMiddle, notMiddle, wave = 1;
	static boolean init = false, playerDead = false;
	public String respawn = "Respawn (R)", resume = "Resume (R)", mainMenu = "Main Menu (M)", exit = "Quit Game (Q)", won = "You Won!!", not = "NOT!!";
	TrueTypeFont font4, font5;
	public SpaceGame(int state) {
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		if(!init){
			respawnAI();

			//			ships.add(pShip1);
			//			respawnAI();

			/// set up Communication stuff with server (XXX MOVED TO SERVERCHOOSE)
//			try{
//				client = new Socket("192.555.5.5", port);		// 10.6.53.208 			// 75.175.60.225
//				out = new DataOutputStream(client.getOutputStream());
//				in = new DataInputStream(client.getInputStream());
//				canTalk = true;
//			} catch (Exception ioe){
//				System.out.println("Could not set up Server communications!");
//				canTalk = false;
//			}
			/// communication set up

			background = new Image("res/" + backgroundImage);
			explosion = new Image("res/explosion.gif");
			smoody = new Image("res/smoody.gif");
			width = gc.getWidth();
			height = gc.getHeight();
			init = true;
			
			Font awtFont4 = new Font("Calibre", Font.BOLD, 32);
			font4 = new TrueTypeFont(awtFont4, false);
			wonMiddle = Main.width/2 - font4.getWidth(won);
			Font awtFont5 = new Font("Calibre", Font.BOLD, 64);
			font5 = new TrueTypeFont(awtFont5, false);
			notMiddle = Main.width/2 - font5.getWidth(not);
		}
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		background.draw(0, 0);
		
//		g.drawString("Score: " + playerScore, Main.width -200, 10);
		g.drawString("Enemies Left: " + count, Main.width - 200, 30);
		g.drawString("Kills: " + killCount, Main.width - 200, 50);
		g.drawString("Deaths: " + deathCount, Main.width - 200, 70);
		if(deathCount > 0) {
			kdr = (float)killCount/deathCount;
		} else {
			kdr = killCount;
		}
		g.drawString("K/D Ratio: " + kdr, Main.width - 200, 90);
		
		for(Ship current : ships){
			current.render(gc, sbg, g);
		}
		
		if(playerDead == true) {
			g.drawString(respawn,    (Main.width/2) - respawn.length(), 200);
			g.drawString(mainMenu, (Main.width/2) - mainMenu.length(), 250);
			g.drawString(exit, (Main.width/2) - exit.length(), 300);
			if(playerDead == false){
				g.clear();
			}
		}
		
		if(menu == true){
			g.drawString(resume,    (Main.width/2) - resume.length(), 200);
			g.drawString(mainMenu, (Main.width/2) - mainMenu.length(), 250);
			g.drawString(exit, (Main.width/2) - exit.length(), 300);
			if(menu == false){
				g.clear();
			}
		}	

//		if(aiShips.isEmpty() && System.currentTimeMillis() <= respawnTimer + 7000) {
//			font4.drawString(wonMiddle, Main.height/2, won, Color.green);
//		} else if(System.currentTimeMillis() <= respawnTimer + 8000)
//			font5.drawString(notMiddle, Main.height/2, not, Color.red);
		
		if(wave == 2 && aiShips.isEmpty()){
			g.drawImage(smoody, 200, Main.height/2);
			g.drawString("So you've got some skill.", 500, Main.height/2+100);
			g.drawString("Lets see you take on these!!", 500, Main.height/2+120);
		}
		
		for( Ship current : deadShips){
			if(System.currentTimeMillis() <= current.explosionTime+300)
			explosion.draw(current.x-15, current.y-10);
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (initial)
			initial();
		if (canTalk)
			gameTalk();
		//otherDoStuff
		//

		if(!menu)
			aiDoStuff(delta);
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
			if(current.getClass() != AI.class && current.getClass() != OtherPlayer.class)
				current.update(gc, sbg, delta, input);
		}		

		// Escape key/ key to open the menus in game
		if(input.isKeyDown(Input.KEY_ESCAPE)){
			menu = true;
		}      
		
		if(playerDead){
			if(!gc.isPaused()){ // Tells the game to pause when the menu is brought up
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
				sbg.enterState(0);
				initial = true;
				try{
					Thread.sleep(250);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			
			if(input.isKeyDown(Input.KEY_Q)){  // Closes the program
				Menu.exit();
			}
		}

		// When escape is hit, this tells the menu that it can open
		if(menu){
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
				sbg.enterState(0);
				// Joseph's stuff (for when the user leaves a server)
				reset();

				try{
					Thread.sleep(250);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}

			if(input.isKeyDown(Input.KEY_Q)){  // Closes the program
				Menu.exit();
			}
		}
	}
	
	private static void respawnPlayer() {
		Random gen1 = new Random(), gen2 = new Random();
		Ship ship;
		try {
			ship = new Ship(myName, gen1.nextInt(Main.width), gen2.nextInt(Main.height), shipName1);
			ships.add(ship);
		} catch (SlickException e) {
		}
	}

	private void reset() {
		ships.clear();
		aiShips.clear();
		initial = true;
		try{
			if (canTalk){
				out.writeUTF("XXX");			// the XXX will be the signifier that the user has left, the other stuff is just for fun :)
				System.out.println("User has left the game room!");
				out.close();
				in.close();
				client.close();
			}

		} catch (IOException ioe){
			System.out.println("Error giving confirmation that user has left the game!" + ioe.getMessage());
			ioe.printStackTrace();
		}
	}

	// State based stuff thingie
	public int getID(){
		return 2;
	}

	public static void aiDoStuff(int delta) {
		if(aiShips.isEmpty() && System.currentTimeMillis() >= respawnTimer + 2000 && wave == 3){
			wave++;
			respawnAI();
		} else if(aiShips.isEmpty() && System.currentTimeMillis() >= respawnTimer + 2000){
			wave++;
			respawnAI();
		}
		aiRotate();
		aiMove(delta);
		aiShot();
	}

	private static void respawnAI() {
		for(int i = 0; i < numOfAI; i++){
			Random gen1 = new Random(), gen2 = new Random();
			AI ai;
			try {
				ai = new AI(gen1.nextInt(Main.width), gen2.nextInt(Main.height));
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
		for(AI current : aiShips ){
			if(!current.isHit && current.target != null){
				if(!current.shotFired) {
					current.currentShot = new Shot(current.x+10, current.y+5, current.ship.getRotation(), 3);
					current.shotFired = true;
				} else if(!current.currentShot.shotVisible) {
					current.currentShot = new Shot(current.x+10, current.y+5, current.ship.getRotation(), 3);
				}
			}
		}
	}

	private static void aiMove(int delta) {
		for(AI current : aiShips ){
			current.move(delta * 0.3f, current.ship.getRotation());
		}
	}

	private static void aiRotate() {
		for(AI current : aiShips ){
			current.rotate(current.leftOrRight() * 1f, current.ship);
		}
	}

	public String packShip(){

		return myName + "|" + String.valueOf(x) +  "|" + String.valueOf(y) + "|" + String.valueOf(rotation) + "|" + shipName;
	}


	/**
	 * Should take the input string (from the server) and update every ship/create new ships based on the strings values (parsed accordingly)
	 * @param positions
	 * @throws SlickException
	 */
	public void unPackShip(String positions) throws SlickException {
		String[] plrs = positions.split("#");
		boolean check = false;
		for (String plr : plrs){
			String[] player = plr.split("|");
			for (Ship ship: ships){
				if (player[0].equals(ship.myName) && ship.getClass() == OtherPlayer.class){
					ship.x = Float.parseFloat(player[1]);
					ship.y = Float.parseFloat(player[2]);
					ship.rotation = Float.parseFloat(player[3]);
					ship.update(null, null, 0, null);
					check = true;
					break;
				}
			}
			if (!check){
				OtherPlayer newplayer = new OtherPlayer(player);
				ships.add(newplayer);
				newplayer.update(null, null, 0, null);
			}

		}
	}

	@Override
	public String SendInfo(String str, int num) {
		return null;
	}

	@Override
	public String RecieveInfo(){

		return null;
	}
	@Override
	public boolean SetUpConnection() {
		return true;
	}

	@Override
	public boolean CloseConnection() {
		return true;
	}

	/**
	 * Reads new information from the game
	 */
	@Override
	public void gameTalk() {
		String fromServ = null;
		boolean rw = false;

		try{

			try{
				fromServ = in.readUTF();
				System.out.println(fromServ + "did it");
				out.writeUTF(packShip());
				System.out.println(fromServ + "did itX2");
				rw = true;
			} catch (IOException ioe){
				System.out.println("could not get data!");
				rw = false;
			}
			if (rw){
				unPackShip(fromServ);
			}
		} catch (Exception e){
			System.out.println("##EXIT INCOMPLETE: FATAL ERROR::\n" + e.getMessage());
		}

	}

	public static void spawnAI() {
		Random gen1 = new Random(), gen2 = new Random();
		AI ai;
		try {
			ai = new AI(gen1.nextInt(Main.width), gen2.nextInt(Main.height));
			ships.add(ai);
			aiShips.add(ai);
			count++;
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

	public void initial() throws SlickException{

		// add user ship(s)
		pShip1 = new Ship("player", 400, 300, shipName1); 

		ships.add(pShip1);
//		
//		String fromUser = null;
//		String toUser = null;
		
		// connect to server (and get boring stuff out of the way...)
//		int port = 3865;
//		try{
//			client = new Socket("localhost", port);		// 10.6.53.208 			// 75.175.60.225
//			out = new DataOutputStream(client.getOutputStream());
//			in = new DataInputStream(client.getInputStream());
//			canTalk = true;
//		} catch (Exception ioe){
//			System.out.println("Could not set up Server communications!");
//			canTalk = false;
//		}
//		/// if connection, then send initial information (quick and messy just for testing purposes)
//		if (canTalk){
//			try{
//				System.out.println("Yay, could connect");
//				out.writeInt(3);
//				out.writeUTF(Menu.userName + "|" + Menu.password + "|" + SpaceGame.shipName1);
//				
//				in.readBoolean();
//				fromUser = in.readUTF();	// initial stuff up to here (will make more sense when implemented in the correct class)
//				
//				out.writeInt(2);			// minor condition statement
//				out.writeUTF("PIE");		// server chosen
//				
//				if (in.readBoolean()){
//					out.writeBoolean(true);
//				}
//
//			} catch (IOException IOE) {
//				IOE.printStackTrace();
//			}
//		}
//
		initial = false;
	}
}