package clientSide;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Customize extends BasicGameState implements ServerTALK{
	
	Image ship1, ship2, ship3, ship4, ship5, ship6, ship7, ship8;
	public static String oldShipName = "spaceship1.gif";
	public static int ship1Width, ship2Width, ship3Width, ship4Width, ship5Width, ship6Width, ship7Width, ship8Width;
	public static int ship1Height, ship2Height, ship3Height, ship4Height, ship5Height, ship6Height, ship7Height, ship8Height;
	public static int ix = 1;
	Font font;
	public DataOutputStream out = null;
	public DataInputStream in = null;
	public Socket mySocket = null;

	public Customize(int state){
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
		ship1 = new Image("res/spaceship1.gif");
		ship1Width = ship1.getWidth()/2;
		ship1Height = ship1.getHeight()/2;
		ship2 = new Image("res/spaceship2.gif");
		ship2Width = ship2.getWidth()/2;
		ship2Height = ship2.getHeight()/2;
		ship3 = new Image("res/spaceship3.gif");
		ship3Width = ship3.getWidth()/2;
		ship3Height = ship3.getHeight()/2;
		ship4 = new Image("res/spaceship4.gif");
		ship4Width = ship4.getWidth()/2;
		ship4Height = ship4.getHeight()/2;
		ship5 = new Image("res/spaceship5.gif");
		ship5Width = ship5.getWidth()/2;
		ship5Height = ship5.getHeight()/2;
		ship6 = new Image("res/spaceship6.gif");
		ship6Width = ship6.getWidth()/2;
		ship6Height = ship6.getHeight()/2;
		ship7 = new Image("res/spaceship7.gif");
		ship7Width = ship7.getWidth()/2;
		ship7Height = ship7.getHeight()/2;
		ship8 = new Image("res/spaceship8.gif");
		ship8Width = ship8.getWidth()/2;
		ship8Height = ship8.getHeight()/2;
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		// Draw the big rectangle
		g.drawRect((Main.width/2) - 390, (Main.height/2) - 200, 780, 400);

		g.setColor(Color.white);
		// First Row
		ship1.draw((Main.width/2) - 390 + 95 - ship1Width,  (Main.height/2) - 200 + 75 - ship1Height);
		if(MultiplayerGame.shipName.equals("spaceship1.gif")) {
			g.drawRect((Main.width/2) - 390 + 60, (Main.height/2) - 200 + 40, 70, 70);
			ix = 1;
		}
		
		ship2.draw((Main.width/2) - 390 + 295 - ship2Width, (Main.height/2) - 200 + 75 - ship2Height);
		if(MultiplayerGame.shipName.equals("spaceship2.gif")) {
			g.drawRect((Main.width/2) - 390 + 260, (Main.height/2) - 200 + 40, 70, 70);
			ix = 2;
		}
		
		ship3.draw((Main.width/2) - 390 + 495 - ship3Width, (Main.height/2) - 200 + 75 - ship3Height);
		if(MultiplayerGame.shipName.equals("spaceship3.gif")) {
			g.drawRect((Main.width/2) - 390 + 460, (Main.height/2) - 200 + 40, 70, 70);
			ix = 3;
		}
		
		ship4.draw((Main.width/2) - 390 + 695 - ship4Width, (Main.height/2) - 200 + 75 - ship4Height);
		if(MultiplayerGame.shipName.equals("spaceship4.gif")) {
			g.drawRect((Main.width/2) - 390 + 660, (Main.height/2) - 200 + 40, 70, 70);
			ix = 4;
		}
		
		// Second Row
		ship5.draw((Main.width/2) - 390 + 95 - ship5Width,  (Main.height/2) - 200 + 335 - ship5Height);
		if(MultiplayerGame.shipName.equals("spaceship5.gif")) {
			g.drawRect((Main.width/2) - 390 + 60, (Main.height/2) - 200 + 300, 70, 70);
			ix = 5;
		}
		
		ship6.draw((Main.width/2) - 390 + 295 - ship6Width, (Main.height/2) - 200 + 335 - ship6Height);
		if(MultiplayerGame.shipName.equals("spaceship6.gif")) {
			g.drawRect((Main.width/2) - 390 + 260, (Main.height/2) - 200 + 300, 70, 70);
			ix = 6;
		}
		
		ship7.draw((Main.width/2) - 390 + 495 - ship7Width, (Main.height/2) - 200 + 335 - ship7Height);
		if(MultiplayerGame.shipName.equals("spaceship7.gif")) {
			g.drawRect((Main.width/2) - 390 + 460, (Main.height/2) - 200 + 300, 70, 70);
			ix = 7;
		}
		
		ship8.draw((Main.width/2) - 390 + 695 - ship8Width, (Main.height/2) - 200 + 335 - ship8Height);
		if(MultiplayerGame.shipName.equals("spaceship8.gif")) {
			g.drawRect((Main.width/2) - 390 + 660, (Main.height/2) - 200 + 300, 70, 70);
			ix = 8;
		}
		
		g.drawRect((Main.width/2) - 390 + 495, (Main.height/2) - 200 + 450, 63, 20);
		g.drawString("Cancel", (Main.width/2) - 390 + 500, (Main.height/2) - 200 + 450);
		g.drawRect((Main.width/2) - 390 + 595, (Main.height/2) - 200 + 450, 126, 20);
		g.drawString("Apply Changes", (Main.width/2) - 390 + 600, (Main.height/2) - 200 + 450);
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
		Input input = gc.getInput();
		int posX = Mouse.getX();
		int posY = Mouse.getY();

		// Show1
		if((posX>(Main.width/2) - 390 + 60 && posX<(Main.width/2) - 390 + 130)&&(posY>(Main.height/2) - 300 + 390 && posY<(Main.height/2) - 300 + 460)) {
			if(Mouse.isButtonDown(0)){
				ix = 1;
				MultiplayerGame.shipName = "spaceship1.gif";
			}
		}

		// Show2
		if((posX>(Main.width/2) - 390 + 260 && posX<(Main.width/2) - 390 + 330)&&(posY>(Main.height/2) - 300 + 390 && posY<(Main.height/2) - 300 + 460)){
			if(Mouse.isButtonDown(0)){
				ix = 2;
				MultiplayerGame.shipName = "spaceship2.gif";
			}
		}

		// Show3
		if((posX>(Main.width/2) - 390 + 460 && posX<(Main.width/2) - 390 + 530)&&(posY>(Main.height/2) - 300 + 390 && posY<(Main.height/2) - 300 + 460)){
			if(Mouse.isButtonDown(0)){
				ix = 3;
				MultiplayerGame.shipName = "spaceship3.gif";
			}
		}

		// Show4
		if((posX>(Main.width/2) - 390 + 660 && posX<(Main.width/2) - 390 + 731)&&(posY>(Main.height/2) - 300 + 390 && posY<(Main.height/2) - 300 + 460)){
			if(Mouse.isButtonDown(0)){
				ix = 4;
				MultiplayerGame.shipName = "spaceship4.gif";
			}
		}

		// Show5
		if((posX>(Main.width/2) - 390 + 60 && posX<(Main.width/2) - 390 + 130)&&(posY>(Main.height/2) - 300 + 130 && posY<(Main.height/2) - 300 + 200)){
			if(Mouse.isButtonDown(0)){
				ix = 5;
				MultiplayerGame.shipName = "spaceship5.gif";
			}
		}

		// Show6
		if((posX>(Main.width/2) - 390 + 260 && posX<(Main.width/2) - 390 + 330)&&(posY>(Main.height/2) - 300 + 130 && posY<(Main.height/2) - 300 + 200)){
			if(Mouse.isButtonDown(0)){
				ix = 6;
				MultiplayerGame.shipName = "spaceship6.gif";
			}
		}

		// Show7
		if((posX>(Main.width/2) - 390 + 460 && posX<(Main.width/2) - 390 + 530)&&(posY>(Main.height/2) - 300 + 130 && posY<(Main.height/2) - 300 + 200)){
			if(Mouse.isButtonDown(0)){
				ix = 7;
				MultiplayerGame.shipName = "spaceship7.gif";
			}
		}

		// Show8
		if((posX>(Main.width/2) - 390 + 660 && posX<(Main.width/2) - 390 + 731)&&(posY>(Main.height/2) - 300 + 130 && posY<(Main.height/2) - 300 + 200)){
			if(Mouse.isButtonDown(0)){
				ix = 8;
				MultiplayerGame.shipName = "spaceship8.gif";
			}
		}
	
		// Cancel Button
		if((posX>(Main.width/2) - 390 + 495 && posX<(Main.width/2) - 390 + 560) && (posY>(Main.height/2) - 200 - 70 && posY<(Main.height/2) - 200 - 50)){
			if(Mouse.isButtonDown(0)){
				MultiplayerGame.shipName = oldShipName;
				sbg.enterState(Main.gameState.MENU.value);
			}
		}
		
		// Apply Changes Button
		if((posX>(Main.width/2) - 390 + 595 && posX<(Main.width/2) - 390 + 722)&&(posY>(Main.height/2) - 200 - 70 && posY<(Main.height/2 - 200 - 50))){
			if(Mouse.isButtonDown(0)){
				apply();
				sbg.enterState(Main.gameState.MENU.value);				
			}
		}
		

		if(input.isKeyPressed(Input.KEY_LEFT)) {
			ix--;
			if(ix < 1) {
				ix = 8;
			} else if(ix > 8) {
				ix = 1;
			} 
			MultiplayerGame.shipName = "spaceship" + ix + ".gif";
		}
		if(input.isKeyPressed(Input.KEY_RIGHT)) {
			ix++;
			if(ix < 1) {
				ix = 8;
			} else if(ix > 8) {
				ix = 1;
			} 
			MultiplayerGame.shipName = "spaceship" + ix + ".gif";
		}
		if(input.isKeyPressed(Input.KEY_DOWN)) {
				 if(ix == 1) {ix = 5;}
			else if(ix == 2) {ix = 6;}
			else if(ix == 3) {ix = 7;}
			else if(ix == 4) {ix = 8;} 
			else if(ix == 5) {ix = 1;} 
			else if(ix == 6) {ix = 2;} 
			else if(ix == 7) {ix = 3;} 
			else if(ix == 8) {ix = 4;}
			MultiplayerGame.shipName = "spaceship" + ix + ".gif";
		}
		if(input.isKeyPressed(Input.KEY_UP)) {
				 if(ix == 1) {ix = 5;}
			else if(ix == 2) {ix = 6;}
			else if(ix == 3) {ix = 7;}
			else if(ix == 4) {ix = 8;} 
			else if(ix == 5) {ix = 1;} 
			else if(ix == 6) {ix = 2;} 
			else if(ix == 7) {ix = 3;} 
			else if(ix == 8) {ix = 4;}
			MultiplayerGame.shipName = "spaceship" + ix + ".gif";
		}

		if(input.isKeyPressed(Input.KEY_ENTER)) {
			apply();
			sbg.enterState(Main.gameState.MENU.value);
		}
		if(input.isKeyPressed(Input.KEY_ESCAPE)) {
			MultiplayerGame.shipName = oldShipName;
			sbg.enterState(Main.gameState.MENU.value);
		}
	}
	
	public void apply() {
		Main.user.setShipName(MultiplayerGame.shipName);
		SendInfo(Main.user.ups.Encode(), 2);
	}

	public int getID(){
		return 1;
	}

	@Override
	public String RecieveInfo() {
		return null;
	}

	@Override
	public String SendInfo(String str, int num) {
		boolean setup = SetUpConnection();
		if (setup){
			try{
				out.writeInt(num);
				//System.out.println("OO " + str);
				out.writeUTF(str);
				in.readBoolean();
				CloseConnection();
			} catch(Exception e){
				System.out.println("Cannot set up connection");
				// Also, error box here
			}
		}
		return null;
	}

	@Override
	public boolean SetUpConnection() {
		try{
			mySocket = new Socket(Main.ip, Main.port);		// 192.168.0.3 			// 75.175.60.225
			out = new DataOutputStream(mySocket.getOutputStream());
			in = new DataInputStream(mySocket.getInputStream());
		} catch (IOException ioe){
			System.out.println("ERROR IN SETUPCONNECTION()");
			// set up pop-ups later
			CloseConnection();
			return false;
		}
		return true;
	}

	@Override
	public void gameTalk() {
		return;
	}

	@Override
	public boolean CloseConnection() {
		try {
			out.close();
		} catch (IOException e1) {
			System.out.println("^^User could not close output stream" + e1.getMessage());
		}
		try {
			in.close();
		} catch (IOException e1) {
			System.out.println("^^User could not close input stream" + e1.getMessage());
		}
		try {
			mySocket.close();
		} catch (IOException e1) {
			System.out.println("^^User could not close Socket" + e1.getMessage());
		}
		return true;
	}
}
