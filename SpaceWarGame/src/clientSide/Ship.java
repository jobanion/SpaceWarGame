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
	public Shot currentShot = null;
	Point point;
	int deltaMain = 0;

	
	
	public Ship(String name, float x, float y, String shipName) throws SlickException {
		this.myName = name;
		this.shipName = shipName;
		this.ship = new Image("res/" + this.getShipName());
		this.x = x;
		this.y = y;
		this.point = new Point(x,y);
	}

	public void isCollision(){
		for( Ship current : SpaceGame.ships){
			if(this.getClass() == AI.class && current.getClass() == AI.class)
				continue;
			if(this.currentShot != null && this != current) {
				if (this.currentShot.x < current.x + current.ship.getWidth() && this.currentShot.x > current.x && 
					this.currentShot.y < current.y + current.ship.getHeight() && this.currentShot.y > current.y && 
					!current.isHit && !currentShot.hit && currentShot.shotVisible) { 
					
					System.out.println("HIT " + current.getShipName() + "!!");
					current.isHit = true;
					SpaceGame.ships.remove(current);
					SpaceGame.deadShips.add(current);
					
					if(current.getClass() == AI.class){
						SpaceGame.count--;
						SpaceGame.killCount++;
						SpaceGame.aiShips.remove(current);

					} else {
						SpaceGame.deathCount++;

					}
					if(SpaceGame.aiShips.isEmpty())
						SpaceGame.respawnTimer = System.currentTimeMillis();

					this.currentShot.hit = true;
					current.explosionTime = System.currentTimeMillis();
				}
			}
		}
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta, Input input) throws SlickException{
		deltaMain = delta;
		if(input.isKeyDown(Input.KEY_C)){
			SpaceGame.spawnAI();
		}
		float rotates = 0.2f * delta;
		if(input.isKeyDown(Input.KEY_LEFT) && !this.isHit) {
			this.rotate(-rotates, this.ship);
			SpaceGame.rotation = -rotates;
		} else if(input.isKeyDown(Input.KEY_RIGHT) && !this.isHit) {
			this.rotate(rotates, this.ship);
			SpaceGame.rotation = rotates;
		}

		if(input.isKeyDown(Input.KEY_UP) && !this.isHit) {
			float hip = 0.4f * delta;
			float rotation = this.ship.getRotation();

			this.move(hip, rotation);
		}

		if(input.isKeyDown(Input.KEY_DOWN) && !this.isHit) {
			float hip = -1 * 0.4f * delta;
			float rotation = this.ship.getRotation();

			this.move(hip, rotation);
		}
		
		if(input.isKeyDown(Input.KEY_SPACE) && !this.isHit){
			if(!shotFired && !gc.isPaused()) {
				currentShot = new Shot(this.x+10, this.y+5, this.ship.getRotation(), 1);
				shotFired = true;
			} else if(!currentShot.shotVisible) {
				currentShot = new Shot(this.x+10, this.y+5, this.ship.getRotation(), 1);
			}
		}
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if(this.isHit == false)
			this.ship.draw(this.x, this.y);
		
		if(this.getClass() == AI.class) {
			if(shotFired) {
				if(currentShot.shotVisible && !currentShot.hit) {
					currentShot.updateShot();
					g.fillOval(currentShot.x, currentShot.y, 8, 8);
				} else if (currentShot.timeFired + 800 < System.currentTimeMillis()) {
					currentShot.hit = false;
					currentShot.shotVisible = false;
				}
			} 
		} else if(shotFired) {
			if(currentShot.shotVisible && !currentShot.hit) {
				currentShot.updateShot(deltaMain * 0.5f);
				g.fillOval(currentShot.x, currentShot.y, 8, 8);
			} else if (currentShot.timeFired + 800 < System.currentTimeMillis()) {
				currentShot.hit = false;
				currentShot.shotVisible = false;
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
			x += (hip * Math.sin(Math.toRadians(rotation)));
			y -= (hip * Math.cos(Math.toRadians(rotation)));
			SpaceGame.x = x;
			SpaceGame.y = y;
			// makes the sides loop
			if(x > SpaceGame.width) {
				x = 0;
			} else if(x < 0) {
				x = SpaceGame.width;
			}

			// Makes the top and bottom loop
			if(y > SpaceGame.height) {
				y = 0;
			} else if(y < 0) {
				y = SpaceGame.height;
			}
		}
	}
	
	public float getRotation(){
		return rotation;
	}
}
