
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.sun.jmx.snmp.Timestamp;


public class PDUJoin {

	public static byte [] createPdu(String nickName) throws UnsupportedEncodingException{
		
		byte[] nickNameInt = nickName.getBytes("UTF-8");
		PDU pdu = new PDU((4+nickNameInt.length));
		
		pdu.setByte(0, (byte)OpCodes.JOIN); /*Op*/
		pdu.setByte(1,(byte)nickNameInt.length);		/*Length of username*/
		pdu.setByte(2,(byte) 0);			/*Add zero*/
		pdu.setByte(3,(byte) 0);			/*Add zero*/
		
		/*Add the nickname to the pdu*/
	
		pdu.setSubrange(4, nickNameInt);
		
		/*Check if it's divisible by four*/
		ChatMain.devideByFour(pdu);
		 
		/*Getting the resulting byte array*/
		byte[] joinArray= pdu.getBytes();

		
		return joinArray;
		
		
	}
	public static void readUJOIN(byte op) throws IOException{
		/*---Skapar en byte array och läser in allt från input-streamen till den---*/
		byte[] ujoinNPDU = new byte[ChatMain.sInput.available()];
		ChatMain.sInput.read(ujoinNPDU, 0, ujoinNPDU.length);
		
		/*---Bygger upp PDUn från grunden---*/
		PDU pdu = new PDU(ujoinNPDU.length+1);
		pdu.setByte(0,op);
		pdu.setSubrange(1, ujoinNPDU);
		
		int nickLength = pdu.getByte(1);
		
		/*En short med Pad dvs nästa offset är 4*/
		
		/*---Tidsstämpel---*/
		long time = pdu.getInt(4); 
		Timestamp timeString = new Timestamp(time);
		/*-----------------*/
		
		/*---Sparar alla nicknames i en string---*/
		byte[] nickName = new byte[nickLength];
		nickName = pdu.getSubrange(8,nickLength);
		String name = new String(nickName,"UTF-8");
		
		System.out.println(timeString.toString());
		System.out.println(name+" anslöt sig till chatten");
		
	}
	public static void readULEAVE(byte op) throws IOException{
		/*---Skapar en byte array och läser in allt från input-streamen till den---*/
		byte[] ujoinNPDU = new byte[ChatMain.sInput.available()];
		ChatMain.sInput.read(ujoinNPDU, 0, ujoinNPDU.length);
		
		/*---Bygger upp PDUn från grunden---*/
		PDU pdu = new PDU(ujoinNPDU.length+1);
		pdu.setByte(0,op);
		pdu.setSubrange(1, ujoinNPDU);
		
		int nickLength = pdu.getByte(1);
		
		/*En short med Pad dvs nästa offset är 4*/
		
		/*---Tidsstämpel---*/
		long time = pdu.getInt(4); 
		Timestamp timeString = new Timestamp(time);
		/*-----------------*/
		
		/*---Sparar alla nicknames i en string---*/
		byte[] nickName = new byte[nickLength];
		nickName = pdu.getSubrange(8,nickLength);
		String name = new String(nickName,"UTF-8");
		
		System.out.println(timeString.toString());
		System.out.println(name+" Lämnade chatten");
	}
	
}
