package clientSide;

public class Shot {

	//Shape shotShape = null;
	float x, y, rotation;
	long timeFired = 0;
	boolean shotVisible = false;
	public boolean hit = false;
	int shipNum;
	float hip;

	public Shot(float hip, float x, float y, float rotation, int shipNum){
		this.hip  = hip;
		this.x = x;
		this.y = y;
		this.rotation = rotation;
		this.timeFired = System.currentTimeMillis();
		this.shotVisible = true;
		this.shipNum = shipNum;
	}

	public void updateShot(){
		this.x += hip*Math.sin(Math.toRadians(this.rotation));
		this.y -= hip*Math.cos(Math.toRadians(this.rotation));
		if(this.timeFired + 800 < System.currentTimeMillis()) {
			shotVisible = false;
		}
		
		if(shipNum != 3) {
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
		isCollision();
	}
	
	public void isCollision() {
		for(Ship current : SpaceGame.ships){
			current.isCollision();
		}
//		if(shipNum == 1) {
//			if (this.x < SpaceGame.newShip2.x + SpaceGame.newShip2.ship.getWidth() && this.x > SpaceGame.newShip2.x && this.y < SpaceGame.newShip2.y + SpaceGame.newShip2.ship.getHeight() && this.y > SpaceGame.newShip2.y && !SpaceGame.newShip2.isHit) { 
//				System.out.println("HIT P2!!");
//				SpaceGame.newShip2.isHit = true;
//				hit = true;
//				SpaceGame.explosionTime2 = System.currentTimeMillis();
//			} else if (this.x < SpaceGame.newAIShip.x + SpaceGame.newAIShip.ship.getWidth() && this.x > SpaceGame.newAIShip.x && this.y < SpaceGame.newAIShip.y + SpaceGame.newAIShip.ship.getHeight() && this.y > SpaceGame.newAIShip.y && !SpaceGame.newAIShip.isHit) { 
//				System.out.println("HIT AI!!");
//				SpaceGame.newAIShip.isHit = true;
//				hit = true;
//				SpaceGame.explosionTime3 = System.currentTimeMillis();
//			}
//		}
//		
//		if(shipNum == 2) {
//			if (this.x < SpaceGame.newShip1.x + SpaceGame.newShip1.ship.getWidth() && this.x > SpaceGame.newShip1.x && this.y < SpaceGame.newShip1.y + SpaceGame.newShip1.ship.getHeight() && this.y > SpaceGame.newShip1.y && !SpaceGame.newShip1.isHit) { 
//				System.out.println("HIT P1!!");
//				SpaceGame.newShip1.isHit = true;
//				hit = true;
//				SpaceGame.explosionTime1 = System.currentTimeMillis();
//			} else if (this.x < SpaceGame.newAIShip.x + SpaceGame.newAIShip.ship.getWidth() && this.x > SpaceGame.newAIShip.x && this.y < SpaceGame.newAIShip.y + SpaceGame.newAIShip.ship.getHeight() && this.y > SpaceGame.newAIShip.y && !SpaceGame.newAIShip.isHit) { 
//				System.out.println("HIT AI!!");
//				SpaceGame.newAIShip.isHit = true;
//				hit = true;
//				SpaceGame.explosionTime3 = System.currentTimeMillis();
//			}
//		}
//		
//		if(shipNum == 3) {
//			if (this.x < SpaceGame.newShip1.x + SpaceGame.newShip1.ship.getWidth() && this.x > SpaceGame.newShip1.x && this.y < SpaceGame.newShip1.y + SpaceGame.newShip1.ship.getHeight() && this.y > SpaceGame.newShip1.y && !SpaceGame.newShip1.isHit) { 
//				System.out.println("HIT P1!!");
//				SpaceGame.newShip1.isHit = true;
//				hit = true;
//				SpaceGame.explosionTime1 = System.currentTimeMillis();
//			} else if (this.x < SpaceGame.newShip2.x + SpaceGame.newShip2.ship.getWidth() && this.x > SpaceGame.newShip2.x && this.y < SpaceGame.newShip2.y + SpaceGame.newShip2.ship.getHeight() && this.y > SpaceGame.newShip2.y && !SpaceGame.newShip2.isHit) { 
//				System.out.println("HIT P2!!");
//				SpaceGame.newShip2.isHit = true;
//				hit = true;
//				SpaceGame.explosionTime2 = System.currentTimeMillis();
//			}
//		}
	}
}
