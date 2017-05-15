package net.maidsafe.binding;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import net.maidsafe.binding.model.AuthReq;
import net.maidsafe.binding.model.FfiCallback;

public interface AuthBinding extends Library {

	void encode_auth_req(AuthReq req, Pointer userPointer, FfiCallback.Auth cb);
//	void encode_containers_req();
//	void decode_ipc_msg();
//	void app_unregistered();
//	void app_registered();
//	void app_free();
//	//Test utils
//	void test_create_app();
//	void test_create_app_with_access();
}
