
import java.io.IOException;


import com.sun.jmx.snmp.Timestamp;


public class PDUMessage {
	public static void messageToSend(byte[] message) throws IOException{
		
		PDU pdu = new PDU(12+message.length);
		pdu.setByte(0,(byte)OpCodes.MESSAGE); //OP code
		pdu.setByte(1,(byte)0);//Meddelandetyp
		pdu.setByte(2,(byte)0); // Nickl�ngd ska s�ttas till 0 enl spec.
		pdu.setByte(3,(byte)0); // Checksummma s�tts till 0 innan ber�kning
		pdu.setShort(4,(short)message.length);// L�ngd p� meddelandet
		pdu.setByte(6,(byte) 0);//Pad
		pdu.setByte(7,(byte) 0);//Pad
		
	/*---Timestamp fylls med 0or servern fyller i denna sen---*/
		pdu.setByte(8,(byte)0);
		pdu.setByte(9,(byte)0);
		pdu.setByte(10,(byte)0);
		pdu.setByte(11,(byte)0);
		pdu.setSubrange(12, message);
	/*---------------------------------------------------------*/	
		
		
		/*kolla om delbart med 4*/
		ChatMain.devideByFour(pdu);
		
		/*---K�r Checksum ber�kningen och l�gger in resultatet i checksumf�ltet i PDUn---*/
		pdu.setByte(3,Checksum.calc(pdu.getBytes(), pdu.length()));
		
		try{
			
			ChatMain.sOutput.write(pdu.getBytes());
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		
	
	public static void read(byte op) throws IOException{
		
		/*---Skapar en byte array och l�ser in allt fr�n input-streamen till den---*/
		byte[] messagePDU = new byte[ChatMain.sInput.available()];		
		ChatMain.sInput.read(messagePDU, 0, messagePDU.length);
		
		/*---Skapar en PDU av det inkommande paketet---*/
		PDU pdu = new PDU(messagePDU.length+1);
		
		/*Bygger upp PDUn fr�n grunden*/
		pdu.setByte(0,op);
		pdu.setSubrange(1, messagePDU);
		int messageType = pdu.getByte(1);
		int nickLength = pdu.getByte(2);
		
		/*-----Kontroll av checksumman------*/
		int checksum = Checksum.calc(pdu.getBytes(), pdu.length());
		if(checksum != 0){
			System.out.println("\nChecksumman st�mmer inte!");
			System.out.println("\nChecksumman �r:"+checksum);
			return;
			
		/*---Hanterar endast vanliga meddelanden---*/
		}else if(messageType != 0){
			System.out.println("\n Meddelandetyp:"+messageType+": k�nns inte igen");
			return;
		}else{
			int messageLength = pdu.getShort(4);
			
			/*---En short med pad 8 blir d�rf�r n�sta offset---*/
			long time = pdu.getInt(8);
			Timestamp timeString = new Timestamp(time);

			/*---------Meddelandet-----------*/
			byte[] message = new byte[messageLength];
			message = pdu.getSubrange(12,messageLength);
			String messageString = new String(message,"UTF-8");
			/*-------------------------------*/
			
			/*---Kollar om det �r ett meddelande fr�n servern eller en klient---*/
			if(nickLength != 0){
				byte[] nickname = new byte[nickLength];
				
				if(messageLength %4 == 0){
					nickname = pdu.getSubrange((12+messageLength), nickLength);
				}else if(messageLength %4 == 1){
					nickname = pdu.getSubrange((12+messageLength+3), nickLength);
				}
				else if(messageLength %4 == 2){
					nickname = pdu.getSubrange((12+messageLength+2), nickLength);
				}
				else{
					nickname = pdu.getSubrange((12+messageLength+1), nickLength);
				}
				String nick = new String(nickname,"UTF-8");
				System.out.println(timeString.toString() +" \nMessage from "+nick+":");
				System.out.println(messageString);
			}else{
				System.out.println(timeString.toString() +"\nMessage from Server:");
				System.out.println(messageString);
			}
			/*------------------------------------------------------------------*/
		}
	}
}
