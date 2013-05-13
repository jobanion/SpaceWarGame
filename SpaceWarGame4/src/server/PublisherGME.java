package server;

import java.util.List;


/**
 * For the observer pattern: Publisher will be implemented by a gameroom object and 
 * send/receive information from users (get user updates, compile them together and send everything out)
 * @author Boys
 *
 */
abstract class PublisherGME extends Thread {

	public String myName;										// dont really need both of these identifiers, but cant decide which one to use (between them..)
	public int id;
	public List<SubscriberGME> connected;
	//public List<String> peoplePos;
	//protected HashMap<Integer, PPosition> people;

	//	public void retreveInfo(){
	//												//connected.set(index, element);
	//		for (SubscriberGME next : connected){
	//			while (next.usinPos);
	//			if(next.usinPos == false)
	//				next.usinPos = true;
	//			else{
	//				System.out.println("Error reading user" + next.id);
	//			}
	//			people.set(next.id, next.pos);		// change logic here
	//		}
	//	}

	//	public void updateSubs(){
	//		for (SubscriberGME next: connected){
	//			while (next.usinUpdateList);
	//			if(next.usinUpdateList == false)
	//				next.usinUpdateList = true;
	//			else{
	//				System.out.println("Error reading user" + next.id);
	//			}
	//			
	//			next.UpdateList = people;
	//			next.usinUpdateList = true;
	//		}
	//	}

	public synchronized void addUser(SubscriberGME newUser){

		connected.add(newUser);

	}

	public synchronized void removeUser(SubscriberGME User){

		connected.remove(User);
	}

	public void close(){
		// to close players: close individual players on server, delete closed 
		// players from this list (and TCPServer list), then close gameroom (removing it from TCPServer list).
		synchronized(connected){
			for (SubscriberGME next : connected){				// disconnect user, remove from its list, remove from top list (in ServerMAIN())
				next.state = false;
				try {
					next.join(10000);							// waiting 10 seconds at max for the user thread to disconnect
					if (next.isAlive()){
						next.interrupt();
					}
				} catch (InterruptedException e) {
					Printu(e.getMessage());
					e.printStackTrace();
					next.close();						// this may not do anything...
				} finally {
					Printu("Disconnected user");
				}
			}
		}
		// afterwards, take the GameRoom out of the list in main
		this.FinishClose();
	}

	protected abstract void FinishClose();
	protected synchronized void Printu(String outputString){
		ServerMAIN.Print(" Server" + myName + ": " + outputString);
	}

}
