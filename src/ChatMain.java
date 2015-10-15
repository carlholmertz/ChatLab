import java.io.*;
import java.net.*;
import java.util.Scanner;




public class ChatMain{
	static DataOutputStream sOutput;
	static DataInputStream sInput;
	static Socket client;
	
	
	
	public static void main(String[] args) throws NumberFormatException, IOException{
		/*Börjar med att ansluta till namnservern för att ta reda på tillgängliga servers*/
		String availableServers[][] = ConnectToNameserver.connectToNs();
		
		
		/*Här får användaren välja en server av server*/
		String choice;
		Scanner in = new Scanner(System.in);
		System.out.println("Välj servernummer:");
		choice = in.nextLine();
		
		
		/*---Om val av server inte är giltigt---*/
		while(Integer.parseInt(choice) <= 0 || Integer.parseInt(choice) > availableServers.length){
			System.out.println(choice+" är inte en giltlig server, ange nytt servernummer:");
			choice = in.nextLine();
			
		}
		
		// Sparar port och adress som användaren angett
		int port = Integer.parseInt(availableServers[(Integer.parseInt(choice)-1)][1]);
		InetAddress address = InetAddress.getByName(availableServers[(Integer.parseInt(choice)-1)][0]);

		
		try {
			/*---Öppnar en socket för anslutning till servern---*/
			client = new Socket(address, port);
			sInput = new DataInputStream( client.getInputStream() );
			sOutput = new DataOutputStream( client.getOutputStream() );
			
			/*---------------------------------------------------*/
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Vi är inte anslutna! Något gick fel.");
			e.printStackTrace();
		}
		/*---Användaren får ange önskat användarnamn---*/
		
		System.out.println("Skriv användarnamn:");
		String nickName = in.nextLine();
		
		/*---Anvöndarnamnet måste bestå av minst ett tecken---*/
		while(nickName.length() == 0){
			System.out.println("Ditt användarnamn måste bestå av minst ett tecken!");
			System.out.println("Skriv användarnamn:");
			nickName = in.nextLine();
		}
		
		/*-----Försöker ansluta till chatten med joinPDUn-----*/
		byte [] joinPdu = PDUJoin.createPdu(nickName);
		try {
			sOutput.write(joinPdu);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*----------------------------------------------------*/
		
		/*----Startar tråd som lyssnar efter input från tangentbordet----*/
		 Thread thread = new Thread(new ClientSender());
		 thread.start();
		
		
		/*----En loop som ligger och lyssnar efter meddelanden och kallar på funktioner
		 * beroende på OPnummer----*/
		byte op;
		boolean loop = true;
		while(loop == true){
			
			op = sInput.readByte();
		
			
			switch (op) {
            	case OpCodes.NICKS:
            		
            		PDUNicks.read(op);
            		

                    break;
            	case OpCodes.MESSAGE:
            			System.out.println("\nRecieved message: ");
            			PDUMessage.read(op);
            			
            		break;
            	case OpCodes.UJOIN:
        			System.out.println("\nNy klient har anslutit: ");
        			PDUJoin.readUJOIN(op);
        			
        			
        		break;
            	case OpCodes.ULEAVE:
        			System.out.println("\nKlient har lämnat chatten: ");
        			PDUJoin.readULEAVE(op);
        			
        			
        		break;
            	case OpCodes.UCNICK:
        			System.out.println("\nKlient har bytt användarnamn: ");
        			PDUNicks.readUCNICK(op);
        			
        		break;
//            	case OpCodes.QUIT:
//        			System.out.println("\nChatten avslutas. ");
//        			
//        			client.shutdownOutput();
//        			client.shutdownInput();
//        			client.close();
//        			loop = false;
//        			
//        		break;
            	default:
            		System.out.println(op+" Felaktig OP-kod");
            		System.out.println(sInput.available());
            		
            		break;
			}		
		}
	}
	
	/*En funktion som fyller PDUerna med nollor för att de ska vara jämnt delbara
	 * med fyra.*/
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





	
		
		
		
		
		

