import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.w3c.dom.NameList;

public class InstantMessenger {
	public static String[] nameList;
	public static String[] IPPortList;
	public static String serverurl;
	public static void main(String[] args) throws Exception, IOException {
		// TODO Auto-generated method stub
		//System.out.println(args);
		String username = args[0];
		serverurl = args[1];
		String mode = args[2];
		nameList = new String[0]; 
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		UI ( inFromUser, username, serverurl, mode);
	}
	public static void UI (BufferedReader inFromUser,String username,String serverurl,String mode) throws Exception
	{
		if (mode.equals("listen"))
		{

			DatagramSocket serverSocket = new DatagramSocket(0);
			InetAddress IPAddress = InetAddress.getByName("hostname");
			String IPPort = IPAddress.toString().split("/")[1]+":"+ serverSocket.getLocalPort();
			PostRequest(username,IPPort);
			byte[] receiveData = new byte[1024]; 
			byte[] sendData = new byte[1024];
			while(true){
				DatagramPacket receivePacket =
						new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				String sentence = new String(receivePacket.getData());
				System.out.println(sentence);// name will be in the sentence
			}
		}else if (mode.equals("send")){

			while(true){
				String input = inFromUser.readLine();
				String comand = input.split(" ")[0];
				if(comand.equals("list")){
					System.out.println(GetRequest());
				}else if(comand.equals("unicast")){
					String mesage = input.split("\"")[1].substring(1, input.split("\"")[1].length()-1);
					System.out.println(SendMesage(input.split(" ")[1],mesage));

				}else if(comand.equals("broadcast")){
					String mesage = input.split("\"")[1].substring(1, input.split("\"")[1].length()-1);
					SendMesage(mesage);
				}else if(input.equals("multicast")){
					String mesage = input.split("\"")[1].substring(1, input.split("\"")[1].length()-1);
					String names = input.split("\"")[0].split(" ")[1];
					String[] nameArray = names.substring(1, names.length()-1).split(",");
					System.out.println(SendMesage(nameArray,mesage));
				}else if(input.equals("exit")){
					System.exit(0);
				}
			}
		}
	}


	private static String SendMesage(String[] nameArray, String mesage) throws Exception {
		GetRequest();
		String result ="";
		for(String n : nameArray)
		{
			result += SendMesage( n,  mesage) +"\n";
		}
		return result;
	}

	private static void SendMesage(String mesage) throws Exception {
		GetRequest();
		String result = "";
		for(String name:nameList ){
			SendWithUDP(name,mesage);
		}
	}

	private static String SendMesage(String name, String mesage) throws Exception {
		GetRequest();
		String result = "user "+name+"is not found";
		for(String n:nameList ){
			if(n.equals(name)){
				SendWithUDP(name,mesage);
				result = "message is sent to "+ name;
				break;
			}

		}
		return result;
	}

	private static void SendWithUDP(String name, String mesage) throws Exception {
		InetAddress IPAddress; 
		int port;
		for (int i=0; i<nameList.length;i++){
			if(nameList[i].equals(name)){
				IPAddress = InetAddress.getByName(IPPortList[i].split(":")[0]);
				port = Integer.parseInt(IPPortList[i].split(":")[1]);
				
				DatagramSocket clientSocket = new DatagramSocket();
				byte[] sendData = new byte[1024];
				sendData = mesage.getBytes();
				DatagramPacket sendPacket =
						new DatagramPacket(sendData, sendData.length, IPAddress, port);
				clientSocket.send(sendPacket);
				clientSocket.close();
				break;
			}
		}
		
		
		

	}

	private static String findSender(DatagramPacket receivePacket) {
		InetAddress IPAddress = receivePacket.getAddress(); 
		int port = receivePacket.getPort();
		String senderName;
		for (int i=0; i<IPPortList.length;i++){
			if(IPPortList[i].equals(IPAddress+":"+port)){
				senderName = nameList[i];
				return senderName;
			}

		}
		return null;
	}
	private static String GetRequest() throws Exception {
		Socket clientSocket = new Socket(serverurl, 80);
		DataOutputStream outToServer =
				new DataOutputStream(clientSocket.getOutputStream());
		outToServer.writeBytes("GET /userlist.txt HTTP/1.0" + '\r'+'\n' ); //GET /userlist.txt
		//outToServer.writeBytes("POST /userlist.txt" +'\r'+'\n' );
		//outToServer.writeBytes("POST " + sentence + " HTTP/1.0"+ '\r'+'\n');
		
		//outToServer.writeBytes("Host: debian.server.paul.grozav.info"+ '\r'+'\n');
		//outToServer.writeBytes("Content-Length: " + sentence.length() + '\r'+'\n');
		//outToServer.writeBytes("Content-Type: string"+ '\r'+'\n');
		outToServer.writeBytes(""+'\r'+'\n');
		clientSocket.close();
		
		return null;
	}

	private static void PostRequest(String username, String iPPort) {
		// TODO Auto-generated method stub

	}

}
