package clientSide;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Main extends StateBasedGame{

	public static User user;
	public static String ip = "localhost";
	public static int port = 3865;
	public static final String gamename = "Space War Game";
	public static final int menu = 0, customize = 1, playGame = 2, login = 3;	// that states
	public static int width = 1200, height = 650;							// Window dimensions

	public Main(String gamename){
		super(gamename);
		this.addState(new Menu(menu));
		this.addState(new Customize(customize));
		this.addState(new SpaceGame(playGame));
		this.addState(new Login(login));
	}

	public void initStatesList(GameContainer gc) throws SlickException{
		this.getState(menu).init(gc, this);
		this.getState(customize).init(gc, this);
		this.getState(playGame).init(gc, this);
		this.getState(login).init(gc, this);
		this.enterState(login);
	}

	// Does Tim's stuff not need to be in here?
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

//	public static void main(String[] args) {
//		AppGameContainer appgc;
//		try{
//			appgc = new AppGameContainer(new Main(gamename)){
//				@Override
//				protected void gameLoop() throws SlickException {
//					int delta = getDelta();
//					if (!Display.isVisible() && updateOnlyOnVisible) {
//						try { Thread.sleep(100); } catch (Exception e) {}
//					} else {
//						try {
//							updateAndRender(delta);
////							if(!SpaceGame.menu) { //!SpaceGame.newAIShip.isHit &&
////								SpaceGame.aiDoStuff(delta);
////							}
//						} catch (SlickException e) {
//							Log.error(e);
//							running = false;
//							return;
//						}
//					}
//
//					updateFPS();
//
//					Display.update();
//					
//					if (Display.isCloseRequested()) {
//						if (game.closeRequested()) {
//							running = false;
//						}
//					}
//				}
//			};
//			height = appgc.getScreenHeight() - 80;
//			width = appgc.getScreenWidth() - 15;
//			appgc.setDisplayMode(width, height, false);
//			appgc.setShowFPS(false);
//			appgc.start();
//		}catch(SlickException e){
//			System.out.println("Cannot load Game");
//		}
//	}
	
}