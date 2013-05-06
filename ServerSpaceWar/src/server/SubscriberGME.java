package server;

import java.awt.Color;
import java.io.IOException;
import java.net.SocketException;
import java.util.List;


/**
 * For the publisher subscriber pattern: The subscriber will send updates (of it'self) 
 * to the publisher and receive compiled updates from the subscriber.
 * @author Boys
 *
 */
abstract class SubscriberGME extends Fileio {

	//public List<PPosition> UpdateList;		// server updated "everything else" position info
	//public List<PPosition> pos;									// user updated position info (only using element 0, is like this because 
	public boolean state;
	public boolean contUp;
	public String myPos;
	public PublisherGME myServer;
	private int ix = 0;
	public List<String> toUpdate;

	protected void updateUpdateList() throws InterruptedException, SocketException{
		// update player with current string
		try {
			
			if (toUpdate.get(0).length() >= 3){
				out.writeUTF(toUpdate.get(0));					// send info to user
				Printu("gave string to user" + toUpdate.get(0));
			} else {
				out.writeUTF("JA|34.56|44.55|54.32|1");
				System.out.println("gave string to user:JA|34.56|44.55|54.32|1");
			}
			
			toUpdate.set(0, "");								// update info 
		} catch (IOException e) {
			Printu("Could not update User! " + e.getMessage() , Color.red);
			System.out.println("Could not update User!");
			//e.printStackTrace();
			throw new SocketException(); 
		}
	}

	protected void updatePos() throws InterruptedException, SocketException{
		// get newest user position
		try {
			myPos = in.readUTF();
//			Printu("Updated pos to server: " + myPos + ix++);
			if (myPos.equals("XXX")){
				state = false;
			}
			contUp = true;
		} catch (IOException e) {
			Printu("Could not update Thread!" , Color.red);
			e.printStackTrace();
			myPos = null;
			contUp = false;
		}
		if (contUp){
			for (SubscriberGME plr : myServer.connected){
				if (plr.id == this.id) 
					continue;
				try{
					plr.toUpdate.set(0, plr.toUpdate.get(0) + "#" +  myPos);//appending my new position to the end of each of the players aggregate lists (because it is a synchronized array list, I *kinda* assume no synchronization errors will occur)
				} catch (Exception e){
					Printu("Error in writing string to a user " + e.getMessage(), Color.red);
					throw new SocketException();
				}
			}
		}

	}

	protected void CheckStats(){
		// update Stats object with what is currently on the file
		String MyString = readFile(ServerMAIN.resources + ServerMAIN.slash + mif.myName + ServerMAIN.slash + "Stats.sts");

	}

	protected void UpdateStats(){
		// update stats currently on file with most recent stats
	}

	// ********************************for later*************************************************************************************
	private boolean UpdateMyStats(String Filepath){
		String values = readFile(Filepath);
		if (values == null){
			return false;
		}
		// reading all the values which were in the file in their appropriate fields
		stats.Decode(values);
		return true;
	}

}
