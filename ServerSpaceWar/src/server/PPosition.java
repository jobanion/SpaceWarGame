package server;


/**
 * Structure to hold a single player position information (for game transmission).Every class has it's own unique way of encoding and decoding information, and so it will implement the interface "Encodable" which will implement methods for this class to encode and decode information
 * @author Boys
 *
 */
public class PPosition implements Encodable {
	//new coordinates of user ship
	public int x;
	public int y;
	public double turn;
	
	@Override
	public String Encode() {
		String encoding = String.valueOf(this.x) + "|" + String.valueOf(this.y) + "|" + String.valueOf(this.turn); // add to later
		return encoding;
	}
	@Override
	public void Decode(String encoding) {
		String[] values = encoding.split("\\|");
		this.x = Integer.parseInt(values[0]);
		this.y = Integer.parseInt(values[1]);
		this.turn = Double.parseDouble(values[2]);
		
	}
}
