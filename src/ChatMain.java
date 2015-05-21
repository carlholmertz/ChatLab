import java.io.*;
import java.net.*;
import java.util.Scanner;




public class ChatMain{
	static DataOutputStream sOutput;
	static DataInputStream sInput;
	static Socket client;
	
	
	public static void main(String[] args) throws NumberFormatException, IOException{
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
		
		InetAddress address = InetAddress.getByName(availableServers[(Integer.parseInt(choice)-1)][0]);
	
		
		
		try {
			
			client = new Socket(address, port);
			
			
			
			sInput = new DataInputStream( client.getInputStream() );
			sOutput = new DataOutputStream( client.getOutputStream() );
			System.out.println("\nVi är anslutna!");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Vi är inte anslutna!");
			e.printStackTrace();
		}
		
		System.out.println("Skriv användarnamn:");
		String nickName = in.nextLine();
		

		byte [] joinPdu = PDUJoin.createPdu(nickName);
		
		try {
			sOutput.write(joinPdu);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int op;
		while(true){
			
		
			op = sInput.read();
			
			
			switch (op) {
            	case OpCodes.NICKS:
            		
            		PDUNicks.read(sInput);
            		

                    break;
            	case OpCodes.MESSAGE:
            			System.out.println("Recieved message: ");
            			PDUMessage.read(sInput);
            			
            		break;
            	default:
            		System.out.println(op+" Felaktig OP-kod");
            		System.out.println(sInput.available());
            		
            		break;
			}
//		
			
			
			
			
			
		}
		

		
		

		

		
	}
	
	public static void devideByFour(PDU pdu){
		
		
		int modulus = pdu.length() % 4;
		
		
		if (modulus !=0){
			int newLength = (pdu.length()+(4-modulus));
			
			pdu.extendTo(newLength);
			
			int step = (4-modulus);
			int offset;
			for(int i= 0; i != step; i++){
				offset = (pdu.length() -(step-i));
						
				
				pdu.setByte(offset,(byte) 0);
				
			}
		}
	}
}



	
		
		
		
		
		

