package com.eccentex.dcm.MobileApp;

/**
 * Created by Nikolay on 12.08.2014.
 */
public  class ErrorException extends Exception{
	private int errorCode = 0;
	public String errorMessage ="Ok";
	int getErrorCode(){
		return  errorCode;
	}
	String getErrorMessage(){
		return  errorMessage;
	}
	void setErrorCode(int value){
		errorCode = value;
	}
	void setErrorMessage(String message){
		errorMessage = message;
	}
}
