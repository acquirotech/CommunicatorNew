package com.acq;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.acq.web.dao.Acq_Dao;
import com.acq.web.dao.impl.Acq_Bankit_Transactions_Dao;

public class Acq_Bankit_Transactions_Utilities extends Acq_Dao {
	final static Logger logger = Logger.getLogger(Acq_Bankit_Transactions_Dao.class);
	
	// Test Url Details
		String url = "http://125.63.96.115:9012/DMR/";
		String apiId = "10039";
		String AgentAuthId = "PAX TECHNOLOGIES PRIVATE LIMITED281649";
		String AgentAuthPassword = "kj74gvlai4";

	// Live URL Details
		/*String url = "https://services.bankit.in:8443/DMRV1.1/";
		String apiId = "10039";
		String AgentAuthId = "PAX TECHNOLOGIES PRIVATE LIMITED281649";
		String AgentAuthPassword = "kj74gvlai4";*/
	
		public JSONObject bankitDMTConnector(String request,String requestUrl, String callFrom) {			
			
			logger.info(callFrom + "::" + "bankitDMTConnector" + "::" + "Begin");
			HttpURLConnection bankitConnector = null;
			try {
				URL sendUrl = new URL(url+requestUrl);  
				bankitConnector = (HttpURLConnection)sendUrl.openConnection();  
				bankitConnector.setConnectTimeout(8000);
				bankitConnector.setReadTimeout(8000);
				bankitConnector.setRequestMethod("POST");
				bankitConnector.setDoInput(true);
				bankitConnector.setDoOutput(true);
				bankitConnector.setUseCaches(false);
				bankitConnector.setRequestProperty("Authorization","Basic UEFYIFRFQ0hOT0xPR0lFUyBQUklWQVRFIExJTUlURUQyODE2NDk6a2o3NGd2bGFpNA==");
				bankitConnector.setRequestProperty("Content-Type", "application/json");
				bankitConnector.setRequestProperty("apiId",apiId);
				bankitConnector.setRequestProperty("AgentAuthId",AgentAuthId);
				bankitConnector.setRequestProperty("AgentAuthPassword",AgentAuthPassword);
				
				if(!request.isEmpty())
					{
					DataOutputStream uploadStream = new DataOutputStream(bankitConnector.getOutputStream());
					uploadStream.writeBytes(request);
					uploadStream.flush();
					uploadStream.close();
					logger.info(callFrom + "::" + "bankitDMTConnector" + "::" + "Data Uploaded");
					}

				BufferedReader downloadStream = new BufferedReader(new InputStreamReader(bankitConnector.getInputStream()));
				String portResponse = "", singleLine = "";
				
				//Reading response from Bankit
				while ((singleLine = downloadStream.readLine()) != null) {
					portResponse += singleLine;
				}
				downloadStream.close();
				logger.info(callFrom + "::" + "bankitDMTConnector" + "::" + "Data Downloaded");
				
				//Parsing String into json format
				JSONObject portResponseJson  = (JSONObject)new JSONParser().parse(portResponse);	
				logger.info(callFrom + "::" + "bankitDMTConnector" + "::" + "Data Parsed To Json");
				return portResponseJson;
			}
			catch(java.net.SocketTimeoutException e)
			{
				logger.info(callFrom + "::" + "bankitDMTConnector" + "::" + "Connection Timeout With Bankit");
				e.printStackTrace();
				return null;
			}
			catch(Exception e)
			{
				logger.info(callFrom + "::" + "bankitDMTConnector" + "::" + "Connection Error With Bankit");
				e.printStackTrace();
				return null;
			}
			finally {
				if(bankitConnector!= null)
				{
					bankitConnector.disconnect();
				}
			}
		}
			

		public JSONObject callPrepaidApi(String request, String requestUrl,String url) {			
			URL sendUrl;
			logger.info(url+"::Bankit Card Load Api" + "::" + "Begin");
			Map<String,String> map =  new HashMap<String,String>();  
			JSONObject json = new JSONObject();
			try {
				sendUrl = new URL("https://portal.bankit.in:9090/BANKITMRA/resources/AESPPC/"+requestUrl);  
				HttpURLConnection httpConnection = (HttpURLConnection)sendUrl.openConnection();  
				httpConnection.setConnectTimeout(8000);
				httpConnection.setReadTimeout(8000);
				httpConnection.setRequestMethod("POST");
				httpConnection.setDoInput(true);
				httpConnection.setDoOutput(true);
				httpConnection.setUseCaches(false);
				httpConnection.setRequestProperty("Authorization"," Basic UEFYIFRFQ0hOT0xPR0lFUyBQUklWQVRFIExJTUlURUQyODE2NDk6a2o3NGd2bGFpNA==");
				httpConnection.setRequestProperty("Content-Type", "application/json");
				DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());
				dataStreamToServer.writeBytes(request);
				System.out.println("Headr::"+httpConnection.getOutputStream());
				System.out.println("Header::"+httpConnection+":::Request::"+request);
				dataStreamToServer.flush();
				dataStreamToServer.close();
				dataStreamToServer.flush();
				dataStreamToServer.close();
				logger.info(url + "::" + "bankitDMTConnector" + "::" + "Data Uploaded");
				BufferedReader dataStreamFromUrl = new BufferedReader(
						new InputStreamReader(httpConnection.getInputStream()));
				String dataFromUrl = "", dataBuffer = "";
				while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
					dataFromUrl += dataBuffer;
				}
				dataStreamFromUrl.close();
				logger.info(url + "::" + "bankitDMTConnector" + "::" + "Data Downloaded");
				 json = (JSONObject)new JSONParser().parse(dataFromUrl);	
				return json;
			}catch(IOException ioe){
				return json;
			}catch(Exception e){
				map.put("status", "-1");
				map.put("message", "Wrong phone or access token");
				System.out.println("errror "+e);
				return json;
			}
		}	


}
