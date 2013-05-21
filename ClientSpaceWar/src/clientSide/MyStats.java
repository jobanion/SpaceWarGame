package clientSide;


public class MyStats implements Encodable {
	protected int kills = 1;
	protected int deaths = 1;
	protected double kdr = 1;
	
	public int getKills() {
		return kills;
	}
	public void setKills(int kills) {
		this.kills = kills;
	}
	
	public int getDeaths() {
		return deaths;
	}
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	
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
