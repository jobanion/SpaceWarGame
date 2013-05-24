package clientSide;

public class User{
	public UInfo ups; // ups = UserPasswordShipnum
	public PosUpdate pos; // pos = position
	public MyStats stats; 	// Need to make this encrypted somehow
	private String shipName = "spaceship1.gif";
	//private Ship myShip;
	private int exp = 0;
	private int level = 0;

	public User(String uName, String pWord, int xp, int lvl, String ship) {
		ups = new UInfo();
		stats = new MyStats();
		pos = new PosUpdate();
		ups.setUsername(uName);
		ups.setPass(pWord);
		//setExp(xp);
		//setLevel(lvl);		// XXX remember this
		setShipName(ship);
	}

	// Getters and Setters from here down ###############################################
	
	
	
	public int getKills() {
		return stats.kills;
	}
	public void incKills(int exp) {
		this.stats.kills++;
	}
	
	public int getDeaths() {
		return stats.deaths;
	}
	public void incDeaths(int exp) {
		this.stats.deaths++;
	}
	
	public double getKDR() {
		return stats.kdr;
	}
	public void setStats(MyStats stats) {
		this.stats = stats;
	}
	
	
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
											// until we need these (if so, ill have to modify some classes)
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
		this.ups.shipNum = Integer.parseInt(shipName.substring(9, 10));	// should take the ship number out of the shipName (ex: the '1' out of "spaceship1.gif")
	}

}
