package clientSide;

//import server.Encodable;

/**
 * User info (data structure for user login info and preferences)
 * @author Boys
 *
 */
public class UInfo implements Encodable{
	public String myName;
	public String myPassword;
	public int shipNum;
	//public int score; 		// dont need this right now
	
	public String getUsername() {
		return myName;
	}
	public void setUsername(String username) {
		this.myName = username;
	}
	
	public String getPass() {
		return myPassword;
	}
	public void setPass(String pass) {
		this.myPassword = pass;
	}
	
	@Override
	public String Encode() {
		String encoding = this.myName + "|" + this.myPassword + "|" + String.valueOf(this.shipNum); // add to later
		return encoding;
	}
	@Override
	public void Decode(String encoding) {
		String[] values = encoding.split("\\|");
		this.myName = values[0];
		this.myPassword = values[1];
		this.shipNum = Integer.parseInt(values[2]);
		
	}


}
