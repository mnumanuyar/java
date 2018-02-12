import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class yedek {

	public static void main(String[] args) throws Exception, IOException {
		// TODO Auto-generated method stub
		//System.out.println(args);
		//String username = args[0];
		//String serverurl = args[1];
		//String mode = args[2];
		String sentence;
		String modifiedSentence;
		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket clientSocket = new Socket("127.0.0.1", 80);
		DataOutputStream outToServer =
				new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer =
				new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		sentence = inFromUser.readLine();
		//sentence = "REGISTER yarkin@192.168.1.13:54321";
		System.out.print(sentence);
		
		outToServer.writeBytes(sentence + '\r'+'\n' ); //GET /userlist.txt HTTP/1.1
		//outToServer.writeBytes("POST /userlist.txt HTTP/1.1" +'\r'+'\n' );
		//outToServer.writeBytes("POST REGISTER yarkin@192.168.1.13:54321 HTTP/1.0"+ '\r'+'\n');
		
		//outToServer.writeBytes("Host: debian.server.paul.grozav.info"+ '\r'+'\n');
		//outToServer.writeBytes("Content-Length: 34" + sentence.length() + '\r'+'\n');
		//outToServer.writeBytes("Content-Type: string"+ '\r'+'\n');
		outToServer.writeBytes(""+'\r'+'\n');
		modifiedSentence = inFromServer.readLine();
		System.out.println("FROM SERVER: " + modifiedSentence); 
		System.out.println(  inFromServer.readLine()); 
		System.out.println(  inFromServer.readLine());
		System.out.println(  inFromServer.readLine());
		System.out.println(  inFromServer.readLine());
		clientSocket.close();
//
//		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
//		Socket clientSocket = new Socket("hostname", 80);
//		BufferedWriter outToServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF8"));
//		BufferedReader inFromServer =
//				new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//		sentence = inFromUser.readLine();
//		System.out.print(sentence);
//		outToServer.write(sentence + '\r'+'\n' );
//		outToServer.write('\r'+'\n');//GET /userlist.txt
//		modifiedSentence = inFromServer.readLine();
//		System.out.println("FROM SERVER: " + modifiedSentence); 
//		clientSocket.close();
//		
//		Socket clientSocket;
//		PrintWriter p;
//		DataInputStream r;
//		
//		try {
//			System.out.println("a");
//			
//			clientSocket = new Socket("127.0.0.1", 80);
//			System.out.println("a");
//			p = new PrintWriter(clientSocket.getOutputStream(),true);
////			System.out.println("a");
////			String message = "REGISTER yarkin@192.168.1.13:54321";
////			p.println("Post / HTTP/1.1");
////			p.println("Host: stackoverflow.com");
////			p.println("Content-Length: " + message.length() );
////		    p.println("Content-Type: String");
////		    
////
////		    p.println(message);
////		    p.println("");
////		    p.flush();
//			
//			p.write("GET /userlist.txt");
//			p.flush();
//			System.out.println("a");
//			System.out.println("a ");
//			BufferedReader input =
//		            new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//			System.out.println("a " +input.readLine());
//			clientSocket.close();
//			System.out.println("b ");
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		if (mode.equals("listen"))
//		{
//			PostRequest(name)
//		input stream udp listen
//		while(true){
//			clientlistenet.listen
//		}
//		}else if (mode.equals("send")){
//		while(true){
//		input = System.in.read();
		//if(input.equals("list")){
		// 
//	}elseif(input.equals("list")){
		// 
//		}elseif(input.equals("unicast")){
		// 
//		}elseif(input.equals("multicast")){
		// 
//		}elseif(input.equals("exit")){
		// 
//		}else
		//}
//		}
//		
		//Register username, IP, and port to the server using HTTP POST request
		// while true
		// wait for message
		//print f incoming message
		
		//elseif send
		//read command window
		// if list
		// update userlist
		// print userlist
		// else if unicast name message
		// update userlist 
		// send message
		// if error 
		//print user X not found
		// else 
		//print message is sent to X
		// else if broadcast message
		// update list
		// sent everybody
		// else if multicast [name, name ...] message
		// for (name)
		// 		unicast
		// else if exit
		//  quit 
		
	}

}
