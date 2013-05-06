package server;

import java.awt.Color;
import java.io.*;
import java.net.SocketException;
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
		toUpdate.add(0, "JA|34.56|44.55|54.32|1");
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
				System.out.println("User has connected sucessfully");
			}
		} catch (IOException e){
			System.out.println("Error checking connection");
			close();
		}


	}

	public void run(){}

	public void asdf() throws SocketException{

		// extra testing stuff
		try{
			String test = "testtesttest"; 
			out.writeUTF(test);
			System.out.println(test);
			System.out.println("Test with user went Successfully");

		} catch (IOException IOE){
			System.out.println("Error with output to user in TEST");
		} finally{
			try{
				System.out.println("PAUSED");
				Thread.sleep(700);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		// end of extra testing stuff


		// to do: this method takes a user, and runs the user input and output, sending input to the user and updating the user with information
		try {
			while(state){
				System.out.println("{}{}" + toUpdate.get(0));
				// read player sent info, send new updated info
				updateUpdateList();
				updatePos();
				if (!state)
					break;
				//				try {
				//					Thread.sleep(100);
				//				} catch (InterruptedException e1) {
				//					e1.printStackTrace();
				//				}
			}
		} catch (InterruptedException | SocketException soc){
			Printu("Closing...");
		}
		close();
	}

	
	
	protected void close() {
		String encoding = null;
		try{
			encoding = in.readUTF();
		}catch (IOException ioe){
			Printu("Could not read from user: " + ioe.getMessage());
			encoding = "0|0|0";
		}
		stats.Decode(encoding);
		writeFile(ServerMAIN.resources + ServerMAIN.slash + mif.myName + ServerMAIN.slash + ServerMAIN.stats + ServerMAIN.ext, stats.Encode()); // probably needs to be user stats instead (or both?)
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