package net.maidsafe.binding;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import net.maidsafe.binding.model.AuthGrantedResponse;
import net.maidsafe.binding.model.AuthReq;
import net.maidsafe.binding.model.FfiContainerPermission;
import net.maidsafe.binding.model.ContainerRequest;
import net.maidsafe.binding.model.FfiCallback;

public interface AuthBinding extends Library {

	void encode_auth_req(AuthReq req, Pointer userPointer, FfiCallback.Auth cb);

	void encode_containers_req(ContainerRequest req, Pointer userPointer,
			FfiCallback.Auth cb);

	void decode_ipc_msg(String uri, Pointer userPointer,
			FfiCallback.AuthGranted authCb,
			FfiCallback.ReqIdCallback containerCb,
			FfiCallback.NoArgCallback revokedCb, FfiCallback.ErrorCallback errCb);

	void app_registered(String appId, AuthGrantedResponse authGranted,
			Pointer userPointer, FfiCallback.NetworkObserverCallback obsCb,
			PointerByReference appPointerRef);

	void app_unregistered(Pointer userPointer,
			FfiCallback.NetworkObserverCallback obsCb,
			PointerByReference appPointerRef);

	// //Test utils
	int test_create_app(PointerByReference appPointerRef);

	int test_create_app_with_access(FfiContainerPermission[] permissions,
			long permissionsLength, PointerByReference appPointerRef);
}
