import java.io.*;
import java.net.*;
import java.util.Scanner;




public class ChatMain{

	public static void main(String[] args) throws NumberFormatException, IOException {
		String availableServers[][] = ConnectToNameserver.connectToNs();
		System.out.println(availableServers.length);
		
		/*Val av server*/
		String choice;
		Scanner in = new Scanner(System.in);
		System.out.println("Välj servernummer:");
		choice = in.nextLine();
		
		while(Integer.parseInt(choice) <= 0 || Integer.parseInt(choice) > availableServers.length){
			System.out.println(choice+" är inte en giltlig server, ange nytt servernummer:");
			choice = in.nextLine();
			
		}
		
		int port = Integer.parseInt(availableServers[(Integer.parseInt(choice)-1)][1]);
		System.out.println(port);
		InetAddress address = InetAddress.getByName(availableServers[(Integer.parseInt(choice)-1)][0]);
		Socket client = new Socket(address, port);
		
		
		System.out.println("Skriv användarnamn:");
		String nickName = in.nextLine();
		System.out.println(nickName);

		PDUJoin.createPdu(nickName);

		
		
	}
	
	public static void devideByFour(PDU pdu){
		
		
		int modulus = pdu.length() % 4;
		System.out.println("modulus: "+ modulus);
		System.out.println("pdu.length: "+ pdu.length());
		int newLength = (pdu.length()+modulus);
		System.out.println("new length: "+newLength);
		pdu.extendTo(newLength);
		System.out.println(pdu.length());
		pdu.setByte(10,(byte) 0);
		
//		byte[] nameByte = pdu.getSubrange(, nameLength);
//		y += 4;
//		String name = new String(nameByte,"UTF-8");
//		int offset;
//		for(int i= 0; i != modulus; i++){
//			offset = (pdu.length() -(modulus-i)+1);
//					
//			System.out.println(i+" längd: "+offset);
//			pdu.setByte(offset,(byte) 0);
//			
//		}
		System.out.println("Längden mother f:er: "+pdu.length());
		
	}

}
