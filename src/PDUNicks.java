import java.io.DataInputStream;
import java.io.IOException;


public class PDUNicks {
	public static void read(DataInputStream sInput) throws IOException{
		byte[] nickNamesPDU = new byte[sInput.available()];
//		sInput.readFully(nickNames);
		
		sInput.read(nickNamesPDU, 0, nickNamesPDU.length);
		
		PDU pdu = new PDU(nickNamesPDU, nickNamesPDU.length);
		
		int amountOfNames = pdu.getByte(0);
		int totalLength = pdu.getShort(1);
		
		byte[] nickNames = new byte[totalLength];
//		sInput.readFully(nickNames);
		
		nickNames = pdu.getSubrange(3,totalLength);
		
		String name = new String(nickNames,"UTF-8");
		int i;
		
		System.out.println("Antal anslutna på servern: "+amountOfNames);
		
			String[] name1 = name.split("\0");
		for(i=0; i != amountOfNames; i++){
			
			System.out.println("Nick nr"+(i+1)+": "+name1[i]);
		}
		
	}
}
