package clientSide;

import java.awt.Rectangle;

import org.newdawn.slick.Image;

public class Collisions {
	/** The rectangle used for this entity during collisions resolution */
	private Rectangle me = new Rectangle();
	/** The rectangle used for other entities during collision resolution */
	private Rectangle him = new Rectangle();
	
	public boolean shotCollidesWith(Image image1, Image image2, float shotX, float shotY) {
		me.setBounds((int) shotX, (int) shotY, image1.getWidth(), image1.getHeight());
//		him.setBounds(/*need to put the x and ys in here someplace I think*/ image2.getWidth(), image2.getHeight());

		return me.intersects(him);
	}
}
