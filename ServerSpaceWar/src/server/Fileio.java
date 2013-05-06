package server;

import java.awt.Color;
import java.io.*;
import java.net.Socket;

abstract class Fileio extends Thread {
	
	public int id;
	protected Socket clientSocket;				// the socket for the client
	protected DataInputStream in;				// the in/out connections to the client
	protected DataOutputStream out;
	protected UInfo mif;				// user preferences are saved here
	public MyStats stats;				// user stats are saved here (kdr)
	public boolean isPlayer;
	protected String fileP;

	abstract public void run();

	abstract protected void close();
	//		TCPServer.remove(this);
	//	   try {							// try to close the connection, piece by piece
	//		   if(out != null) 
	//			   out.close();
	//	   } catch (IOException e) {
	//		   Printu("Could not close output stream");
	//	   }
	//	
	//	   try {
	//		   if(in != null) 
	//			   in.close();
	//	   } catch (IOException e) {
	//		   Printu("Could not close input stream");
	//	   }
	//	
	//	   try {
	//		   if(clientSocket != null) 
	//			   clientSocket.close();
	//	   } catch (IOException e) {
	//		   Printu("Could not close socket");
	//	   }
	//   }

	/**
	 * Open and reads the information contained in a user file. If the file is not there, does nothing.
	 * Yes, a lot of return statements (tried to mitigate risk of returning a false positive) or not encountering a return statement
	 * @param FilePath
	 * @param id
	 * @return
	 */
	
	protected synchronized String readFile(String FilePath){
		BufferedReader br = null;
		//String[] toReturn = new String[10];
		StringBuffer toReturn = new StringBuffer();
		String check = null;
		int index = 0;
		int isfile = ServerMAIN.findFile(FilePath);
		if (isfile == 0){
			Printu("Error: file is not there", Color.red);
			return null;
		} else if (isfile == 1){
			Printu("KFile located: reading..");
			/*
			 * Connect to file, throw errors if fail
			 */
			try{
				br = new BufferedReader(new FileReader(FilePath));
			} catch (IOException ioe){							// kinda wierd if it gets here
				Printu("ERROR: CANT OPEN FILE", Color.red);
				System.out.println(ioe.getStackTrace());
				return null;
			}
			try{
				//for(index = 0; (toReturn[index] = br.readLine()) != null; index++);
				toReturn.append(br.readLine());
				for(index = 0; (check = br.readLine()) != null; index++){
					toReturn.append(ServerMAIN.token + check);
				}
				//mif.myName = br.readLine();
				//mif.myPassword = br.readLine();
				//mif.shipNum = (int) Integer.parseInt(br.readLine());
			} catch (IOException ioe) {
				Printu("ERROR: CANT READ FROM FILE", Color.red);
				try {
					if (br != null)
						br.close();
				} catch (IOException e) {
					Printu("--Could not close BufferedReader" + e.getMessage(), Color.red);
					e.printStackTrace();
				}
				return null;
			}
		} else if (isfile == 2){
			Printu("User" + id + " gave a directory, not a file!", Color.red);
			return null;
		} else {
			Printu("ERROR: NAME IS NOT A FILE OR DIRECTORY", Color.red);
			return null;
		}
		return toReturn.toString();
	}

	/**
	 * Writes to a file. Should only write to the file if it can be found
	 * @param FilePath
	 * @param toWrite
	 * @return
	 */
	protected boolean writeFile(String FilePath, String toWrite){
		BufferedWriter bw = null;
		int isfile = ServerMAIN.findFile(FilePath);
		//System.out.println("inWriteFile: findFile worked" + isfile);
		if (isfile == 0){
			Printu("File is missing:: " + FilePath);
//			try{
//				new File(FilePath).createNewFile();
//				bw = new BufferedWriter(new FileWriter(new File(FilePath), false));
//			} catch (IOException ioe){							// kinda weird if it gets here
//				ServerMAIN.Print("ERROR: CANT OPEN/CREATE FILE");
//				System.out.println(ioe.getStackTrace());
//			}
//			try{
//				for (String write: toWrite.split("\\|")){
//					bw.write(write);
//					bw.newLine();
//				}
//			} catch (IOException ioe) {
//				Printu("COULD NOT WRITE PROPERLY TO NEW FILE: ABORTING OPERATIONS");
//			} finally {
//				try{
//					bw.close();
//				} catch (IOException ioe){
//					Printu("--COULD NOT CLOSE BUFFEREDWRITER");
//				}
//			}
			return false;
		} else if (isfile == 1){
			Printu("File located: writing.." + FilePath);
			try{
				/*
				 * Connect to file, throw errors if fail
				 */
				try{
					new File(FilePath).createNewFile();
					bw = new BufferedWriter(new FileWriter(new File(FilePath), false));
				} catch (IOException ioe){							// kinda weird if it gets here
					ServerMAIN.Print("ERROR: CANT OPEN FILE");
					System.out.println(ioe.getStackTrace());
					throw new IllegalStateException();
				}
				
				/**
				 * Read from file, throw errors if fail
				 */
				try{
					for (String nextLine: toWrite.split("\\|")){
						//if (write.length() != 0){				/// This should get rid of 
							bw.write(nextLine);
							bw.newLine();
						//} else {
						//	bw.// what????, how to skip a line in a file w/ bufferedreader?
						//}
					}
				} catch (IOException ioe) {
					Printu("COULD NOT WRITE PROPERLY TO NEW FILE: ABORTING OPERATIONS", Color.red);
					throw new IllegalStateException();
				} 
				
			} finally {
				try{
					bw.close();
				} catch (IOException ioe){
					Printu("--COULD NOT CLOSE BUFFEREDWRITER", Color.red);
				}
			}
			
		} else if (isfile == 2){
			Printu("User" + id + " gave a directory, not a file!", Color.red);
			return false;
		} else {
			Printu("ERROR: NAME IS NOT A FILE OR DIRECTORY", Color.red);
			return false;
		}

		return true;
	}

//	protected boolean UpdateUserInfo(String Filepath){
//		String[] values = readFile(Filepath);
//		if (values == null){
//			return false;
//		}
//		int ix = 0;
//		// reading all the values which were in the file in their appropriate fields
//		mif.myName = values[ix++];
//		mif.myPassword = values[ix++];
//		mif.shipNum = Integer.getInteger(values[ix++]);
//		
//		return true;
//	}
	
	protected synchronized void Printu(String outputString){
		ServerMAIN.Print(" " + id + ": " + outputString);
	}

	protected synchronized void Printu(String outputString, Color textColor){
		ServerMAIN.Print(" " + id + ": " + outputString, textColor);
	}
}
