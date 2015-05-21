import java.io.DataInputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;

import com.sun.jmx.snmp.Timestamp;


public class PDUMessage {
	public static void read(DataInputStream sInput) throws IOException{
		
		byte[] messagePDU = new byte[sInput.available()];
//		sInput.readFully(nickNames);
		
		sInput.read(messagePDU, 0, messagePDU.length);
		
		PDU pdu = new PDU(messagePDU, messagePDU.length);
		
		int messageType = pdu.getByte(0);
		int nickLength = pdu.getByte(1);
		int checksum = Checksum.calc(pdu.getSubrange(2,1), 1);
		int messageLength = pdu.getShort(3);
		// En short med pad 7 blir nästa offset
		long time = pdu.getInt(7);
		 
		Timestamp timeString = new Timestamp(time);
		
//		byte[] time = new byte[4];
//		 time = pdu.getSubrange(7, 4);
//		 String timeString = new String(time,"UTF-8");
		/*Meddelandet*/
		byte[] message = new byte[messageLength];
		message = pdu.getSubrange(11,messageLength);
		String messageString = new String(message,"UTF-8");
		
		if(nickLength != 0){
			byte[] nickname = new byte[nickLength];
			nickname = pdu.getSubrange((11+messageLength), nickLength);
			String nick = new String(nickname,"UTF-8");
			System.out.println(timeString.toString() +" \nMessage from "+nick+":");
			System.out.println(messageString);
		}else{
			
			System.out.println(timeString.toString() +"\nMessage from Server:");
			System.out.println(messageString);
		}
//	
//		byte messageType = sInput.readByte();
//		byte nickLength = sInput.readByte();
//		byte[] checkSum = new byte[1];
//		checkSum[0] = sInput.readByte();
		

//		short messageLength = sInput.readShort();
//		short pad = sInput.readShort();
//		
//		int time = sInput.readInt();
//		
//		byte[] message = new byte[messageLength];
//		
//		sInput.read(message, 0, messageLength);
		
//		System.out.println(sInput.available());
	}
}
