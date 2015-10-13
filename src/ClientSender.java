import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;




public class ClientSender implements Runnable{

/*---Tr�d som k�rs i backrunden f�r att lyssna p� input fr�n tangentbordet---*/
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		/*----Loop f�r att l�sa input fr�n tangentbordet----*/
		while(true){
			BufferedReader keyboardReader = new BufferedReader(
			new InputStreamReader(System.in));
			String fromKeyboard = null;
			try {
				fromKeyboard = keyboardReader.readLine();
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("\n Gick inte att l�sa fr�n tangentbord!\n");
				e.printStackTrace();
			}
			if(fromKeyboard.length() == 0 ){
				System.out.println("\nDu kan inte skriva tomma meddelanden! \n");
			}
			
			/*---Kollar om anv�ndaren f�rs�ker g�ra en operation---*/
			else if(fromKeyboard.startsWith("/nick")){
				String nyttNick[] = fromKeyboard.split(" ");
				if(nyttNick.length != 2){
					System.out.println("\nDu har angett f�r m�nga parametrar");
					System.out.println("\nByt nick genom att skriva '/nick dittnyanick'");
				}else{
					try {
						/*---Kallar p� changeNick f�r att byta nickname---*/
						PDUNicks.changeNICK(nyttNick[1]);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			/*---Om personen vill avsluta---*/
			else if(fromKeyboard.startsWith("/avsluta") || fromKeyboard.startsWith("/exit")|| fromKeyboard.startsWith("/quit")){
				System.out.println("\nDu har valt att avsluta.");
				byte[] quit = {OpCodes.QUIT, 0, 0, 0};
				try {
					/*---Skickar QUIT-meddelandet och avslutar loopen---*/
					ChatMain.sOutput.write(quit);
					
					//Avslutar programmet.
					System.exit(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/*---Om personen skriver fel kommando s� skrivs det ut vad man kan g�ra---*/
			else if(fromKeyboard.startsWith("/")){
				System.out.println("\nDe operationer du kan utf�ra �r:");
				System.out.println("Byta nick: '/nick nyttnick'");
				System.out.println("Avsluta: '/quit' eller '/exit' eller '/avsluta'");
			}
			/*---Annars tolkas inputen som en meddelande---*/
			else{
				byte[] bytesToSend;
				try {
					
					bytesToSend = (fromKeyboard + '\n').getBytes("UTF-8");
					/*---kollar strorleken p� meddelandet, 65535 �r en begr�nsning enligt spec---*/
					if(bytesToSend.length < 65536){ 
						PDUMessage.messageToSend(bytesToSend);
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
			
			
		}
		
	}

}
