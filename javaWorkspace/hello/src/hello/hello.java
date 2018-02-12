package hello;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class hello {

	public static void main(String[] args) throws SocketException, UnknownHostException {
		// TODO Auto-generated method stub
		int port = 9876;
		DatagramSocket serverSocket = new DatagramSocket(port);
		System.out.println(serverSocket.getLocalPort());
		System.out.println(serverSocket.getInetAddress());
		System.out.println(serverSocket.getLocalSocketAddress());
		System.out.println(serverSocket.getRemoteSocketAddress());
		InetAddress IPAddress = InetAddress.getByName("hostname");
		System.out.println("REGISTER yarkin@192.168.1.13:54321".length());
		System.out.println(IPAddress.toString().split("/")[1]+":"+ port);
		serverSocket.close();
	}

}
