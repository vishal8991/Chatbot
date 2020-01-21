package Connect;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
class BlankBot implements ActionListener{
	JFrame frame;
	JPanel p1, p2, p3;	
	TextArea msgArea;
	TextArea userBlock;	
	JButton send, connect, Disconnect;
	TextField msgField;
	JLabel label1, label2, label3;
	String S1, S2, S3;	
	Socket myClient;
	DataInputStream din;
	DataOutputStream dout;
	BlankBot(){}	
	BlankBot(String str){
		frame = new JFrame(str);
		msgArea = new TextArea(10, 50);
		label1 = new JLabel("Chat Window");
		label1.setBounds(20, 20, 100, 20);
		label1.setForeground(Color.white);
		frame.add(label1);
		msgArea.setBackground(new Color(180, 200, 220, 250));
		msgArea.setBounds(20, 40, 150, 300);
		msgArea.setEditable(false);
		frame.add(msgArea);
		label2 = new JLabel("User Block");
		label2.setBounds(180, 20, 100, 20);
		label2.setForeground(Color.white);			
		frame.add(label2);
		userBlock = new TextArea("0 users are connected now....");
		userBlock.setBounds(180, 40, 280, 250);
		userBlock.setBackground(new Color(215, 180,  139));
		userBlock.setEditable(false);
		frame.add(userBlock);		
		msgField = new TextField("Type your text here........");
		msgField.setBounds(20, 350, 320, 40);
		msgField.setBackground(Color.lightGray);
		frame.add(msgField);
		send = new JButton();
		Icon icon = new ImageIcon("D:\\JAVA\\menuswingp-20200103T063200Z-001\\menu\\images\\indent.gif");
		send.setIcon(icon);
		send.setBounds(350, 350, icon.getIconWidth(), icon.getIconHeight());
		Cursor c = new Cursor(Cursor.HAND_CURSOR);
		send.setCursor(c);
		send.addActionListener(this);
		frame.add(send);		
		connect = new JButton("Connect");
		Disconnect = new JButton("Disconnect");
		connect.setBounds(190, 300, 120, 40);
		connect.addActionListener(this);
		frame.add(connect);
		frame.getContentPane().setBackground(new Color(12, 14, 14, 236));
		Disconnect.setBounds(330, 300, 120, 40);
		Disconnect.addActionListener(this);
		frame.add(Disconnect);
		frame.setResizable(false);
		frame.setIconImage(new ImageIcon("D:\\JAVA\\19 Graphics\\icons\\reddit.png").getImage());
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setVisible(true);
	}	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == send){
			S1 = msgField.getText();
			msgArea.setText("me> "+S1);
			msgField.setText("");	
			try{
				MyNewThread myThread = new MyNewThread(din);
				Thread t = new Thread(myThread);
				t.start();
				msgArea.setText("unknown> "+myThread.rText);
				dout.writeUTF(S1);
				dout.flush();
				//S2 = din.readUTF();
			}	
			catch(Exception E){
				System.out.println("Message cannot be sent!\n"+E);
			}
		}
		if(e.getSource() == connect){
			try{
				Cursor pointer = new Cursor(Cursor.WAIT_CURSOR);
				connect.setCursor(pointer);
			myClient = new Socket("localhost", 10);
			userBlock.setText("1 user joins the chatbot");		
			dout = new DataOutputStream(myClient.getOutputStream());
			din = new DataInputStream(myClient.getInputStream());
			}
			catch(Exception ex){
				Cursor pointer = new Cursor(Cursor.DEFAULT_CURSOR);
				connect.setCursor(pointer);
				userBlock.setText("Connection Problem"+ex);	
			}
		}
		if(e.getSource() == Disconnect){
			System.exit(0);
			userBlock.setText("Now you cannot send & recieve messages...");
		}
	}
	public static void main(String... args){
		new BlankBot("BlankBot");
	}
}
class MyNewThread implements Runnable{
	DataInputStream dis;
	String rText="";
	MyNewThread(DataInputStream dis){
		this.dis = dis;
	}
	public void run(){
		
		do{
			try{
				rText = dis.readUTF();
					
			}
			catch(Exception E){
				System.out.println("reading error!"+E);
			}
		}while(!rText.equalsIgnoreCase("stop"));
	}	
}