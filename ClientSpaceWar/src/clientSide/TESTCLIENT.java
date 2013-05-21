package clientSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class TESTCLIENT {
	static DataOutputStream out;
	static DataInputStream in;
	
	@SuppressWarnings("resource")
	public static void main(String argv[]) throws Exception
    {
		
	   	//first, do any pre-initialization: gui
		// then, create server object and initialize
		//afterwards, do post-initialization: 
		int port = 3865;						// change this to a dynamic port later on
		try{
			Socket server = new Socket("192.168.0.3", port);		// 192.168.0.3 		10.6.53.208	// 75.175.60.225
			out = new DataOutputStream(server.getOutputStream());
			in = new DataInputStream(server.getInputStream());
			
			//********************************************
			// The test Number:
			//
			// test0: login with old user (checks username and password)
			// test1: login with new user (checks username and password, creates new user if no current user by that name) 
			// test2: logged-in user changes his ship (assumed logged-in)
			//
			//********************************************
			test2();		
			//********************************************
			
		} catch (Exception e){
			System.out.println("##EXIT INCOMPLETE: FATAL ERROR::\n" + e.getMessage());
		}
    }

	@SuppressWarnings("unused")
	private static void test0() throws Exception {
		boolean check;
		String str = "JOE|pie|1";	// change name to change behavior (username|password|shipnum)
		out.writeInt(0);
		out.writeUTF(str);
		check = in.readBoolean();
		if (check){
			System.out.println("SUCCESS!!");
		} else {
			System.out.println("ERROR: CHECK STUFF!");
		}
	}
	
	@SuppressWarnings("unused")
	private static void test1() throws Exception {
		boolean check;
		String str = "Jeff|not Pie|73"; 	// change stuff to change behavior (username|password|shipnum)
		out.writeInt(1);
		out.writeUTF(str);
		check = in.readBoolean();
		if (check){
			System.out.println("SUCCESS!!");
		} else {
			System.out.println("ERROR: CHECK STUFF!");
		}
	}
	
	private static void test2() throws Exception {
		boolean check;
		String str = "JO|pie|7"; 	// change stuff to change behavior (username|password|shipnum)
		out.writeInt(2);
		out.writeUTF(str);
		check = in.readBoolean();
		if (check){
			System.out.println("SUCCESS!!");
		} else {
			System.out.println("ERROR: CHECK STUFF!");
		}
	}
}
