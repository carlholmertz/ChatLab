import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;


public class ConnectToNameserver {
	  public static String servers[][];

	public static String[][] connectToNs() throws UnknownHostException, SocketException, UnsupportedEncodingException {
		
		String host;
		int port;
		
		/*Ber om adress och port från användaren*/
	    Scanner in = new Scanner(System.in);
		System.out.println("Skiriv namnserverns namn:");
		host = in.nextLine();
	    
	    System.out.println("Skiriv portnummer:");
		port = in.nextInt();
	
		System.out.println("Du skrev in namservern "+host+" på port:"+port);
		
		DatagramSocket client = sendPacket(port,host);
		byte[] receivedData = receivePacket(client);
		
		/*Skapar en ny PDU för att kunna läsa av paketet från namnservern*/
		pduReader(receivedData);
		
		client.close();
		
		return servers;
			
		
	}
	private static DatagramSocket sendPacket(int port, String host) throws UnknownHostException, SocketException{
		/*Skickar GetList kommandot till namnservern för att få reda på 
		 * servrar som är tillgängliga.*/
		
		InetAddress address = InetAddress.getByName(host);
		DatagramSocket client = new DatagramSocket();
		
		byte[] getList = {OpCodes.GETLIST, 0, 0, 0};
		
		DatagramPacket sendPacket =
				new DatagramPacket(getList, getList.length, address, port);
		try {
			client.send(sendPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return client;
	}
	private static byte[] receivePacket(DatagramSocket client){
		byte[] receivedData = new byte[100];
		

		
		DatagramPacket receivedPacket =
				new DatagramPacket(receivedData, receivedData.length);
		try {
			client.receive(receivedPacket);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		System.out.println("Server received " +
				receivedPacket.getLength() + " bytes");
		return receivedData;	
		
	}
	private static void pduReader(byte[] receivedData) throws UnknownHostException, UnsupportedEncodingException{
		
		
		
		/*Skapar en ny PDU för att kunna läsa av paketet från namnservern*/
		PDU availableServers = new PDU(receivedData, receivedData.length);
		int amountOfServers = availableServers.getShort(2);
		servers = new String[amountOfServers][2];
		
		/*Skriver ut headern som pdun från namnservern inehåller.*/

		System.out.println("Antal servers tillgängliga "+amountOfServers);
		
		/*Loopar igenom alla tillgängliga servrar och skriver ut dessa för att användaren 
		 * ska kunna välja en av dessa*/
		int y = 4;
		for(int i = 0; i != amountOfServers; i++){
			
			System.out.println("\n"+"Server nr: "+ (i+1));
			System.out.println("------------------------------");
			
			/*Ip-adressen, castar om long till String, sedan tar vi reda på host namnet
			 * utifrån ip-adressen*/
			long addressLong = availableServers.getInt(y);
			String address = Long.toString(addressLong);
			InetAddress ipAddress = InetAddress.getByName(address);
			String hostName = ipAddress.getHostName();
		    
			y += 4;
			int portNumber = availableServers.getShort(y);
			y += 2;
			int amountClients = availableServers.getByte(y);
			y += 1;
			int nameLength = availableServers.getByte(y);
			y +=1;
			/*Servernamn hämtas som en byte array för att kunna skrivas till en sträng*/
			byte[] nameByte = availableServers.getSubrange(y, nameLength);
			y += 4;
			String name = new String(nameByte,"UTF-8");
			
			System.out.println("Adress: "+hostName);
			System.out.println("Port: "+portNumber);
			System.out.println("Klienter: "+amountClients);
			System.out.println("Namn längd: "+nameLength);
			System.out.println("Namn: "+name);
			System.out.println("------------------------------");

			
			servers[i][0] = hostName;
			servers[i][1] = Integer.toString(portNumber);
			
		}
		
			
	}
}
