package server;

import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import server.ServerMAIN;

/**
 * This class is streamlined to deal with user information sending and receiving for 
 * in-game processes. It is created and assigned to a game by a secretary object/thread.
 * @author Boys
 *
 */
class Player extends SubscriberGME {
   boolean state;
   
   // may need to change this (also look at secretary class)
   public Player(Fileio secretary, PublisherGME Server){
		   // basically, initialize stuff and check to see if this is a old user
	   this.clientSocket = secretary.clientSocket;
	   stats = secretary.stats;
	   toUpdate = Collections.synchronizedList(new ArrayList<String>());
	   toUpdate.add(0, "JA|asdf|4");
	   contUp = false;
	   state = true;
	   myServer = Server;
	   isPlayer = true;
	   id = ServerMAIN.UsrNum++;
	   out = secretary.out;
	   in = secretary.in;
	   this.mif = secretary.mif;
	   try{
		   out.writeBoolean(true);
		   boolean check = in.readBoolean();
		   if (check){
			   Printu("User has connected sucessfully", Color.green);
		   }
	   } catch (IOException e){
		   Printu("Error checking connection", Color.red);
		   try {
			   out.close();
		   } catch (IOException e1) {
			   Printu("^^Could not close output stream" + e1.getMessage(), Color.red);
		   }
		   try {
			   in.close();
		   } catch (IOException e1) {
			   Printu("^^Could not close input stream" + e1.getMessage(), Color.red);
		}
	   }
   }
   
   public void run(){
	   // to do: this method takes a user, and runs the user input and output, sending input to the user and updating the user with information
	   try {
	   while(state){
		   System.out.println("{}{}" + toUpdate.get(0));
    	// read player sent info, send new updated info
		   updateUpdateList();
		   updatePos();
		   if (!state)
			   break;
	   }
	   } catch (InterruptedException ie){
		   Printu("Closing...");
	   }
	   close();
   }
   
   protected void close() {
	   	writeFile(ServerMAIN.resources + ServerMAIN.slash + mif.myName, mif.Encode()); // probably needs to be user stats instead (or both?)
		ServerMAIN.addRemoveUser(this, false);
		myServer.connected.remove(this);
	   try {							// try to close the connection, piece by piece
		   if(out != null) 
			   out.close();
	   } catch (IOException e) {
		   Printu("Could not close output stream", Color.red);
	   }

	   try {
		   if(in != null) 
			   in.close();
	   } catch (IOException e) {
		   Printu("Could not close input stream", Color.red);
	   }

	   try {
		   if(clientSocket != null) 
			   clientSocket.close();
	   } catch (IOException e) {
		   Printu("Could not close socket", Color.red);
	   }
  }
	   
}