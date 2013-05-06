package server;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

@SuppressWarnings({"serial"})
public class ServerGui extends JFrame implements ServerInterface, WindowListener{

	JPanel mainPanel, logPanel, listPanel, listPanelButtons;
	JList<Fileio> userList;
	JList<GameRoom> worldList;
	JScrollPane scroller1, scroller2, scroller3;
	public DefaultListModel<Fileio> userloader = new DefaultListModel<>();
	public DefaultListModel<GameRoom> gameloader = new DefaultListModel<>();
	JButton createWorld, deleteWorld, banUser, kickUser, viewStats, exit;
	JLabel userLabel, worldLabel;
	static ColorPane logArea;
	public static int ix = 0;
	static ServerMAIN Server;

	public static JButton createButton(String text, Container panel, String tooltip, ActionListener listener) {
		JButton button = new JButton(text);
		button.setToolTipText(tooltip);
		button.addActionListener(listener);
		panel.add(button);
		return button;
	}

	public ServerGui() { 

		// #################### Panel Initializations ###################

		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1,2));
		this.setTitle("Server Client");
		this.setSize(685,600);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		listPanel = new JPanel();
		listPanel.setBorder(new LineBorder(null));		// Draws a line around the panel to gave a separation between the two parts

		listPanelButtons = new JPanel();
		listPanelButtons.setLayout(new GridLayout(3, 2, 4, 4));		// 3 rows, 2 columns, withs padding of 4 all the way around the objects shown

		// Somewhat centers the new window because the "this.setRelativeTo(null);" does not always work to do that
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
		int xPos = (dim.width / 2) - (this.getWidth() / 2);
		int yPos = (dim.height / 2) - (this.getHeight() / 2);
		this.setLocation(xPos, yPos);

		//#################### Log Panel stuff ####################

		logArea = new ColorPane();		

		// #################### World List panel stuff####################

		worldLabel = new JLabel("Worlds");
		listPanel.add(worldLabel);

		worldList = new JList<>(gameloader);
		worldList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		worldList.setFixedCellHeight(30);	// Each cell in the list is this high
		worldList.setFixedCellWidth(320);	// Each cell in the list is this wide
		worldList.setVisibleRowCount(6);	// It will show only this many rows at a time

		worldList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				repaint();
			}
		});

		scroller2 = new JScrollPane(worldList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listPanel.add(scroller2);

		//################### Player List panel stuff########################

		userLabel = new JLabel("Users");
		listPanel.add(userLabel);

		userList = new JList<>(userloader);
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.setFixedCellHeight(30);	// Each cell in the list is this high
		userList.setFixedCellWidth(320);	// Each cell in the list is this wide
		userList.setVisibleRowCount(7);		// It will show only this many rows at a time

		userList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				repaint();
			}
		});

		scroller3 = new JScrollPane(userList, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listPanel.add(scroller3);

		//##################################################################

		createWorld = createButton("Create World",    listPanelButtons,   "Create a new World", this);
		deleteWorld = createButton("Delete World",    listPanelButtons,   "Delete the selected World", this);
		kickUser    = createButton("Kick User",       listPanelButtons,   "Kick the selected user",   this);
		banUser     = createButton("Ban User",        listPanelButtons,   "Ban the selected user",    this);
		exit        = createButton("Exit",            listPanelButtons,    null,                      this);

		//#########################################################################

		listPanel.add(listPanelButtons);
		mainPanel.add(new JScrollPane(logArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		mainPanel.add(listPanel);

		this.add(mainPanel);  		// adding everything to the frame
		addWindowListener(this); 	// add a window listener (needed for proper server closing)
		this.setVisible(true);
	}

	public static void main(String[] args) {
		ServerGui theen = new ServerGui();
		Server = null;
		int port = 3865;						// change this to a dynamic/ user choice (later)

		try{
			Server = new ServerMAIN(port, theen, true);
			Server.start();
			System.out.println("##EXIT COMPLETE: HAVE A NICE DAY");
		} catch (NullPointerException npe){
			System.out.println("There is already a server running!");
			theen.dispose();
		}catch (Exception e){
			System.out.println("##EXIT INCOMPLETE: FATAL ERROR::\n" + e.getMessage());
			e.getStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {		//#####################
		if(e.getSource() == createWorld) {
			// Code to create a world here
		} else if(e.getSource() == deleteWorld) {		//#####################
			// Code to delete a world here
		} else if(e.getSource() == kickUser) {			//#####################
			Server.PrintUsers();
			Secretary user = (Secretary) userList.getSelectedValue();
			if (user != null){
				userloader.removeElement(user);
				removeUser(user.id);
				logArea.append(Color.blue, "\nUser Removed from all");
			} else {
				logArea.append(Color.red, "\nNo user selected!");
			}
			Server.PrintUsers();
			// get users name from top
			// removeUser(name)
		} else if(e.getSource() == banUser) {			//#####################
			// Code to ban a user here
			Server.PrintUsers();
			Secretary user = (Secretary) userList.getSelectedValue();
		} else if(e.getSource() == exit) {				//#####################
			dispose();
			Server.CloseServer();
		}

	}


	/**
	 * A method to add users to the list (mitigates risk of multiple threads accessing user list)
	 * @param user the user to add
	 */
	//	public synchronized void addUser(Fileio user){
	//		userloader.addElement(user);
	//		
	//	}

	/**
	 * A method to add games to the list (mitigates risk of multiple threads accessing game list)
	 * @param game the game to add
	 */
	//	public synchronized void addGame(GameRoom game){
	//		gameloader.addElement(game);
	//		
	//	}

	@Override
	public void printGUI(String message) {
		logArea.append("\n" + message);
	}

	public void printGUI(String message, Color thisColor) {
		logArea.append(thisColor, "\n" + message);
	}

	private void removeUser(int name){
		Fileio removingUser = null;
		for (Fileio user : Server.snct){
			if (user.id == name){
				removingUser = user;
				logArea.append(Color.yellow, "\nYAY, found him!");
				break;
			}
		}
		Server.addRemoveUser(removingUser, false);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {
		Server.CloseServer();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
}