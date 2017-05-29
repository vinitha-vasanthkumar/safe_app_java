package net.maidsafe.binding.model;

public class AuthUriResponse {

	private int reqId;
	private String uri;
	
	public AuthUriResponse(int reqId, String uri) {
		this.reqId = reqId;
		this.uri = uri;
	}
	
	public int getReqId() {
		return reqId;
	}
	
	public String getUri() {
		return uri;
	}
	
}
