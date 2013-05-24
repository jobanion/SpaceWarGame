package clientSide;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Ship {
	float x, y, rotation;
	Image ship;
	String myName;
	protected String shipName = "";
	public boolean isHit = false, shotFired = false;
	public long explosionTime = 0;
	public ShotSP currentShotSP = null;
	public ShotMP currentShotMP = null;
	Point point;
	float hip = 0;
	double rotL;
	double rotR;



	public Ship(String name, float x, float y, String shipName) throws SlickException {
		this.myName = name;
		this.shipName = shipName;
		this.ship = new Image("res/" + this.getShipName());
		this.x = x;
		this.y = y;
		this.point = new Point(x,y);
	}

	public void isSPCollision(){
		for(Ship current : SingleplayerGame.ships){
			if((this.getClass() == AIMP.class && 
					current.getClass() == AIMP.class) || 
					(this.getClass() == OtherPlayer.class && 
					current.getClass() == OtherPlayer.class)) { // Split up to be more legible

				continue;
			}

			if(this.currentShotSP != null && this != current) {
				if (this.currentShotSP.x < current.x + current.ship.getWidth() && this.currentShotSP.x > current.x &&
						this.currentShotSP.y < current.y + current.ship.getHeight() && this.currentShotSP.y > current.y && 
						!current.isHit && currentShotSP.shotVisible) { // Split up to be more legible

					System.out.println("HIT " + current.getShipName() + "!!");
					current.isHit = true;
					currentShotSP.hit = true;
					SingleplayerGame.ships.remove(current);

					if(current.getClass() == AIMP.class){
						SingleplayerGame.count--;
						SingleplayerGame.killCount++;
						SingleplayerGame.aiShips.remove(current);
					} else {
						SingleplayerGame.deathCount++;
					}
					if(SingleplayerGame.aiShips.isEmpty()) {
						SingleplayerGame.respawnTimer = System.currentTimeMillis();
					}
					this.currentShotSP.hit = true;
					this.explosionTime = System.currentTimeMillis();
				}
			}
		} 
	}

	public void isMPCollision() {
		for(Ship current : MultiplayerGame.ships){
			if((this.getClass() == AIMP.class && current.getClass() == AIMP.class) || 
					(this.getClass() == OtherPlayer.class && current.getClass() == OtherPlayer.class)) { 
				continue;
			}

			if(this.currentShotMP != null && this != current) {
				if (this.currentShotMP.x < current.x + current.ship.getWidth() && this.currentShotMP.x > current.x &&
						this.currentShotMP.y < current.y + current.ship.getHeight() && this.currentShotMP.y > current.y && 
						!current.isHit && currentShotMP.shotVisible) { // Split up to be more legible

					System.out.println("HIT " + current.getShipName() + "!!");
					current.isHit = true;
					currentShotMP.hit = true;
					MultiplayerGame.ships.remove(current);

					if(current.getClass() == AIMP.class){
						MultiplayerGame.count--;
						MultiplayerGame.killCount++;
						MultiplayerGame.aiShips.remove(current);
					} else {
						MultiplayerGame.deathCount++;
					}
					if(MultiplayerGame.aiShips.isEmpty()) {
						MultiplayerGame.respawnTimer = System.currentTimeMillis();
					}
					this.currentShotMP.hit = true;
					this.explosionTime = System.currentTimeMillis();
				}
			}
		} 
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta, Input input) throws SlickException{
//		if(input.isKeyDown(Input.KEY_C) && Menu.multi){
//			MultiplayerGame.respawnAI();
//		}
//		if(input.isKeyDown(Input.KEY_C) && !Menu.multi){
//			SingleplayerGame.respawnAI();
//		}

		float rotates = 0.2f * delta;

		if(input.isKeyDown(Input.KEY_LEFT) && !this.isHit) {
			this.rotate(-rotates, this.ship);
			Main.user.pos.setTurn(-rotates);
		} else if(input.isKeyDown(Input.KEY_RIGHT) && !this.isHit) {
			this.rotate(rotates, this.ship);
			Main.user.pos.setTurn(rotates);
		}

		if(input.isKeyDown(Input.KEY_UP) && !this.isHit && hip <= 3.1415926535897) {
			hip = (float) ((0.01f * delta) + hip);
			float rotation = this.ship.getRotation();

			this.move(hip, rotation);
		}

		if(input.isKeyDown(Input.KEY_DOWN) && !this.isHit && hip >= -3.1415926535897) {
			hip = (float) ((-1 * 0.01f * delta) + hip);
			float rotation = this.ship.getRotation();

			this.move(hip, rotation);
		}

		if(input.isKeyDown(Input.KEY_SPACE) && !this.isHit && Menu.multi == false){
			if(!shotFired && !gc.isPaused()) {
				currentShotSP = new ShotSP(hip + 3, this.x+10, this.y+5, this.ship.getRotation(), 1);
				shotFired = true;
			} else if(!currentShotSP.shotVisible) {
				currentShotSP = new ShotSP(hip + 3, this.x+10, this.y+5, this.ship.getRotation(), 1);
			}
		}
		
		if(input.isKeyDown(Input.KEY_SPACE) && !this.isHit && Menu.multi == true){
			if(!shotFired && !gc.isPaused()) {
				currentShotMP = new ShotMP(hip + 3, this.x+10, this.y+5, this.ship.getRotation(), 1);
				shotFired = true;
			} else if(!currentShotMP.shotVisible) {
				currentShotMP = new ShotMP(hip + 3, this.x+10, this.y+5, this.ship.getRotation(), 1);
			}
		}
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if(this.isHit == false && !gc.isPaused()) {
			this.move(hip, this.ship.getRotation());
			this.ship.draw(this.x, this.y);
		} else if(System.currentTimeMillis() <= this.explosionTime + 300) {
			MultiplayerGame.explosion.draw(this.x-15, this.y-10);	
		}

		if(shotFired && !Menu.multi) {
			if(currentShotSP.shotVisible && !currentShotSP.hit) {
				currentShotSP.updateShot();
				g.fillOval(currentShotSP.x, currentShotSP.y, 8, 8);
			} else if (currentShotSP.timeFired + 800 < System.currentTimeMillis()) {
				currentShotSP.hit = false;
				currentShotSP.shotVisible = false;
			}
		} else if(shotFired && Menu.multi) {
			if(currentShotMP.shotVisible && !currentShotMP.hit) {
				currentShotMP.updateShot();
				g.fillOval(currentShotMP.x, currentShotMP.y, 8, 8);
			} else if (currentShotMP.timeFired + 800 < System.currentTimeMillis()) {
				currentShotMP.hit = false;
				currentShotMP.shotVisible = false;
			}
		} 
	}

	public void rotate(float rotation, Image ship) {
		ship.rotate(rotation);
	}

	public String getShipName() {
		return shipName;
	}

	public void move(float hip, float rotation) {
		if(!isHit) {
			rotL = Math.sin(Math.toRadians(rotation));
			rotR = Math.cos(Math.toRadians(rotation));
			x += (hip * rotL);
			y -= (hip * rotR);
			Main.user.pos.setX(x);
			Main.user.pos.setY(y);

			// makes the sides loop
			if(x > Main.width) {
				x = 0;
			} else if(x < 0) {
				x = Main.width;
			}

			// Makes the top and bottom loop
			if(y > Main.height) {
				y = 0;
			} else if(y < 0) {
				y = Main.height;
			}
		}
	}

	public float getRotation(){
		return rotation;
	}
}
