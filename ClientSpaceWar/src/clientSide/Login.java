package clientSide;

import javax.swing.JFrame;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Login extends BasicGameState{

	public static boolean shown = false;
	
	public Login(int state) {
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		show();
	}

	// The method to show the login JDialog for logging in
	public static void show() {
		if(!shown) {
			shown = true;
			LoginForm gui = new LoginForm();
			gui.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			gui.setSize(525,150);
			gui.setLocationRelativeTo(null);
			gui.setVisible(true);
		}
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if(!Menu.accepted && !shown) {
			show();
		} else if(Menu.accepted && shown) {
			sbg.enterState(Main.gameState.MENU.value);
		}
	}

	public int getID() {
		return 3;
	}
}
