package clientSide;

public class Shot {

	//Shape shotShape = null;
	float x, y, rotation;
	long timeFired = 0;
	boolean shotVisible = false;
	public boolean hit = false;
	int shipNum;

	public Shot(float x, float y, float rotation, int shipNum){
		this.x = x;
		this.y = y;
		this.rotation = rotation;
		this.timeFired = System.currentTimeMillis();
		this.shotVisible = true;
		this.shipNum = shipNum;
	}

	public void updateShot(double hip){
		this.x += Math.sin(Math.toRadians(this.rotation)) * hip;
		this.y -= Math.cos(Math.toRadians(this.rotation)) * hip;
		if(this.timeFired + 800 < System.currentTimeMillis()) {
			shotVisible = false;
		}
		
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
		isCollision();
	}
	
	public void updateShot(){
		this.x += Math.sin(Math.toRadians(this.rotation));
		this.y -= Math.cos(Math.toRadians(this.rotation));
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
	}
}
