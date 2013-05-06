package clientSide;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class OtherPlayer extends Ship {

	public boolean update;
	
	public OtherPlayer(String[] player) throws SlickException{
		this(player[0], Float.parseFloat(player[1]),Float.parseFloat(player[2]), Float.parseFloat(player[3]), "spaceship" + player[4] + ".gif");
	}

	public OtherPlayer(String name, float x, float y, float rotate, String shipName)throws SlickException {
		super(name, x, y, shipName);
		rotation = rotate;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta, Input input) throws SlickException{

		//ship.rotate(rotation);			// update ship rotation

		//		if(input.isKeyDown(Input.KEY_LEFT) && !this.isHit) {
		//			this.rotate(-0.2f * delta, this.ship);
		//		} else if(input.isKeyDown(Input.KEY_RIGHT) && !this.isHit) {
		//			this.rotate(0.2f * delta, this.ship);
		//		}
		//
		//		if(input.isKeyDown(Input.KEY_UP) && !this.isHit) {
		//			float hip = 0.4f * delta;
		//			float rotation = this.ship.getRotation();
		//
		//			this.move(hip, rotation);
		//		}
		//
		//		if(input.isKeyDown(Input.KEY_DOWN) && !this.isHit) {
		//			float hip = -1 * 0.4f * delta;
		//			float rotation = this.ship.getRotation();
		//
		//			this.move(hip, rotation);
		//		}
		//		
		//		if(input.isKeyDown(Input.KEY_SPACE) && !this.isHit){
		//			if(!shotFired && !gc.isPaused()) {
		//				currentShot = new Shot(this.x+10, this.y+5, this.ship.getRotation(), 1);
		//				shotFired = true;
		//			} else if(!currentShot.shotVisible) {
		//				currentShot = new Shot(this.x+10, this.y+5, this.ship.getRotation(), 1);
		//			}
		//		}
	}
}
