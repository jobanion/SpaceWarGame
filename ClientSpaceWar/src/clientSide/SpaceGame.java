package clientSide;

import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
	public static List<OtherPlayer> others = new CopyOnWriteArrayList<>();
	public static List<AI> aiShips = new CopyOnWriteArrayList<AI>();

	// Server communication stuff
	//int port = 3865;
	public static Socket client;
	public static DataOutputStream out;
	public static DataInputStream in;
	boolean canTalk;
	String fromUser = null;

	//The ships (and post-initialization stuff)
	Ship pShip1;// = new Ship("player", 400, 300, shipName1), 
	Ship pShip2;// = new Ship("player", 600, 300, shipName1);
	private boolean initial = true;

	//public static float x, y, rotation;
	//public static String shipName, myName;		/** note: set myName!!!!!!		*/
	//	static Ship newShip1, newShip2;
	//	static AI newAIShip;
	public static Image ship1 = null, ship2 = null, aiShip = null, background = null, shot = null, explosion = null;
	static Shot currentShot1 = null, currentShot2 = null, aiShot = null;
	static boolean menu = false, shotFired1 = false, shotFired2 = false, shotFiredAI = false, exploded1 = false, exploded2 = false;
	public static String shipName1 = "spaceship1.gif", shipName2 = "spaceship1.gif";
	public static long respawnTimer = 0;
	public static int width = 800, height = 500, d = 0, killCount = 0, deathCount = 0;	
	public static float kdr;
	public static int count = 0, numOfAI = 1, wonMiddle, notMiddle;
	static boolean init = false, playerDead = false;
	public String respawn = "Respawn (R)", resume = "Resume (R)", mainMenu = "Main Menu (M)", exit = "Quit Game (Q)", won = "You Won!!", not = "NOT!!";
	TrueTypeFont font4, font5;
	public SpaceGame(int state) {
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		if(!init){
			if (!initial){
				pShip1 = new Ship("player", 600, 300, shipName1);

				ships.add(pShip1);
				respawnAI();
			}
			//			ships.add(pShip1);
			//			ships.add(pShip2);
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

	@SuppressWarnings("deprecation")
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		background.draw(0, 0);
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

		g.drawString("Enemies Left: " + count, Main.width - 200, 10);
		g.drawString("Kills: " + killCount, Main.width - 200, 30);
		g.drawString("Deaths: " + deathCount, Main.width - 200, 50);
		if(deathCount > 0) {
			kdr = (float)killCount/deathCount;
		} else {
			kdr = killCount;
		}
		g.drawString("K/D Ratio: " + kdr, Main.width - 200, 70);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (initial)
			initial();
		if (canTalk)
			gameTalk();
		//otherDoStuff
		//

		if(!menu && !initial)
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
				sbg.enterState(0);
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
		if(menu==true){
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
				sbg.enterState(0);
				// Joseph's stuff (for when the user leaves a server)
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
			ship = new Ship(Main.user.ups.getUsername(), gen1.nextInt(Main.width), gen2.nextInt(Main.height), shipName1);
			ships.add(ship);
		} catch (SlickException e) {
		}
	}

	private void reset() {
		ships.clear();
		aiShips.clear();
		others.clear();
		initial = true;
		try{
			if (canTalk){
				out.writeUTF("XXX");			// the XXX will be the signifier that the user has left
				System.out.println("User has left the game room!");
				out.writeUTF(Main.user.stats.Encode());
				CloseConnection();
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
		if(aiShips.isEmpty() && System.currentTimeMillis() >= respawnTimer + 2000){
			//			if(System.currentTimeMillis() >= respawnTimer + 2000)
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
					current.currentShot = new Shot(1, current.x+10, current.y+5, current.ship.getRotation(), 3);
					current.shotFired = true;
				} else if(!current.currentShot.shotVisible) {
					current.currentShot = new Shot(1, current.x+10, current.y+5, current.ship.getRotation(), 3);
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
			current.rotate(current.leftOrRight() * 2f, current.ship);
		}
	}

	public String packShip(){

		return Main.user.ups.getUsername() + "|" +  Main.user.pos.Encode() + "|" + Main.user.getShipName();
	}


	/**
	 * Should take the input string (from the server) and update every ship/create new ships based on the strings values (parsed accordingly)
	 * @param positions
	 * @throws SlickException
	 */
	public void unPackShip(String positions) throws SlickException {
		int ix = 0;
		String[] plrs = positions.split("#");
		for (String hi: plrs){
			System.out.println(ix + ": " + hi);
		}
		boolean firstnull = false;
		boolean check = false;
		ix = 0;
		for (String plr : plrs){
			String[] player = plr.split("\\|");
			for (String place : player){
				System.out.println(ix + ": " + place);
			}
			if (firstnull){
				System.out.println("LPDS::" + player[0]  + "  " + player[1]);
				firstnull = false;
				continue;
			}

			// loop through the ships and check for every "otherplayer" (everything except your ship/player)
			// ## make new list, add/remove from both lists
			for (OtherPlayer ship: others){
				if (player[0].equals(ship.myName)){
					ship.x = Float.parseFloat(player[1]);
					ship.y = Float.parseFloat(player[2]);
					ship.rotation = Float.parseFloat(player[3]);
					ship.update(null, null, 0, null);
					ship.update = true;
					check = true;
					break;
				}
			}
			if (!check){
				OtherPlayer newplayer = new OtherPlayer(player);
				ships.add(newplayer);
				others.add(newplayer);
				newplayer.update(null, null, 0, null);
				newplayer.update = false;
			}
			for (OtherPlayer ship : others){
				if (ship.update == false){
					others.remove(ship);
					ships.remove(ship);
				} else {
					ship.update = false;
				}
			}

		}
	}

	@Override
	public String SendInfo(String str, int num) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String RecieveInfo(){

		return null;
	}
	@Override
	public boolean SetUpConnection() {
		try{
			client = new Socket(Main.ip, Main.port);		// 10.6.53.208 			// 75.175.60.225
			out = new DataOutputStream(client.getOutputStream());
			in = new DataInputStream(client.getInputStream());
			canTalk = true;
		} catch (Exception ioe){
			System.out.println("Could not set up Server communications!");
			canTalk = false;
			return false;
		}


		return true;
	}

	@Override
	public boolean CloseConnection() {
		try {							// try to close the connection, piece by piece
			if(out != null) 
				out.close();
		} catch (IOException e) {
			System.out.println("Could not close output stream");
		}

		try {
			if(in != null) 
				in.close();
		} catch (IOException e) {
			System.out.println("Could not close input stream");
		}

		try {
			if(client != null) 
				client.close();
		} catch (IOException e) {
			System.out.println("Could not close socket");
		}
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

				System.out.println("-" + fromServ + "did it");
				try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				out.writeUTF(packShip());
				System.out.println(fromServ + "did itX2");
				rw = true;
			} catch (IOException ioe){
				System.out.println("could not get data!" + ioe.getMessage());
				rw = false;
			}
			if (rw){
				System.out.println("TTT" + fromServ + "lTTT");


				unPackShip(fromServ);
			}
		} catch (Exception e){
			System.out.println("##EXIT INCOMPLETE: FATAL ERROR::\n" + e.getMessage());
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void initial() throws SlickException{
		int num = 3;
		boolean connect;
		// add user ship(s)
		pShip1 = new Ship("player", 400, 300, shipName1); 

		ships.add(pShip1);

		
		String toUser = null;

		// connect to server (and get boring stuff out of the way...)

		connect = SetUpConnection();

		/// if connection, then send initial information (quick and messy just for testing purposes)
		if (canTalk && connect){
			try{
				System.out.println("Yay, could connect");
				out.writeInt(num);
				out.writeUTF(Main.user.ups.getUsername() + "|" + Main.user.ups.getPass() + "|" + SpaceGame.shipName1);
				num = 2;
				
				in.readBoolean();
				fromUser = in.readUTF();	// initial stuff up to here (will make more sense when implemented in the correct class)
				System.out.println("<>" + fromUser);
				out.writeInt(num);			// minor condition statement
				out.writeUTF("PIE");		// server chosen

				if (in.readBoolean()){
					out.writeBoolean(true);
				}

			} catch (IOException IOE) {
				System.out.println("ERROR: " + IOE.getMessage());
				IOE.printStackTrace();
			}
		}
		System.out.println("Finished initializationJOE");
		initial = false; 									// set boolean variable = false, so it does not repeat initialization, and reset to true when go to main

		// extra testing stuff:
		try{
			fromUser = in.readUTF();
			if (fromUser != null)
				System.out.println("[[" + fromUser);
			try{
				System.out.println("PAUSED");
				Thread.sleep(700);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

		} catch (IOException ioe){
			System.out.println(";.;" + ioe.getMessage());
			ioe.printStackTrace();
		}
		// end of extra testing stuff

	}
}