package clientSide;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

@SuppressWarnings("serial")
public class LoginForm extends JDialog implements ServerTALK{

	public DataOutputStream out = null;
	public DataInputStream in = null;
	public Socket mySocket = null;

	// Login menu
	private JTextField usernameInput;
	private JLabel uName, pWord;
	private JPasswordField passwordInput;
	private JButton loginButton;

	// Create menu
	private JTextField usernameInput2;
	private JLabel uName2, pWord1, pWord2;
	private JPasswordField passwordInput1, passwordInput2;
	private JButton createInput;

	JPanel mainPanel, loginPanel, createPanel, buttonPanel;
	private JButton exit;

	boolean accept = false;
	String fromServer = null;
	String ship = "spaceship1.gif";


	public LoginForm() {

		this.setTitle("Sign-In or Create");
		this.setResizable(false);

		// Sets it for two side-by-side panes
		mainPanel = new JPanel(new GridLayout(1, 2));

		// Finds the middle of the screen so the window will start in the center of the window
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
		int xPos = (dim.width / 2) - (this.getWidth() / 2);
		int yPos = (dim.height / 2) - (this.getHeight() / 2);
		this.setLocation(xPos, yPos);


		//###################### Login Panel ######################
		loginPanel = new JPanel(new FlowLayout());
		loginPanel.setBorder(new LineBorder(null));

		uName = new JLabel(" Username: ");
		loginPanel.add(uName);

		usernameInput = new JTextField(10);
		usernameInput.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					login();
				}}});
		loginPanel.add(usernameInput);
		usernameInput.requestFocus();

		pWord = new JLabel(" Password: ");
		loginPanel.add(pWord);

		passwordInput = new JPasswordField(10);
		passwordInput.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					login();
				}}});
		loginPanel.add(passwordInput);

		loginButton = new JButton("Login");
		loginButton.addActionListener(new LoginAction());
		loginPanel.add(loginButton);

		exit = new JButton("Exit");
		exit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ExitButton(evt);
			}
		});
		loginPanel.add(exit);


		//###################### Create Panel ######################
		// Creating the grid for the second
		createPanel = new JPanel( new FlowLayout());
		createPanel.setBorder(new LineBorder(null));
		buttonPanel = new JPanel(new GridLayout(1,2, 5, 5));

		uName2 = new JLabel(" Username: ");
		createPanel.add(uName2);

		usernameInput2 = new JTextField(10);
		usernameInput2.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					create();
				}}});
		createPanel.add(usernameInput2);

		pWord1 = new JLabel(" Password: ");
		createPanel.add(pWord1);

		passwordInput1 = new JPasswordField(10);
		passwordInput1.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					create();
				}}});
		createPanel.add(passwordInput1);

		pWord2 = new JLabel(" Re-enter Password: ");
		createPanel.add(pWord2);

		passwordInput2 = new JPasswordField(10);
		passwordInput2.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					create();
				}}});
		createPanel.add(passwordInput2);

		createInput = new JButton("Create Profile");
		createInput.addActionListener(new CreateButton());
		buttonPanel.add(createInput);

		exit = new JButton("Exit");
		exit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ExitButton(evt);
			}
		});
		buttonPanel.add(exit);

		createPanel.add(buttonPanel);


		mainPanel.add(loginPanel);
		mainPanel.add(createPanel);
		this.add(mainPanel);
	}

	public void login() {
		String passWd = new String (passwordInput.getPassword());
		String uName = (usernameInput.getText().length()>0?usernameInput.getText():"You have not entered any!");
		String[] parsed;

		// Used to show all current users, needs to be updated to where the arrayList is though
		//        for(User user : users) {
		//        	  System.out.println(user.getUsername() + " " + user.getPass() + "");
		//        }

		accept = false;
		//System.out.println(uName + ":: " + passWd + "");
		fromServer = SendInfo(uName + "|" + passWd + "|" + String.valueOf(1), 0);

		if (fromServer != null){
			parsed = fromServer.split("\\|");
			Main.user = new User(parsed[0], parsed[1], 0, 0, "spaceship" + parsed[2] + ".gif");
			Main.user.ups.setUsername(uName);
			Main.user.ups.setPass(passWd);
			Main.user.setShipName("spaceship" + parsed[2] + ".gif");
			Menu.accepted = true;
			MultiplayerGame.shipName = Main.user.getShipName();
			//Main.user.setShipName(Menu.Pship);
			Menu.shown = true;
			//System.out.println("spaceship" + parsed[2] + ".gif");
			//SpaceGame.shipName1 = "spaceship" + parsed[2] + ".gif";
			//System.out.println("Current player ship: " + SpaceGame.shipName1);
			accept = true;
			
			dispose();
		} else {
			String error = "The username or password you have entered sucked.  \n" +
					"If you are a new user please create a new account, otherwise  \n" +
					"please enter the correct login name and password and click sign in.";
			JOptionPane.showMessageDialog(null, error, "Password Invalid", JOptionPane.ERROR_MESSAGE);
			usernameInput.setText("");
			usernameInput.requestFocus();
			passwordInput.setText("");
		}
	}

	public void create() {
		String firstPass = new String(passwordInput1.getPassword());  
		String secondPass = new String(passwordInput2.getPassword()); 
		String userName = usernameInput2.getText();

		Boolean uNameTaken = false, pWordsWrong = false, empty = false;

		if(usernameInput2.getText().length()>0 && firstPass.equals(secondPass) && firstPass.length() > 0) {
			System.out.println(userName + " " + firstPass + "");
			//			Main.users.add(new User(userName, firstPass, 0, 0, ship));
			//			Main.user.getUsername() = userName;
			fromServer = SendInfo(userName + "|" + firstPass + "|" + String.valueOf(1), 1);
			if (fromServer == null) {
				uNameTaken = true;
			} else {
				accept = true;
				Main.user = new User(userName, firstPass, 0, 0, ship);
				Main.user.ups.setUsername(userName);
				Main.user.ups.setPass(firstPass);
				Main.user.setShipName("spaceship1.gif");
				Menu.accepted = true;
				Menu.shown = true;
				dispose();
			}
		} else {
			Menu.accepted = false;
			accept = false;
			if(usernameInput2.getText().length() <= 0 || firstPass.length() <= 0 || secondPass.length() <= 0){
				empty = true;
			}
			if(!firstPass.equals(secondPass)) {
				pWordsWrong = true;
			}
		}

		if (!accept){
			if(empty) {
				Menu.accepted = false;
				String error3 = "None of the fields may be left blank";
				JOptionPane.showMessageDialog(null, error3 + "", "Invalid", JOptionPane.ERROR_MESSAGE);  
				usernameInput2.setText("");
				usernameInput2.requestFocus();
				passwordInput1.setText("");
				passwordInput2.setText("");
			} else if(pWordsWrong) {
				Menu.accepted = false;
				String error3 = "Passwords must match";
				JOptionPane.showMessageDialog(null, error3 + "", "Password Invalid", JOptionPane.ERROR_MESSAGE);  
				passwordInput1.setText("");
				passwordInput1.requestFocus();
				passwordInput2.setText("");
			} else if(uNameTaken) {
				System.out.printf(userName + " " + firstPass + " " + secondPass);
				Menu.accepted = false;
				String error2 = "Username is invalid, Choose a different one";
				JOptionPane.showMessageDialog(null, error2 + "", "Username Invalid", JOptionPane.ERROR_MESSAGE);
				usernameInput2.setText("");
				usernameInput2.requestFocus();
				passwordInput1.setText("");
				passwordInput2.setText("");
			}
		}

		// ************************************************************************
		// ************************************************************************
		//        for(User user : Game.users) {
		//        	  System.out.println(user.getUsername() + " " + user.getPass() + "");
		//        }
	}

	class LoginAction implements ActionListener{
		public void actionPerformed(ActionEvent e){
			login();
		}
	}

	class CreateButton implements ActionListener{
		public void actionPerformed(ActionEvent e){
			create();
		}
	}

	private void ExitButton(java.awt.event.ActionEvent evt) {
		this.dispose();
		Menu.exit();
	}

	@Override
	public String SendInfo(String str, int num) {
		boolean checkval = false;
		boolean setup = SetUpConnection();
		if (setup){
			try{
				out.writeInt(num);
				out.writeUTF(str);
				checkval = in.readBoolean();
				if (checkval){
					return in.readUTF();
				}
				CloseConnection();
			} catch(Exception e){
				System.out.println("Cannot set up connection");
				// Also, error box here
			}
		}
		return null;
	}

	@Override
	public boolean SetUpConnection() {
		try{
			mySocket = new Socket(Main.ip, Main.port);		// 192.168.0.3 			// 75.175.60.225
			out = new DataOutputStream(mySocket.getOutputStream());
			in = new DataInputStream(mySocket.getInputStream());
		} catch (IOException ioe){
			System.out.println("ERROR IN SetUpConnection()");
			// set up pop-ups later
			CloseConnection();
			return false;
		}
		return true;
	}

	@Override
	public void gameTalk() {
	}

	@Override
	public boolean CloseConnection() {
		try {
			out.close();
		} catch (IOException e1) {
			System.out.println("^^User could not close output stream" + e1.getMessage());
		}
		try {
			in.close();
		} catch (IOException e1) {
			System.out.println("^^User could not close input stream" + e1.getMessage());
		}
		try {
			mySocket.close();
		} catch (IOException e1) {
			System.out.println("^^User could not close Socket" + e1.getMessage());
		}
		return true;

	}

	@Override
	public String RecieveInfo() {
		return null;
	}
}