package net.maidsafe.binding.model;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public class FfiCallback {
	
	public interface Auth extends Callback {
		void onResponse(Pointer userData, FfiResult result, int reqId, String uri);
	}
	
	public interface AuthGranted extends Callback {
		void onResponse(Pointer userData, int reqId, AuthGrantedResponse authGranted);
	}
	
	public interface ReqIdCallback extends Callback {
		void onResponse(Pointer userData, int reqId);
	}
	
	public interface NoArgCallback extends Callback {
		void onResponse(Pointer userData);
	}
	
	public interface ErrorCallback extends Callback {
		void onResponse(Pointer userData, FfiResult result, int reqId);
	}
	
	public interface NetworkObserverCallback extends Callback {
		void onResponse(Pointer userData, int errorCod, int event);
	}
	
}
