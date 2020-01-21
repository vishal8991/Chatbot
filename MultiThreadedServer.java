package Connect;
import java.io.*;
import java.net.*;
import java.util.*;	//for collection ArrayList class
public class MultiThreadedServer{
	ServerSocket myServer;
	Socket client;
	ArrayList socketPool = new ArrayList();
	public MultiThreadedServer(){
		try{
			myServer = new ServerSocket(10);
			System.out.println("Server Started........");
			while(true){
				client = myServer.accept();
				System.out.println("Client Connected.............");
				socketPool.add(client);
				Runnable r = new MyThread(client, socketPool);
				Thread t = new Thread(r);	
				t.start();
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	public static void main(String[] args){
		new MultiThreadedServer();
	}
}
class MyThread implements Runnable{
	Socket s;
	ArrayList pool;
	MyThread(Socket s, ArrayList pool){
		this.s = s;
		this.pool = pool;
	}	
	public void run(){	
		String str1;
		try{
			DataInputStream din = new DataInputStream(s.getInputStream());
			do{
				str1 = din.readUTF();
				System.out.println(str1);
				if(!str1.equals("stop")){
					tellEveryOne(str1);
				}
				else{
					DataOutputStream dout = new DataOutputStream(s.getOutputStream());
					dout.writeUTF(str1);	
					dout.flush();
					pool.remove(s);
				}
			}while(!str1.equals("stop"));
		}
		catch(Exception e){
			System.out.println(e);	
		}
	}
	public void tellEveryOne(String Str){
		Iterator i = pool.iterator();
		while(i.hasNext()){
			try{
				Socket socket = (Socket) i.next();
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				dos.writeUTF(Str);
				dos.flush();
			}
			catch(Exception e){
				System.out.println(e);
			}
		}
	}	
}
