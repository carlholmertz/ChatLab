
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.sun.jmx.snmp.Timestamp;

public class PDUNicks {
	public static void read(byte op) throws IOException{
		
		/*---Skapar en byte array och l�ser in allt fr�n input-streamen till den---*/
		byte[] nickNamesPDU = new byte[ChatMain.sInput.available()];
		ChatMain.sInput.read(nickNamesPDU, 0, nickNamesPDU.length);
		
		/*---Bygger upp PDUn fr�n grunden---*/
		PDU pdu = new PDU(nickNamesPDU.length+1);
		pdu.setByte(0,op);
		pdu.setSubrange(1, nickNamesPDU);
		
		int amountOfNames = pdu.getByte(1);
		int totalLength = pdu.getShort(2);
		
		/*---Sparar alla nicknames i en string---*/
		byte[] nickNames = new byte[totalLength];
		nickNames = pdu.getSubrange(4,totalLength);
		String name = new String(nickNames,"UTF-8");
		
		System.out.println("Antal anslutna p� servern: "+amountOfNames);
		
		/*---Splittar namnstr�ngen p� \0 f�r att f� ut namnen var f�rsig---*/
		String[] name1 = name.split("\0");
		for(int i=0; i != amountOfNames; i++){
			
			System.out.println("Nick nr"+(i+1)+": "+name1[i]);
		}
		
	}
	public static void readUCNICK(byte op) throws IOException{
		
		/*---Skapar en byte array och l�ser in allt fr�n input-streamen till den---*/
		byte[] nickNamesPDU = new byte[ChatMain.sInput.available()];
		System.out.println("Nick L�ngd: "+nickNamesPDU.length);
		ChatMain.sInput.read(nickNamesPDU, 0, nickNamesPDU.length);
		
		/*---Bygger upp PDUn fr�n grunden---*/
		PDU pdu = new PDU(nickNamesPDU.length + 1);
		pdu.setByte(0,op);
		pdu.setSubrange(1, nickNamesPDU);
		
		int oldNickLength = pdu.getByte(1);
		int newNickLength = pdu.getByte(2);
		
		/*---En byte med pad n�sta offset blir 4---*/
		long time = pdu.getInt(4);
		
		// Tar fram en timestamp
		Timestamp timeString = new Timestamp(time);
		
		// Tar fram det gamla anv�ndarnamnet. 
		byte[] oldNickName = new byte[oldNickLength];
		oldNickName = pdu.getSubrange(8,oldNickLength);
		String oldName = new String(oldNickName,"UTF-8");
		
		// Tar fram det nya anv�ndarnamnet
		byte[] newNickName = new byte[newNickLength];
	
		if(oldNickLength%4 != 0){
			// Plus 8 f�r att det �r 8 bytes fram i pdu:n som gamla anv�ndarnamnet b�rjar
			newNickName = pdu.getSubrange((8+oldNickLength+(4-oldNickLength%4)),(newNickLength));
		}
		else{
			// Plus 8 f�r att det �r 8 bytes fram i pdu:n som gamla anv�ndarnamnet b�rjar
			newNickName = pdu.getSubrange((8+oldNickLength),(newNickLength));
		}
		String newName = new String(newNickName,"UTF-8");
		
		System.out.println(timeString.toString());
		System.out.println(oldName+" bytte anv�ndarnamn till: "+newName);
		
	}
	public static void changeNICK(String newNick) throws UnsupportedEncodingException{
		
		byte[] nickName = newNick.getBytes("UTF-8");
		/*----Skapar en pdu och fyller den med information----*/
		PDU pdu = new PDU(4+nickName.length);
		pdu.setByte(0,(byte)OpCodes.CHNICK);
		pdu.setByte(1,(byte)nickName.length);
		pdu.setByte(2,(byte)0);//Pad
		pdu.setByte(3,(byte)0);//Pad
		
		
		
		pdu.setSubrange(4,nickName);
	
		/*---Kontrollerar att PDUn �r delbar med 4---*/
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
