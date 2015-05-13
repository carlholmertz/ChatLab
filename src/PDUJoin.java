import java.io.UnsupportedEncodingException;


public class PDUJoin {

	public static byte [] createPdu(String nickName) throws UnsupportedEncodingException{
		int length = nickName.length();
		
		PDU pdu = new PDU((4+length));
		
		pdu.setByte(0, (byte)OpCodes.JOIN); /*Op*/
		pdu.setByte(1,(byte)length);		/*Length of username*/
		pdu.setByte(2,(byte) 0);			/*Add zero*/
		pdu.setByte(3,(byte) 0);			/*Add zero*/
		
		/*Add the nickname to the pdu*/
		byte[] nickNameInt = nickName.getBytes("UTF-8");
		System.out.println("Nick length: "+nickNameInt.length);
		pdu.setSubrange(4, nickNameInt);
		
		/*Check if it's divisible by four*/
		ChatMain.devideByFour(pdu);
		 
		/*Getting the resulting byte array*/
		byte[] joinArray= pdu.getBytes();
		System.out.println("joinarr: "+joinArray.length);
		
		return joinArray;
		
		
	}
	
}
