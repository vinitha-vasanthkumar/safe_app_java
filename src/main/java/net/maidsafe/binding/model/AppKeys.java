package net.maidsafe.binding.model;

import java.util.Arrays;
import java.util.List;

import net.maidsafe.utils.FfiConstant;

import com.sun.jna.Structure;

public class AppKeys extends Structure {

	public byte[] owner_key = new byte[FfiConstant.SIGN_PUBLICKEYBYTES];
	// Data symmetric encryption key
	public byte[] enc_key = new byte[FfiConstant.SECRETBOX_KEYBYTES];
	// Asymmetric sign public key.

	// This is the identity of the App in the Network.
	public byte[] sign_pk = new byte[FfiConstant.SIGN_PUBLICKEYBYTES];
	// Asymmetric sign private key.
	public byte[] sign_sk = new byte[FfiConstant.SIGN_SECRETKEYBYTES];
	// Asymmetric enc public key.
	public byte[] enc_pk = new byte[FfiConstant.BOX_PUBLICKEYBYTES];
	// Asymmetric enc private key.
	public byte[] enc_sk = new byte[FfiConstant.BOX_SECRETKEYBYTES];

	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList("owner_key", "enc_key", "sign_pk", "sign_sk",
				"enc_pk", "enc_sk");
	}

}
