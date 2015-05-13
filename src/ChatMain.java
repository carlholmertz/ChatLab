import java.io.*;
import java.net.*;
import java.util.Scanner;




public class ChatMain{

	public static void main(String[] args) throws UnknownHostException, SocketException, UnsupportedEncodingException {
		String availableServers[][] = ConnectToNameserver.connectToNs();
		System.out.println(availableServers.length);
		
		/*Val av server*/
		int choice;
		Scanner in = new Scanner(System.in);
		System.out.println("VŠlj servernummer:");
		choice = in.nextInt();
		
		while(choice <= 0 || choice > availableServers.length){
			System.out.println(choice+" Šr inte en giltlig server, ange nytt servernummer:");
			choice = in.nextInt();
		}
		
		
		
		
		
	}

}
