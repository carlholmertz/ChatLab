import java.io.*;
import java.net.*;
import java.util.Scanner;




public class ChatMain{
	static DataOutputStream sOutput;
	static DataInputStream sInput;
	static Socket client;
	
	
	
	public static void main(String[] args) throws NumberFormatException, IOException{
		/*B�rjar med att ansluta till namnservern f�r att ta reda p� tillg�ngliga servers*/
		String availableServers[][] = ConnectToNameserver.connectToNs();
		
		
		/*H�r f�r anv�ndaren v�lja en server av server*/
		String choice;
		Scanner in = new Scanner(System.in);
		System.out.println("V�lj servernummer:");
		choice = in.nextLine();
		
		
		/*---Om val av server inte �r giltigt---*/
		while(Integer.parseInt(choice) <= 0 || Integer.parseInt(choice) > availableServers.length){
			System.out.println(choice+" �r inte en giltlig server, ange nytt servernummer:");
			choice = in.nextLine();
			
		}
		
		// Sparar port och adress som anv�ndaren angett
		int port = Integer.parseInt(availableServers[(Integer.parseInt(choice)-1)][1]);
		InetAddress address = InetAddress.getByName(availableServers[(Integer.parseInt(choice)-1)][0]);

		
		try {
			/*---�ppnar en socket f�r anslutning till servern---*/
			client = new Socket(address, port);
			sInput = new DataInputStream( client.getInputStream() );
			sOutput = new DataOutputStream( client.getOutputStream() );
			
			/*---------------------------------------------------*/
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Vi �r inte anslutna! N�got gick fel.");
			e.printStackTrace();
		}
		/*---Anv�ndaren f�r ange �nskat anv�ndarnamn---*/
		
		System.out.println("Skriv anv�ndarnamn:");
		String nickName = in.nextLine();
		
		/*---Anv�ndarnamnet m�ste best� av minst ett tecken---*/
		while(nickName.length() == 0){
			System.out.println("Ditt anv�ndarnamn m�ste best� av minst ett tecken!");
			System.out.println("Skriv anv�ndarnamn:");
			nickName = in.nextLine();
		}
		
		/*-----F�rs�ker ansluta till chatten med joinPDUn-----*/
		byte [] joinPdu = PDUJoin.createPdu(nickName);
		try {
			sOutput.write(joinPdu);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*----------------------------------------------------*/
		
		/*----Startar tr�d som lyssnar efter input fr�n tangentbordet----*/
		 Thread thread = new Thread(new ClientSender());
		 thread.start();
		
		
		/*----En loop som ligger och lyssnar efter meddelanden och kallar p� funktioner
		 * beroende p� OPnummer----*/
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
        			System.out.println("\nKlient har l�mnat chatten: ");
        			PDUJoin.readULEAVE(op);
        			
        			
        		break;
            	case OpCodes.UCNICK:
        			System.out.println("\nKlient har bytt anv�ndarnamn: ");
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
	
	/*En funktion som fyller PDUerna med nollor f�r att de ska vara j�mnt delbara
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





	
		
		
		
		
		

