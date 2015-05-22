
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.sun.jmx.snmp.Timestamp;

public class PDUNicks {
	public static void read(byte op) throws IOException{
		
		/*---Skapar en byte array och läser in allt från input-streamen till den---*/
		byte[] nickNamesPDU = new byte[ChatMain.sInput.available()];
		ChatMain.sInput.read(nickNamesPDU, 0, nickNamesPDU.length);
		
		/*---Bygger upp PDUn från grunden---*/
		PDU pdu = new PDU(nickNamesPDU.length+1);
		pdu.setByte(0,op);
		pdu.setSubrange(1, nickNamesPDU);
		
		int amountOfNames = pdu.getByte(1);
		int totalLength = pdu.getShort(2);
		
		/*---Sparar alla nicknames i en string---*/
		byte[] nickNames = new byte[totalLength];
		nickNames = pdu.getSubrange(4,totalLength);
		String name = new String(nickNames,"UTF-8");
		
		System.out.println("Antal anslutna på servern: "+amountOfNames);
		
		/*---Splittar namnsträngen på \0 för att få ut namnen var försig---*/
		String[] name1 = name.split("\0");
		for(int i=0; i != amountOfNames; i++){
			
			System.out.println("Nick nr"+(i+1)+": "+name1[i]);
		}
		
	}
	public static void readUCNICK(byte op) throws IOException{
		
		/*---Skapar en byte array och läser in allt från input-streamen till den---*/
		byte[] nickNamesPDU = new byte[ChatMain.sInput.available()];
		ChatMain.sInput.read(nickNamesPDU, 0, nickNamesPDU.length);
		
		/*---Bygger upp PDUn från grunden---*/
		PDU pdu = new PDU(nickNamesPDU.length+1);
		pdu.setByte(0,op);
		pdu.setSubrange(1, nickNamesPDU);
		
		int oldNickLength = pdu.getByte(1);
		int newNickLength = pdu.getByte(2);
		
		/*---En byte med pad nästa offset blir 4---*/
		long time = pdu.getInt(4);
		Timestamp timeString = new Timestamp(time);
		
		byte[] oldNickName = new byte[oldNickLength];
		oldNickName = pdu.getSubrange(8,oldNickLength);
		String oldName = new String(oldNickName,"UTF-8");
		
		byte[] newNickName = new byte[newNickLength];
		newNickName = pdu.getSubrange(8+oldNickLength,newNickLength);
		String newName = new String(newNickName,"UTF-8");
		
		System.out.println(timeString.toString());
		System.out.println(oldName+" bytte användarnamn till: "+newName);
		
	}
	public static void changeNICK(String newNick) throws UnsupportedEncodingException{
		int length = newNick.length();
		
		/*----Skapar en pdu och fyller den med information----*/
		PDU pdu = new PDU(4+length);
		pdu.setByte(0,(byte)OpCodes.CHNICK);
		pdu.setByte(1,(byte)length);
		pdu.setByte(2,(byte)0);//Pad
		pdu.setByte(3,(byte)0);//Pad
		
		byte[] nickName = newNick.getBytes("UTF-8");
		
		pdu.setSubrange(4,nickName);
		/*---Kontrollerar att PDUn är delbar med 4---*/
		ChatMain.devideByFour(pdu);
		
		try{
			/*---Skickar till servern---*/
			ChatMain.sOutput.write(pdu.getBytes());
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
