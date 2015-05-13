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

		byte [] joinPdu = PDUJoin.createPdu(nickName);
		
		client.getOutputStream().write(joinPdu);
		
		
	}
	
	public static void devideByFour(PDU pdu){
		
		
		int modulus = pdu.length() % 4;
		System.out.println("modulus: "+ modulus);
		System.out.println("pdu.length: "+ pdu.length());
		if (modulus !=0){
			int newLength = (pdu.length()+(4-modulus));
			System.out.println("new length: "+newLength);
			pdu.extendTo(newLength);
			
			int step = (4-modulus);
			int offset;
			for(int i= 0; i != step; i++){
				offset = (pdu.length() -(step-i));
						
				System.out.println(i+" plats: "+offset);
				pdu.setByte(offset,(byte) 0);
				
			}
		}
		
		
		
		
		
	}

}
