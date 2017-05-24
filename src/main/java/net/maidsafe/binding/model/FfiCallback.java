package net.maidsafe.binding.model;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public class FfiCallback {

	public interface Auth extends Callback {
		void onResponse(Pointer userData, FfiResult.ByVal result, int reqId,
				String uri);
	}

	public interface AuthGranted extends Callback {
		void onResponse(Pointer userData, int reqId,
				AuthGrantedResponse authGranted);
	}

	public interface ReqIdCallback extends Callback {
		void onResponse(Pointer userData, int reqId);
	}

	public interface NoArgCallback extends Callback {
		void onResponse(Pointer userData);
	}

	public interface ErrorCallback extends Callback {
		void onResponse(Pointer userData, FfiResult.ByVal result, int reqId);
	}

	public interface NetworkObserverCallback extends Callback {
		void onResponse(Pointer userData, int errorCod, int event);
	}

	public interface HandleCallback extends Callback {
		void onResponse(Pointer userData, FfiResult.ByVal result, long handle);
	}

	public interface TwoHandleCallback extends Callback {
		void onResponse(Pointer userData, FfiResult.ByVal result,
				long handleOne, long handleTwo);
	}

	public interface ResultCallback extends Callback {
		void onResponse(Pointer userData, FfiResult.ByVal result);
	}

	public interface PointerCallback extends Callback {
		void onResponse(Pointer userData, FfiResult.ByVal result,
				Pointer pointer);
	}

	public interface BooleanCallback extends Callback {
		void onResponse(Pointer userData, FfiResult.ByVal result, boolean flag);
	}

	public interface CallbackForData extends Callback {
		void onResponse(Pointer userData, FfiResult.ByVal result, Pointer data,
				long dataLen);
	}
}
