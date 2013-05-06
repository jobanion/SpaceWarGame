package clientSide;

/**
 * Structure to hold a single player position information (for game transmission).Every class has it's own unique way of encoding and decoding information, and so it will implement the interface "Encodable" which will implement methods for this class to encode and decode information
 * @author Boys
 *
 */
public class PosUpdate implements Encodable {
	//new coordinates of user ship
	public float x;
	public float y;
	public double turn;
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	public void setTurn(double turn){
		this.turn = turn;
	}
	
	public double getTurn(){
		return turn;
	}
	
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