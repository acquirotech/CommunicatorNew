package com.acq;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Acq_ThinkWalnut_Utilities {
	
	final static Logger logger = Logger.getLogger(Acq_ThinkWalnut_Utilities.class);
	

		// For Test 
		String userPassword = "memberId=sandbox&passwd=test123";
		String authrization = " Basic 77de68daecd823babbb58edb1c8e14d7106e83bb";

		// For Live
		/*String userPassword = "memberId=TW1033&passwd=3FbwBrS5";		
		String authrization = " Basic af3e133428b9e25c55bc59fe534248e6a0c0f17b";*/
		
		public Map<String,String> tw_Port_Connector(String apiurl, String request, String callFrom) {			
			
			logger.info(callFrom + "::" + "tw_Port_Connector" + "::" + "Begin");
			
			URL apiURL;
			Map<String,String> responseMap =  new HashMap<String,String>();
			HttpURLConnection twConnector = null;
			
			try {
					apiURL = new URL(apiurl);  
					twConnector = (HttpURLConnection)apiURL.openConnection();  
					twConnector.setConnectTimeout(8000);
					twConnector.setReadTimeout(8000);
					twConnector.setRequestMethod("POST");
					twConnector.setDoInput(true);
					twConnector.setDoOutput(true);
					twConnector.setUseCaches(false);
					twConnector.setRequestProperty("Authorization",authrization);
					DataOutputStream uploadStream = new DataOutputStream(twConnector.getOutputStream());					  
					uploadStream.writeBytes(userPassword+request);
					uploadStream.flush();
					uploadStream.close();
					logger.info(callFrom + "::" + "tw_Port_Connector" + "::" + "Data Uploaded");
					
					BufferedReader downloadStream = new BufferedReader(
							new InputStreamReader(twConnector.getInputStream()));
					String dataFromUrl = "", dataBuffer = "";
					
					while ((dataBuffer = downloadStream.readLine()) != null) {
						dataFromUrl += dataBuffer;
					}			
					
					downloadStream.close();
					logger.info(callFrom + "::" + "tw_Port_Connector" + "::" + "Data Downloaded");

					JSONObject apiResponse = (JSONObject)new JSONParser().parse(dataFromUrl);	
					logger.info(callFrom + "::" + "tw_Port_Connector" + "::" + "Data Parsed to Json");
					
					if(request.contains("refId")){
						responseMap.put("optId",""+apiResponse.get("optId"));	
					}
					responseMap.put("txnDateTime",""+apiResponse.get("ts"));
					responseMap.put("clientId",""+apiResponse.get("clientId"));	
					responseMap.put("txnId",""+apiResponse.get("txnId"));	
					responseMap.put("errCode",""+apiResponse.get("errCode"));	
					responseMap.put("msg",""+apiResponse.get("msg"));	
					logger.info(callFrom + "::" + "tw_Port_Connector" + "::" + "Returning To Caller");
					return responseMap;
				}
			catch(Exception e)
			{
				e.printStackTrace();
				logger.info(callFrom + "::" + "tw_Port_Connector" + "::" + "Connection Failed");
				return null;
			}
			finally {
				if(twConnector!= null)
				{
					twConnector.disconnect();
				}
			}
		}
		
}
