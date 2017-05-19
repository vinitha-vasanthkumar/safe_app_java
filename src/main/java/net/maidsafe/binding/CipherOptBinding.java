package net.maidsafe.binding;

import net.maidsafe.binding.model.FfiCallback.HandleCallback;
import net.maidsafe.binding.model.FfiCallback.ResultCallback;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

public interface CipherOptBinding extends Library {

	void cipher_opt_new_plaintext(Pointer app, Pointer userData,
			HandleCallback cb);

	void cipher_opt_new_symmetric(Pointer app, Pointer userData,
			HandleCallback cb);

	void cipher_opt_new_asymmetric(Pointer app, long pubEncKeyhandle,
			Pointer userData, HandleCallback cb);

	void cipher_opt_free(Pointer app, long handle, Pointer userData,
			ResultCallback cb);
}
