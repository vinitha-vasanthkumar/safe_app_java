package net.maidsafe.binding;

public interface CryptoBinding {

	void app_pub_sign_key();
	void sign_key_new();
	void sign_key_get();
	void sign_key_free();
	void app_pub_enc_key();
	void enc_generate_key_pair();
	void enc_pub_key_new();
	void enc_pub_key_get();
	void enc_secret_key_new();
	void enc_secret_key_get();
	void enc_pub_key_free();
	void enc_secret_key_free();
	void encrypt();
	void decrypt();
	void encrypt_sealed_box();
	void decrypt_sealed_box();
	void sha3_hash();
	
}
