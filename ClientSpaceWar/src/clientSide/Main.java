package clientSide;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Main extends StateBasedGame{

	public static User user;
	public static String ip = "localhost";
	public static int port = 3865;
	public static final String gamename = "Space War Game";
	public static enum gameState{
		MENU(0), CUSTOMIZE(1), MULTIPLAYERGAME(2), LOGIN(3), SINGLEPLAYERGAME(4);
		int value;

		private gameState(int value) { // Because they are expected to be ints when being passed in
			this.value = value;
		}
	};
	public static int width = 1200, height = 650;	 // Window dimensions

	public Main(String gamename){
		super(gamename);
		this.addState(new Menu(gameState.MENU.value));
		this.addState(new Customize(gameState.CUSTOMIZE.value));
		this.addState(new MultiplayerGame(gameState.MULTIPLAYERGAME.value));
		this.addState(new Login(gameState.LOGIN.value));
		this.addState(new SingleplayerGame(gameState.SINGLEPLAYERGAME.value));
	}

	public void initStatesList(GameContainer gc) throws SlickException{
		this.getState(gameState.MENU.value).init(gc, this);
		this.getState(gameState.CUSTOMIZE.value).init(gc, this);
		this.getState(gameState.MULTIPLAYERGAME.value).init(gc, this);
		this.getState(gameState.LOGIN.value).init(gc, this);
		this.getState(gameState.SINGLEPLAYERGAME.value).init(gc, this);
		this.enterState(gameState.LOGIN.value);
	}

	public static void main(String[] args) {
		AppGameContainer appgc;
		try{
			appgc = new AppGameContainer(new Main(gamename));
			height = appgc.getScreenHeight() - 80;
			width = appgc.getScreenWidth() - 15;
			appgc.setDisplayMode(width, height, false);	
			appgc.setShowFPS(Menu.accepted);
			appgc.start();
		}catch(SlickException e){
			System.out.println("Cannot load Game");
		}
	}
}
