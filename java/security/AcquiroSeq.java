package security;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AcquiroSeq
{

    private static byte[] smartkey = {
            0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
    };
    
    public static String MakeSmart(String NonSmartData)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final SecretKeySpec ezswypesmartkey = new SecretKeySpec(smartkey, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, ezswypesmartkey);
            final String SmartData = Base64.encodeBase64String(cipher.doFinal(NonSmartData.getBytes()));
            return SmartData;
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }
        return null;

    }

    public static String MakeNonSmart(String SmartData){
        try{
        	//System.out.println("SmartData:"+SmartData);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            final SecretKeySpec ezswypesmartkey = new SecretKeySpec(smartkey, "AES");
            cipher.init(Cipher.DECRYPT_MODE, ezswypesmartkey);
            final String NonSmartData = new String(cipher.doFinal(Base64.decodeBase64(SmartData)));
            //System.out.println("NonSmartData::::::"+NonSmartData);
            return NonSmartData;
        }catch (Exception e){
        	System.out.println("error to make non smart "+e);
         // e.printStackTrace();
          return "0";
        }
    }

    	public static void main(String args[]){
    	System.out.println("1111111111");
        //final String strToEncrypt = "{txnId=71, invoiceId=71, orgName=Ezswype India, orgAddress=JP Towers,New Railway Road,Gurgaon,Haryana,INDIA,122006, tid=123456789, mid=123456789, rmn=9899937171, emailId=codegyani@gmail.com, dateTime=2015-12-21 13:10:46.0}";
        //final String strToEncrypt = "{sessionId=[77], firstLogin=[1]}";
    	final String strToEncrypt = "Acquiro123";
    	final String encryptedStr = AcquiroSeq.MakeSmart(strToEncrypt.trim());
        System.out.println("Encrypted : " + encryptedStr);
        final String decryptedStr = AcquiroSeq.MakeNonSmart("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");
        System.out.println("Decrypted : " + decryptedStr);

    }     
}