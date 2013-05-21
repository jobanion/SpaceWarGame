package clientSide;

import org.newdawn.slick.SlickException;

public class AI extends Ship{
	Ship target;
	
	public AI(float x, float y) throws SlickException {
		super("AI", x, y, "EnemyShip.gif");
		this.isHit = false;
	}
	
	public int leftOrRight() { // Tells the AI whice way to turn to go towards the player
		target = nearestPlayer();
		if(target == null) {
			SpaceGame.playerDead = true;
			return 1;
		}
		float x = target.x - this.x;
		float y = target.y - this.y;
		float arcsin = (float) Math.asin(x/(Math.sqrt(x*x + y*y))) * 57.2957795f;
		float arccos = (float) Math.acos(y/(Math.sqrt(x*x + y*y))) * 57.2957795f;
		float arctan = (float) Math.atan(y/x) * 57.2957795f;
		float rotation = this.ship.getRotation();
		
		if(arcsin >= 0) {
			if(arctan <= rotation - 90f) {
				return -1;
			}
			return 1;
		} else {
			if(arccos >= rotation && arccos <= rotation + 180f) {
				return -1;
			}
			return 1;
		}
	}
	
	public Ship nearestPlayer() {
		Ship closest = null;
		for(Ship current : SpaceGame.ships) {
			if(current.getClass() != AI.class) {
				if(closest == null) {
					closest = current;
				} else {
					if(distanceBetween(this.point, current.point) < distanceBetween(this.point, closest.point)) {
						closest = current;
					}
				}
			}
		}
		return closest;
	}
	
	public float distanceBetween( Point A, Point B) {
        float dX = A.x - B.x;
        float dY = A.y - B.y;
		return (float) Math.sqrt(dX*dX + dY*dY);
	}
}

class Point {
	float x, y;
	public Point(float x, float y) 	{ this.x = x; this.y = y; }
	public void setX(float x)	  	{ this.x = x; }
	public void setY(float y)		{ this.y = y; }
	public float getX()				{ return this.x; }
	public float getY()				{ return this.y; }
}
