package net.maidsafe.api.model;

import java.util.Arrays;
import java.util.List;

import net.maidsafe.binding.model.AppKeys;

public class Keys {

	private byte[] ownerKey;
	// Data symmetric encryption key
	private byte[] symmetricEncryptKey;
	// Asymmetric sign public key.

	// This is the identity of the App in the Network.
	private byte[] signPublicKey;
	// Asymmetric sign private key.
	private byte[] signSecretKey;
	// Asymmetric enc public key.
	private byte[] encryptionPublicKey;
	// Asymmetric enc private key.
	private byte[] encryptionSecretKey;
	
	public Keys(AppKeys keys) {
		this.ownerKey = keys.owner_key;
		this.symmetricEncryptKey = keys.enc_key;
		this.encryptionPublicKey = keys.enc_pk;
		this.encryptionSecretKey = keys.enc_sk;
		this.signPublicKey = keys.sign_pk;
		this.signSecretKey = keys.sign_sk;
	}

	public byte[] getOwnerKey() {
		return ownerKey;
	}

	public void setOwnerKey(byte[] ownerKey) {
		this.ownerKey = ownerKey;
	}

	public byte[] getSymmetricEncryptKey() {
		return symmetricEncryptKey;
	}

	public void setSymmetricEncryptKey(byte[] symmetricEncryptKey) {
		this.symmetricEncryptKey = symmetricEncryptKey;
	}

	public void setEncryptionKeyPair(byte[] publicKey, byte[] secretKey) {
		this.encryptionPublicKey = publicKey;
		this.encryptionSecretKey = secretKey;
	}

	public void setSignKeyPair(byte[] publicKey, byte[] secretKey) {
		this.signPublicKey = publicKey;
		this.signSecretKey = secretKey;
	}

	public List<byte[]> getEncryptionKeyPair() {
		return Arrays
				.asList(this.encryptionPublicKey, this.encryptionSecretKey);
	}

	public List<byte[]> getSignKeyPair() {
		return Arrays.asList(this.signPublicKey, this.signSecretKey);
	}
}
