package clientSide;

import java.awt.Font;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

@SuppressWarnings("deprecation")
public class Menu extends BasicGameState{

	public static Boolean showNoLogin = false, accepted = showNoLogin, shown = showNoLogin;
	public static String banner, playMI, customizeMI, exitMI, won = "You Won!!";
	int middle, playMiddle, customizeMiddle, exitMiddle, boxMiddle, wonMiddle;
	public static int ix = 1;
	TrueTypeFont font1, font2, font3, font4;
	
	public Menu(int state){
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
		Font awtFont1 = new Font("Times New Roman", Font.BOLD, 32);
		font1 = new TrueTypeFont(awtFont1, false);
		Font awtFont2 = new Font("Calibre", Font.PLAIN, 14);
		font2 = new TrueTypeFont(awtFont2, false);
		Font awtFont3 = new Font("Calibre", Font.BOLD, 18);
		font3 = new TrueTypeFont(awtFont3, false);
		Font awtFont4 = new Font("Calibre", Font.BOLD, 32);
		font4 = new TrueTypeFont(awtFont4, false);
		playMI = "Play Game";
		customizeMI = "Customize Ship";
		exitMI = "Exit";
		
		playMiddle = Main.width/2 - font3.getWidth(playMI)/2;
		customizeMiddle = Main.width/2 - font3.getWidth(customizeMI)/2;
		exitMiddle = Main.width/2 - font3.getWidth(exitMI)/2;
		boxMiddle = Main.width/2 - 150/2;
		wonMiddle = Main.width/2 - font4.getWidth(won);
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if(accepted) {
			// The welcome banner
			banner = "Welcome to the Game " + Main.user.ups.getUsername() + "!";
			middle = Main.width/2 - font1.getWidth(banner)/2;
			font1.drawString(middle, 100, banner, Color.yellow);

			// The sign in button
			g.setColor(Color.blue);
			g.drawRect((Main.width/2) + 295, (Main.height/2) - 240, 65, 20);
			font2.drawString((Main.width/2) + 300, (Main.height/2) - 240, "Sign Out", Color.blue);
			
			// The boxes surrounding the menuItems
			g.setColor(Color.white);
			g.drawRect(boxMiddle, (Main.height/2) - 250 + 150, 150, 26);
			g.drawRect(boxMiddle, (Main.height/2) - 250 + 200, 150, 26);
			g.drawRect(boxMiddle, (Main.height/2) - 250 + 250, 150, 26);
			
			// And the coloring so we can seee which menuItem is selected
			g.setColor(Color.green);
			if(ix == 1) {
				g.drawRect(boxMiddle, (Main.height/2) - 250 + 150, 150, 26);
				font3.drawString(playMiddle, (Main.height/2) - 250 + 150, "Play Game", Color.green);
				font3.drawString(customizeMiddle, (Main.height/2) - 250 + 200, "Customize Ship");
				font3.drawString(exitMiddle, (Main.height/2) - 250 + 250, "Exit");
			} else if(ix == 2) {
				g.drawRect(boxMiddle, (Main.height/2) - 250 + 200, 150, 26);
				font3.drawString(playMiddle, (Main.height/2) - 250 + 150, "Play Game");
				font3.drawString(customizeMiddle, (Main.height/2) - 250 + 200, "Customize Ship", Color.green);
				font3.drawString(exitMiddle, (Main.height/2) - 250 + 250, "Exit");
			} else if(ix == 3) {
				g.drawRect(boxMiddle, (Main.height/2) - 250 + 250, 150, 26);
				font3.drawString(playMiddle, (Main.height/2) - 250 + 150, "Play Game");
				font3.drawString(customizeMiddle, (Main.height/2) - 250 + 200, "Customize Ship");
				font3.drawString(exitMiddle, (Main.height/2) - 250 + 250, "Exit", Color.green);
			}
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
		Input input = gc.getInput();
		int posX = Mouse.getX();
		int posY = Mouse.getY();
		
		// Used to find where the edges that need checking are at by printing the mouse location to 
		// the console when it is clicked
//		if(Mouse.isButtonDown(0)){
//			System.out.println("Mouse is at: " + posX + ", " + posY + "");
//		}
		
		if(accepted) { 
			// Sign out button
			if((posX>(Main.width/2) + 295 && posX<(Main.width/2) + 360) && (posY>(Main.height/2) + 190 && posY<(Main.height/2) + 240)){
				if(Mouse.isButtonDown(0)){
					ix = 1;
					Login.shown = false;
					accepted = false;
					sbg.enterState(3);
				}
			}
			
			//play game button
			if((posX>(Main.width/2) - 75 && posX<(Main.width/2) + 75)&&(posY>(Main.height/2) + 75 && posY<(Main.height/2) + 100)) {
				if(Mouse.isButtonDown(0)){
					// PlayGame state
					ix = 1;
					sbg.init(gc);
					sbg.enterState(2);
				}
			}

			//customize button
			if((posX>(Main.width/2) - 75 && posX<(Main.width/2) + 75)&&(posY>(Main.height/2) + 25 && posY<(Main.height/2) + 50)) {
				if(Mouse.isButtonDown(0)){
					// Customize game state
					ix = 1;
					Customize.oldShipName = SpaceGame.shipName1;
					sbg.enterState(1);
				}
			}

			//exit game
			if((posX>(Main.width/2) - 75 && posX<(Main.width/2) + 75)&&(posY>(Main.height/2) - 25 && posY<(Main.height/2))) {
				if(Mouse.isButtonDown(0)){
					exit();
				}
			}
		
			// When the up key is pressed
			if(input.isKeyPressed(Input.KEY_UP)) {
				ix--;
				if(ix < 1) {
					ix = 3;
				} else if(ix > 3) {
					ix = 1;
				}
			}

			// When the down key is pressed
			if(input.isKeyPressed(Input.KEY_DOWN)) {
				ix++;
				if(ix < 1) {
					ix = 3;
				} else if(ix > 3) {
					ix = 1;
				}
			}

			// when the enter key is pressed
			if(input.isKeyPressed(Input.KEY_ENTER)) {
				if(ix == 1) {
					ix = 1;
					sbg.init(gc);
					sbg.enterState(2);
				} else if (ix == 2) {
					ix = 1;
					Customize.oldShipName = SpaceGame.shipName1;
					sbg.enterState(1);
				} else if (ix == 3) {
					exit();
				}
			}
		}
	}
	
	// The method to call when you want to close the program
	public static void exit() {
		System.exit(0);
	}

	// something needed for using the states
	public int getID(){
		return 0;
	}
}
