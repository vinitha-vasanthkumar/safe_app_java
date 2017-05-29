package net.maidsafe.api;

import net.maidsafe.binding.model.FfiResult;

public class Exception extends java.lang.Exception {
		
	private static final long serialVersionUID = -4472820439176985115L;
	
	private int errorCode;
	private String descripton;
	
	public Exception(FfiResult result) {
		super();
		this.errorCode = result.error_code;
		this.descripton = result.description;
	}

	public Exception(CustomError error) {
		super();
		this.errorCode = error.getErrorCode();
		this.descripton = error.getDescription();
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getDescripton() {
		return descripton;
	}	
	
}
