package clientSide;

public class ShotSP {

	float x, y, rotation;
	long timeFired = 0;
	boolean shotVisible = false;
	public boolean hit = false;
	int shipNum;
	float hip;

	public ShotSP(float hip, float x, float y, float rotation, int shipNum){
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
		isCollision();
	}
	
	public void isCollision() {
		for(Ship current : SingleplayerGame.ships){
			current.isSPCollision();
		}
	}
}
