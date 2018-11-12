package com.acq.web.dao.impl;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

public class Acq_TransTrac_Port_Connector {

	public static String VISA = "Visa";
    public static String RUPAY = "Rupay";
    public static String MAESTRO = "Maestro";
    public static String MASTER = "MasterCard";
    public static String DINERS = "DINERS";
    public static String JCB = "JCB";
    public static String AMERICAN_EXPRESS = "American Express";
    public static String DISCOVER = "Discover";
    
    
    final static Logger logger = Logger.getLogger(Acq_TransTrac_Port_Connector.class);
    
    public static String portConnectorForKeyExchange(String isoMsg, String UAT, String txnType, int appendZero)
			throws UnknownHostException, IOException{
    	
    	logger.info(txnType + "::" + "Port Connector" + "::" + "Begin");
    	String transtracResponse = "";
    	Socket transtracPort = null;
		try{
			String isoMessage = isoMsg;
			
			if(1==appendZero)
			{
				isoMessage = "00000000000000000000"+isoMsg;   //Prefixing 20 Zeros
			}
			
			int isoLength = isoMessage.length()+2;  //Adding 2 for data length
			String lenInHex = Integer.toHexString(isoLength);
			String hexString = "";	
			
			if(lenInHex.length()==3){
				hexString = "0"+lenInHex;
			}
			else if(lenInHex.length()==2)
			{
				hexString = "00"+lenInHex;
			}
			else
			{
				hexString = lenInHex;
			}

			byte[] bcdArray = stringToBcd(hexString);
			byte[] isoMessageBytes = isoMessage.getBytes();
			byte[] totalMsgBytes = new byte[bcdArray.length+isoMessageBytes.length];
			System.arraycopy(bcdArray, 0, totalMsgBytes, 0, bcdArray.length);  //copying bcdArray (length) to totalMsgBytes
			System.arraycopy(isoMessageBytes, 0, totalMsgBytes, bcdArray.length, isoMessageBytes.length); //appending isoMessageBytes to totalMsgBytes after length
			
			logger.info(txnType + "::" + "Port Connector" + "::" + "isoMessageBytes" + "::" + isoMessageBytes );
						
			if(UAT!=null&&UAT.equals("1")){  //Defining Port and URL to Connect
				transtracPort = new Socket("180.151.52.26", 9888);
			}
			else if(UAT!=null&&UAT.equals("2")){
				transtracPort = new Socket("180.151.52.26", 9887);
			}	
			transtracPort.setSoTimeout (75000);  //connection timeout set to 75 secs.
			
			
    		BufferedOutputStream isoStream = new BufferedOutputStream(transtracPort.getOutputStream());
    		isoStream.write(totalMsgBytes);  //writing ISO bytes to transtrac port
    		isoStream.flush();
			
			byte[] portResponse = new byte[4096];
			int bytesRead = transtracPort.getInputStream().read(portResponse, 0, 4096);
			for (int reponseCount = 0; reponseCount < bytesRead; reponseCount++){
				char response = (char)portResponse[reponseCount];
				transtracResponse = transtracResponse + response;
			}
			
			logger.info(txnType + "::" + "Port Connector" + "::" + "transtracResponse" + "::" + transtracResponse );
			transtracPort.close();
		}
		catch(java.net.SocketTimeoutException e)
		{
			logger.info(txnType + "::" + "Port Connector" + "::" + "Timeout Exception");
			e.printStackTrace();
			return "Connection Timeout";
		}
		catch(Exception e){
			logger.info(txnType + "::" + "Port Connector" + "::" + "Connection Failed");
			return "Connection Failed";
		}
		finally {
			transtracPort.close();  // Close Connection In Case of Exception Also 
		}
		return transtracResponse;
	}
    
    public static byte[] stringToBcd(String data) { 
		int len = data.length(); 
		int mod = len % 2; 
		if (mod != 0) { 
		data = "0" + data; 
		len = data.length(); 
		} 
		byte abt[] = new byte[len]; 
		if (len >= 2) { 
		len = len / 2; 
		} 
		byte bbt[] = new byte[len]; 
		abt = data.getBytes(); 
		int j, k; 
		for (int p = 0; p < data.length() / 2; p++) {
		if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) { 
		j = abt[2 * p] - '0'; 
		} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) { 
		j = abt[2 * p] - 'a' + 0x0a; 
		} else { 
		j = abt[2 * p] - 'A' + 0x0a; 
		} 
		if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) { 
		k = abt[2 * p + 1] - '0'; 
		} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) { 
		k = abt[2 * p + 1] - 'a' + 0x0a; 
		} else { 
		k = abt[2 * p + 1] - 'A' + 0x0a; 
		} 
		int a = (j << 4) + k; 
		byte b = (byte) a; 
		bbt[p] = b; 
		}
		return bbt; 
	} 
        
    static String de61Builder(boolean pinEntered,boolean isEMV,String de52,String merchantAddress,String merchantPincode){
        
    	StringBuilder de61 = new StringBuilder();
        de61.append("4");
        
        if(pinEntered)
        {
            if(de52.equals("0000000000000000"))
            {
                de61.append("1"); 
            }
            else
            {
                de61.append("2");
            }
            de61.append("0"); 
        }
        else
        {
            de61.append("1");
            de61.append("1");
        }
        
        de61.append("0");
        de61.append("1"); 
        de61.append("2");
        if(isEMV)
        {
            de61.append("3"); 
        }
        else
        {
            de61.append("2"); 
        }
        if(pinEntered)
        {
            if(de52.equals("0000000000000000"))
            {
                de61.append("3"); 
            }
            else
            {
                de61.append("2"); 
            }
        }
        else
        {
            de61.append("3"); 
        }
        if(isEMV)
        {
            de61.append("1");
        }
        else
        {
            de61.append("0"); 
        }
        
        if(isEMV)
        {
            de61.append("2"); 
        }
        else
        {
            de61.append("0"); 
        }
        de61.append("3"); 
        de61.append("3"); 
        de61.append("000" + merchantPincode);
        de61.append(merchantAddress); 
        return de61.toString();
    }
          
    public static String translateDe39(String de39){
        switch (de39){
            case "00":
                return "Approved or Completed Successfully";
            case "03":
                return "Invalid Merchant";
            case "04":
                return "Pick-Up";
            case "05":
                return "Do not honour";
            case "06":
                return "Error";
            case "12":
                return "Invalid Transaction";
            case "13":
                return "Invalid Amount";
            case "14":
                return "Invalid Card Number";
            case "15":
                return "No Such Issuer";
            case "17":
                return "Customer Cancellation";
            case "20":
                return "Invalid Response";
            case "21":
                return "No action Taken";
            case "22":
                return "Suspected malfunction";
            case "30":
                return "Format error";
            case "31":
                return "Bank not supported by switch";
            case "33":
                return "Expired card, capture";
            case "34":
                return "Suspected fraud, capture";
            case "36":
                return "Restricted card, capture";
            case "38":
                return "Allowable PIN tries exceeded, capture";
            case "39":
                return "No credit account";
            case "40":
                return "Requested function not supported";
            case "41":
                return "Lost card, capture";
            case "42":
                return "No universal account";
            case "43":
                return "Stolen card, capture";
            case "51":
                return "Not sufficient funds";
            case "52":
                return "No checking account";
            case "53":
                return "No savings account";
            case "54":
                return "Expired card, decline";
            case "55":
                return "Incorrect personal identification number";
            case "56":
                return "No card record";
            case "57":
                return "Transaction not permitted to Cardholder";
            case "58":
                return "Transaction not permitted to terminal";
            case "59":
                return "Suspected fraud, decline/ Transaction declined based on Risk Score";
            case "60":
                return "Card acceptor contact acquirer, decline";
            case "61":
                return "Exceeds withdrawal amount limit";
            case "62":
                return "Restricted card, decline";
            case "63":
                return "Security violation";
            case "65":
                return "Exceeds withdrawal frequency limit";
            case "66":
                return "Card acceptor calls acquirer's ";
            case "67":
                return "Hard capture";
            case "68":
                return "Acquirer time-out";
            case "74":
                return "Transaction declined by issuer based on Risk Score";
            case "75":
                return "Allowable number of PIN tries exceeded, decline";
            case "90":
                return "Cut-off is in process";
            case "91":
                return "Issuer or switch is inoperative";
            case "92":
                return "No routing available";
            case "93":
                return "Transaction cannot be completed. Compliance violation";
            case "94":
                return "Duplicate transmission";
            case "95":
                return "Reconcile error";
            case "96":
                return "System malfunction";
            case "E3":
                return "ARQC validation failed by issuer";
            case "E4":
                return "TVR validation failed by issuer";
            case "E5":
                return "CVR validation failed by issuer";
        }
        return de39;
    }
    
}