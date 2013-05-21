package clientSide;

public class Shot {

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
			if(x > MultiplayerGame.width) {
				x = 0;
			} else if(x < 0) {
				x = MultiplayerGame.width;
			}

			// Makes the top and bottom loop
			if(y > MultiplayerGame.height) {
				y = 0;
			} else if(y < 0) {
				y = MultiplayerGame.height;
			}
		}
		isCollision();
	}
	
	public void isCollision() {
		for(Ship current : MultiplayerGame.ships){
			current.isCollision();
		}
	}
}
