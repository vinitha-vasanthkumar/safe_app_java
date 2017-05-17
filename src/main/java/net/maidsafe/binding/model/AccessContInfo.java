package net.maidsafe.binding.model;

import java.util.Arrays;
import java.util.List;

import net.maidsafe.utils.FfiConstant;

import com.sun.jna.Structure;

public class AccessContInfo extends Structure {

	// ID
	public byte[] id = new byte[FfiConstant.XOR_NAME_LEN];
	// Type tag
	public long tag;
	// Nonce
	public byte[] nonce = new byte[FfiConstant.SECRETBOX_NONCEBYTES];

	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList("id", "tag", "nonce");
	}
}
