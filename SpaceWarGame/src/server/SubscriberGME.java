package server;

import java.awt.Color;
import java.io.IOException;
import java.util.List;




/**
 * For the publisher subscriber pattern: The subscriber will send updates (of it'self) 
 * to the publisher and receive compiled updates from the subscriber.
 * @author Boys
 *
 */
abstract class SubscriberGME extends Fileio {

	public List<PPosition> UpdateList;		// server updated "everything else" position info
	//public List<PPosition> pos;									// user updated position info (only using element 0, is like this because 
	public boolean state;
	public boolean contUp;
	public String myPos;
	public PublisherGME myServer;
	private int ix = 0;
	public List<String> toUpdate;

	protected void updateUpdateList() throws InterruptedException{
		// update player with current string
		try {
			out.writeUTF(toUpdate.get(0));		
			Printu("gave string to user");                  // send info to user
			//toUpdate.set(0, "");							// update info 
		} catch (IOException e) {
			Printu("Could not update User!" , Color.red);
			e.printStackTrace();
		}
	}

	protected void updatePos() throws InterruptedException{
		// get newest user position
		try {
			myPos = in.readUTF();
			Printu("Updated pos to server: " + ix++);
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
					plr.toUpdate.set(0, plr.toUpdate.get(0) + "#" +  myPos);//appending my new position to the end of each of the players aggregate lists (because it is a synchronized array list, I *kinda* assume no errors will occur)
				} catch (Exception e){
					Printu("Error in writing string to a user", Color.red);
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
