package net.maidsafe.binding.model;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class AuthGrantedResponse extends Structure {

	public AppKeys app_keys;
	public AccessContInfo access_container;
	public Pointer bootstrap_config_ptr;
	public long bootstrap_config_len;
	public long bootstrap_config_cap;

	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList("app_keys", "access_container",
				"bootstrap_config_ptr", "bootstrap_config_cap",
				"bootstrap_config_len");
	}

}
