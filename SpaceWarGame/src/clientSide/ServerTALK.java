package clientSide;

interface ServerTALK {

	public String SendInfo(String str, int num);
	
	public String RecieveInfo();
	
	public boolean SetUpConnection();
	
	public boolean CloseConnection();
	
	public void gameTalk();
}
