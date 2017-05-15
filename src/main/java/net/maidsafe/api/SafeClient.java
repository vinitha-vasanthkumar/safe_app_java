package net.maidsafe.api;

import com.sun.jna.Native;
import net.maidsafe.binding.AuthBinding;

public class SafeClient {
	
		
	public int authorise() {
		try {
			Native.register("libwinpthread-1.dll");
			Native.register("safe_app.dll");
			Native.loadLibrary(AuthBinding.class);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return 0;
	}
	
	

}
