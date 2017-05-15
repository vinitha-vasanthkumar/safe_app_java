package net.maidsafe.binding.model;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public class FfiCallback {
	
	public interface Auth extends Callback {
		void onResponse(Pointer userData, FfiResult result, int reqId, String uri);
	}
	
}
