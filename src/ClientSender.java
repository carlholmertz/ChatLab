import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;




public class ClientSender implements Runnable{

/*---Tråd som körs i backrunden för att lyssna på input från tangentbordet---*/
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		/*----Loop för att läsa input från tangentbordet----*/
		while(true){
			BufferedReader keyboardReader = new BufferedReader(
			new InputStreamReader(System.in));
			String fromKeyboard = null;
			try {
				fromKeyboard = keyboardReader.readLine();
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("\n Gick inte att läsa från tangentbord!\n");
				e.printStackTrace();
			}
			if(fromKeyboard.length() == 0 ){
				System.out.println("\nDu kan inte skriva tomma meddelanden! \n");
			}
			
			/*---Kollar om användaren försöker göra en operation---*/
			else if(fromKeyboard.startsWith("/nick")){
				String nyttNick[] = fromKeyboard.split(" ");
				if(nyttNick.length != 2){
					System.out.println("\nDu har angett för många parametrar");
					System.out.println("\nByt nick genom att skriva '/nick dittnyanick'");
				}else{
					try {
						/*---Kallar på changeNick för att byta nickname---*/
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
			/*---Om personen skriver fel kommando så skrivs det ut vad man kan göra---*/
			else if(fromKeyboard.startsWith("/")){
				System.out.println("\nDe operationer du kan utföra är:");
				System.out.println("Byta nick: '/nick nyttnick'");
				System.out.println("Avsluta: '/quit' eller '/exit' eller '/avsluta'");
			}
			/*---Annars tolkas inputen som en meddelande---*/
			else{
				byte[] bytesToSend;
				try {
					
					bytesToSend = (fromKeyboard + '\n').getBytes("UTF-8");
					/*---kollar strorleken på meddelandet, 65535 är en begränsning enligt spec---*/
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
