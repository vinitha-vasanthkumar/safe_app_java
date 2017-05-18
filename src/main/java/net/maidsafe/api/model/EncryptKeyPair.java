package net.maidsafe.api.model;

public class EncryptKeyPair {

	private SecretEncryptKey secretKey;
	private PublicEncryptKey publicKey;
	
	public EncryptKeyPair(SecretEncryptKey secretKey, PublicEncryptKey publicKey) {
		this.secretKey = secretKey;
		this.publicKey = publicKey;
	}

	public SecretEncryptKey getSecretKey() {
		return secretKey;
	}

	public PublicEncryptKey getPublicKey() {
		return publicKey;
	}
		
}
