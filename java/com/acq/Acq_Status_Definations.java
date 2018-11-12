package com.acq;
public enum Acq_Status_Definations {
	 ClientError("",1),
	 ServerError("Server Error",2),
	 UnexpectedServerError("Unexpected Server Error",100),
	 TidNotFound("TID Not Found",100),
	 MidNotFound("MID Not Found",100),
	 UserValidationError("Acq_User validation Errors",102),
	 UserAccessError("Invalid Acq_User",101),
	 NotFound("Not Found",101),
	 LessLessThanOne("Amount is less than 1Rs.",100),
	 AlreadyInUse("Already In Use",101),
	 RecordNotFound("Records Not Found",101),
	 PGError("Payment gateway Connection Errors",301),
	 PGUserCredentialsError("Acq_User Card details Error",302),
	 OK("OK",0),
	 Authenticated("Authenticated",0),
	 WrongFormat("Invalid Param Value",101),
	 InvalidSession("Session Expired",111),
	 InvalidCrediential("Invalid Credientials",100),
	 
	 
	 BeneNotFound("Beneficiary Not Found",100),
	 NeftFailed("Neft Failed",100),
	 CardLoadFailed("Card Load Failed",100),
	 TransactionsNotFound("Transactions Not found",100),
	 InsufficientDMTBalance("Insufficient DMT Balance",100),
	 InsufficientRechargeBalance("Insufficient Recharge Balance",100),
	 ExceptionInSystemParams("Error To Find System Parameters",100),
	 VoidFailed("Void Tranaction Not Successfull", 0),
	 TransactionSearchFailed("Transaction Search Failed",100),
	 DukptMissing("Dukpt Keys Missing",100),
	 DataElementMissing("Data Element Missing",100),
	 DataPersistanceError("Error To Save Data in DB",100),
	 ExceptionInMerchantInfo("Error To Find Merchant Info",100),
	 TerminalUserNotFound("Terminal User Not Found",100),
	 TerminalInfoNotFound("Terminal Info Not Found",100),
	 TerminalStoreNotFound("Terminal Store Not Found",100),
	 TerminalMerchantNotFound("Terminal Merchant Not Found",100),
	 SerialNotFound("Terminal Serial Not Found",100),
	 InvalidParameters("Invalid Parameters",100),
	 WalletAlreadyInUse("Walletid already in use",100),
	 TransactionTimeOut("Transaction Time Out",102),
	 NoResponseFromSwitch("Response Empty From Switch",102),
	 SwitchConnectionTimeOut("Switch Connection TimeOut",102),
	 SwitchConnectionFailed("Switch Connection Failed Due To Unexpected Error",102),
	 InvalidResponseFromSwitch("Invalid Response From Switch",102),
	 InvalidDe44("Invalid DE44",104),
	 IssuerConnectIssue("Issuer Not Responding",100),
	 CashTransactionAmount("Cash transaction amount should be less than 50000Rs",100),
	 TransactionAmountMismatch("Mismatch Transaction Amount",100),
	 MaintenanceMode("Server Down For Upgradation",100),
	 InvalidData("Records Not Found",100),
	 FailedCardTransaction("Transaction Failed",216),
	 
	 TransactionInitiated("Transaction Inititated",215),
	AmountDebited("Amount Debited",205);
	
	 
	 private String description;
	 private int id;
	 private Acq_Status_Definations(String description, int id) {
		this.description = description;
		this.id = id;
	}
	public String getDescription() {
		return description ;
	}
	
	public int getId() {
		return id;
	}
	
	
}