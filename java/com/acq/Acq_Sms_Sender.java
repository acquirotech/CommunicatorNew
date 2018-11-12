package com.acq;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
public class Acq_Sms_Sender {
	
	//private static Logger logger = Logger.getLogger(Acq_Sms_Sender.class);
	String username;
	String password;
	String message;
	String sender;
	String sendto;
	String server;
	int port;
	public Acq_Sms_Sender(String server, String username, String password,
			String message, String sender, String sendto) {
		this.username = username;
		this.password = password;
		this.message = message;
		this.sendto = sendto;
		this.sender = sender;
		this.server = server;
	}
	private void submitMessage() {
		try {
			URL sendUrl = new URL("http://" + "sms.copiousinfo.com/api/swsendSingle.asp?");
			HttpURLConnection httpConnection = (HttpURLConnection)sendUrl.openConnection();
			System.out.println("wefewfwefwef");
			httpConnection.setConnectTimeout(9000);
			httpConnection.setReadTimeout(9000);
			httpConnection.setRequestMethod("GET");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);
			DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());
			dataStreamToServer.writeBytes("username="
					+ URLEncoder.encode(this.username, "UTF-8") + "&password="
					+ URLEncoder.encode(this.password, "UTF-8") + "&sender="
					+ URLEncoder.encode(this.sender, "UTF-8") + "&sendto="
					+ URLEncoder.encode(this.sendto, "UTF-8") + "&message="
					+ URLEncoder.encode(this.message, "UTF-8"));
			dataStreamToServer.flush();
			dataStreamToServer.close();
			BufferedReader dataStreamFromUrl = new BufferedReader(
					new InputStreamReader(httpConnection.getInputStream()));
			String dataFromUrl = "", dataBuffer = "";
			while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
				dataFromUrl += dataBuffer;
			}
			dataStreamFromUrl.close();
			System.out.println("Response: " + dataFromUrl);
		}catch(SocketTimeoutException e){
			System.out.println("Error to send SMS,SMS Server is down");
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error to send sms, in Acq_Sms_Sender");
		}
	}
	public static void sendRouteSMS(String message,String mobileNo){
		System.out.println("Mobile No :"+mobileNo);
		Acq_Sms_Sender s = new Acq_Sms_Sender("sms.copiousinfo.com","t1ptpl",
				"PaxT@ch", message,"PaxPay", mobileNo);
		s.submitMessage();
	}
	
	
}
