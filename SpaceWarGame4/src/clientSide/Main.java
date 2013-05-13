package clientSide;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Main extends StateBasedGame{

	public static User user;
	public static String ip = "localhost";
	public static int port = 3865;
	public static final String gamename = "Space War Game";						// The window title 
	public static final int menu = 0, customize = 1, playGame = 2, login = 3;	// The states
	public static int width = 1200, height = 650;								// Window dimensions

	public Main(String gamename){
		super(gamename);
		this.addState(new Menu(menu));
		this.addState(new Customize(customize));
		this.addState(new SpaceGame(playGame));
		this.addState(new Login(login));
	}

	public void initStatesList(GameContainer gc) throws SlickException{
		int ix = 0;
		this.getState(menu).init(gc, this);
		this.getState(customize).init(gc, this);
		this.getState(login).init(gc, this);
		if(ix == 1) {
			this.getState(playGame).init(gc, this);
		}

		this.enterState(login);
	}

	public static void main(String[] args) {
		AppGameContainer appgc;
		try{
			appgc  = new AppGameContainer(new Main(gamename));
			height = appgc.getScreenHeight() - 80;
			width  = appgc.getScreenWidth()  - 15;
			appgc.setDisplayMode(width, height, false);
			appgc.setShowFPS(Menu.accepted);
			appgc.start();
		}catch(SlickException e){
			System.out.println("Cannot load Game");
		}
	}
}
