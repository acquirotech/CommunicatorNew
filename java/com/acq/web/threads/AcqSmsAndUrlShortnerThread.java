package com.acq.web.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.acq.Acq_Sms_Sender;

import security.AcquiroSeq;



public class AcqSmsAndUrlShortnerThread implements Runnable{

	
	final static Logger logger = Logger.getLogger(AcqSmsAndUrlShortnerThread.class);
	private String transactionId;
	private String txnStatus;
	private String amount;
	private String dateTime;	
	private String cardPanNo;
	private String appHost;
	private String marketingName;
	private String rmn;
	private String custPhone;
	private String txnType;
	
	
	public String shortUrl(String appHost,String receiptId){
		String urlResult="";
		HttpURLConnection conn = null;
		try {
			logger.info("hitting google shortner api");
			URL url = new URL("https://www.googleapis.com/urlshortener/v1/url?key=AIzaSyBApNbXPE2LmmCtaz7pOeQMZgaFsGeoAXg");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			String input = "{\"longUrl\":\""+appHost+"/sign/receipt?signImage="+receiptId+"\"}";
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			String output;		
			String reponseString="";
			while ((output = br.readLine()) != null) {
				reponseString += output;
			}			
			JSONObject body = null;
			try {
				body = (JSONObject)new JSONParser().parse(reponseString.toString());
			} catch (ParseException e) {
				e.printStackTrace();
				urlResult="error";
				return urlResult;
			}
			urlResult=""+body.get("id");
			logger.info("shortner url is "+body.get("id"));			
			return urlResult;
		  } catch (MalformedURLException e) {			
			logger.error("error to short url " +e);
			urlResult="error";
 			return urlResult;
		  } catch (IOException e) {			
			logger.error("error to short url " +e);
			urlResult="error";
			return urlResult;
		  }catch(Exception e){
			logger.error("error to short url " +e);
			urlResult="error";
			return urlResult;
		  }		
		  finally{
			  conn.disconnect();
		  }
	}
	
	@Override
	public void run() {
		//String transactionIds= ""+getTransactionId();
		String message="";
		 if(getTxnType().equals("PLACE")){
			System.out.println("card place");
			if(Integer.valueOf(getTxnStatus())==505){
				//System.out.println("transactionIds:"+getTransactionId()+":");
				String txnId = AcquiroSeq.MakeSmart(getTransactionId());
				byte[]   bytesEncoded = Base64.encodeBase64(txnId .getBytes());
				String shortnerUrl = shortUrl(appHost,new String(bytesEncoded));
				if(shortnerUrl.equals("error")){
					message = "INR "+getAmount()+" debited on "+getDateTime()+" from card no "+getCardPanNo()+" at "+getMarketingName()+ ", "+getAppHost()+"/sign/receipt?signImage="+new String(bytesEncoded)+" Powered by Acquiro!";
				}else{
					message = "INR "+getAmount()+" debited on "+getDateTime()+" from card no "+getCardPanNo()+" at "+getMarketingName()+ ", "+shortnerUrl+" Powered by Acquiro!";
				}
				//message = "INR "+list.get(0)+" at "+list.get(4)+" is approved on "+list.get(2)+" from Acquiro: "+model.getCustPhone()+". Txn ID:"+model.getTransactionId()+". Powered by Acquiro!";
			}else{
				message = "INR "+getAmount()+" failed on "+getDateTime()+" from card no "+getCardPanNo()+" at "+getMarketingName()+" Powered by Acquiro!";
				//message = "INR "+list.get(0)+" at "+list.get(4)+" is failed on "+list.get(2)+" from Acquiro: "+model.getCustPhone()+". Txn ID:"+model.getTransactionId()+". Powered by Acquiro!";
			}
			Acq_Sms_Sender.sendRouteSMS(message, custPhone+","+getRmn());
			logger.info("receipt sms sent to customer");
		}else if(getTxnType().equals("VOID")){
			String txnId = AcquiroSeq.MakeSmart(getTransactionId());
			byte[]   bytesEncoded = Base64.encodeBase64(txnId .getBytes());
			String shortnerUrl = shortUrl(appHost,new String(bytesEncoded));		
			logger.info("void txn short url is: "+shortnerUrl);
			if(shortnerUrl.equals("error")){
				message = "INR "+getAmount()+" is Approved on "+getDateTime()+" from card no "+getCardPanNo()+" has been voided at "+getMarketingName()+", "+getAppHost()+"/sign/receipt?signImage="+new String(bytesEncoded)+" Powered by Acquiro!";
				Acq_Sms_Sender.sendRouteSMS(message, getCustPhone()+","+getRmn());	
				//System.out.println(getCustPhone()+":aaaaaaaaaaaaaaaaaaaaaa");
			}else{
				message = "INR "+getAmount()+" is Approved on "+getDateTime()+" from card no "+getCardPanNo()+" has been voided at "+getMarketingName()+", "+ shortnerUrl+" Powered by Acquiro!";
				Acq_Sms_Sender.sendRouteSMS(message, getCustPhone()+","+getRmn());	
				//System.out.println(getCustPhone()+":bbbbbbbbbbbbbbbbbbbbbb:"+getRmn());
			}
		}else if(getTxnType().equals("REVERSAL")){
			String txnId = AcquiroSeq.MakeSmart(getTransactionId());
			byte[]   bytesEncoded = Base64.encodeBase64(txnId .getBytes());
			String shortnerUrl = shortUrl(appHost,new String(bytesEncoded));		
			logger.info("reversal short url is: "+shortnerUrl);
			if(shortnerUrl.equals("error")){
				message = "Dear customer, your transaction was not completed successfully, if your amount was debited it will be refunded in your account. "+getAppHost()+"/sign/receipt?signImage="+new String(bytesEncoded);
				Acq_Sms_Sender.sendRouteSMS(message, getCustPhone());	
			}else{
				//message = "INR "+getAmount()+" is Approved on "+getDateTime()+" from card no "+getCardPanNo()+" has been voided at "+getMarketingName()+", "+ shortnerUrl+" Powered by Acquiro!";
				message = "Dear customer, your transaction was not completed successfully, if your amount was debited it will be refunded in your account. "+shortnerUrl+"";
				Acq_Sms_Sender.sendRouteSMS(message, getCustPhone());	
			}
		}
	}
	
	

	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getRmn() {
		return rmn;
	}
	public void setRmn(String rmn) {
		this.rmn = rmn;
	}
	public String getCustPhone() {
		return custPhone;
	}
	public void setCustPhone(String custPhone) {
		this.custPhone = custPhone;
	}
	public String getMarketingName() {
		return marketingName;
	}
	public void setMarketingName(String marketingName) {
		this.marketingName = marketingName;
	}
	public String getTxnStatus() {
		return txnStatus;
	}
	public void setTxnStatus(String txnStatus) {
		this.txnStatus = txnStatus;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getAppHost() {
		return appHost;
	}



	public void setAppHost(String appHost) {
		this.appHost = appHost;
	}



	public String getTransactionId() {
		return transactionId;
	}



	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}



	public String getCardPanNo() {
		return cardPanNo;
	}



	public void setCardPanNo(String cardPanNo) {
		this.cardPanNo = cardPanNo;
	}



	

}
