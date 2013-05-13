package clientSide;

import server.Encodable;

public class MyStats implements Encodable {
	protected int kills;
	protected int deaths;
	protected double kdr;
	
	@Override
	public String Encode() {
		String encoding = String.valueOf(this.kills) + "|" + String.valueOf(this.deaths) + "|" + String.valueOf(this.kdr); // add to later
		return encoding;
	}
	@Override
	public void Decode(String encoding) {
		String[] values = encoding.split("\\|");
		this.kills = Integer.parseInt(values[0]);
		this.deaths = Integer.parseInt(values[1]);
		this.kdr = Double.parseDouble(values[2]);
		
	}

}
