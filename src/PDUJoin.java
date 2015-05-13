import java.io.UnsupportedEncodingException;


public class PDUJoin {

	public static void createPdu(String nickName) throws UnsupportedEncodingException{
		int length = nickName.length();
		
		PDU pdu = new PDU((4+length));
		
		pdu.setByte(0, (byte)OpCodes.JOIN);
		pdu.setByte(1,(byte)length);
		pdu.setByte(2,(byte) 0);
		pdu.setByte(3,(byte) 0);
		
		byte[] nickNameInt = nickName.getBytes("UTF-8");
		System.out.println("Nick length: "+nickNameInt.length);
		pdu.setSubrange(4, nickNameInt);
		ChatMain.devideByFour(pdu);
		 
		
		
		
	}
	
}
