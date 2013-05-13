package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameRoom extends PublisherGME {
	
	public boolean isGoing;
	
	public GameRoom(String givenName){
		//peoplePos = Collections.synchronizedList(new ArrayList<String>());
		connected = new CopyOnWriteArrayList<SubscriberGME>();
		id = ServerMAIN.UsrNum++;
		myName = givenName;
		isGoing = true;
		setDaemon(true);		// Error may pop up here (if threads have strange errors)
	}
	
	public void run(){
		while (isGoing){
		//retreveInfo();
		//updateSubs();
		}
		close();
	}

	protected void FinishClose() {					// anything else for finalizing the closing of the gameroom
		ServerMAIN.addRemoveGame(this, false);
		
	}
	
	
	
}
