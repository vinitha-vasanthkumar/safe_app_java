package net.maidsafe.api.test;

import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

import junit.framework.TestCase;
import net.maidsafe.api.SafeClient;
import net.maidsafe.api.model.EncryptKeyPair;
import net.maidsafe.api.model.PublicEncryptKey;
import net.maidsafe.api.model.PublicSignKey;
import net.maidsafe.utils.FfiConstant;

public class CryptoTest extends TestCase {

	public void testAppPublicSignKey() throws Exception {
		SafeClient client = Utils.getTestAppWithAccess();
		PublicSignKey key = client.getCrypto().getAppPublicSignKey().get();
		assert (key != null);
		byte[] raw = key.getRaw().get();
		assertEquals(raw.length, FfiConstant.SIGN_PUBLICKEYBYTES);
		key = client.getCrypto().getPublicSignKey(raw).get();
		assert (Arrays.equals(raw, key.getRaw().get()));
	}

	public void testAppPublicSignKeyInvalidSize() throws Exception {
		SafeClient client = Utils.getTestAppWithAccess();
		PublicSignKey key = client.getCrypto().getAppPublicSignKey().get();
		assert (key != null);
		byte[] raw = key.getRaw().get();
		assertEquals(raw.length, FfiConstant.SIGN_PUBLICKEYBYTES);
		try {
			client.getCrypto().getPublicSignKey(Arrays.copyOf(raw, 10)).get();
		} catch (Exception e) {
			assert (e != null);
		}
		try {
			client.getCrypto().getPublicSignKey(null).get();
		} catch (Exception e) {
			assert (e != null);
		}
	}

	public void testAppPublicEncryptKey() throws Exception {
		SafeClient client = Utils.getTestAppWithAccess();
		PublicEncryptKey key = client.getCrypto().getAppPublicEncryptKey()
				.get();
		assert (key != null);
		byte[] raw = key.getRaw().get();
		assertEquals(raw.length, FfiConstant.BOX_PUBLICKEYBYTES);
		key = client.getCrypto().getPublicEncryptKey(raw).get();
		assert (Arrays.equals(raw, key.getRaw().get()));
	}

	public void testAppPublicEncryptKeyInvalidSize() throws Exception {
		SafeClient client = Utils.getTestAppWithAccess();
		PublicEncryptKey key = client.getCrypto().getAppPublicEncryptKey()
				.get();
		assert (key != null);
		byte[] raw = key.getRaw().get();
		assertEquals(raw.length, FfiConstant.BOX_PUBLICKEYBYTES);
		try {
			client.getCrypto().getPublicEncryptKey(Arrays.copyOf(raw, 10))
					.get();
		} catch (Exception e) {
			assert (e != null);
		}
		try {
			client.getCrypto().getPublicEncryptKey(null).get();
		} catch (Exception e) {
			assert (e != null);
		}
	}

	public void testEncryptKeyPairGeneration() throws Exception {
		SafeClient client = Utils.getTestAppWithAccess();
		EncryptKeyPair pair = client.getCrypto().generateEncryptKeyPair().get();
		assert (pair.getPublicKey() != null);
		assert (pair.getSecretKey() != null);
		client.getCrypto()
				.getSecretEncryptKey(pair.getSecretKey().getRaw().get()).get();
	}

	public void testSha3Hash() throws Exception {
		String expected = "DDAD25FB24BD67C0AD883AC9C747943036EC068837C8A894E44F29244548F4ED";
		SafeClient client = Utils.getTestAppWithAccess();
		byte[] hash = client.getCrypto().hashSHA3("Demo".getBytes()).get();
		assertEquals(expected, DatatypeConverter.printHexBinary(hash));
	}

	public void testBoxEncryption() throws Exception {
		SafeClient clientOne = Utils.getTestAppWithAccess();

		EncryptKeyPair senderKeys = clientOne.getCrypto()
				.generateEncryptKeyPair().get();
		EncryptKeyPair recieverKeys = clientOne.getCrypto()
				.generateEncryptKeyPair().get();

		byte[] cipherText = recieverKeys.getPublicKey()
				.encrypt("message".getBytes(), senderKeys.getSecretKey()).get();
		byte[] plainText = recieverKeys.getSecretKey()
				.decrypt(cipherText, senderKeys.getPublicKey()).get();

		assertEquals(new String(plainText), "message");
	}

	public void testBoxSealedEncryption() throws Exception {
		SafeClient clientOne = Utils.getTestAppWithAccess();

		EncryptKeyPair recieverKeys = clientOne.getCrypto()
				.generateEncryptKeyPair().get();

		byte[] cipherText = recieverKeys.getPublicKey()
				.encryptSealed("message".getBytes()).get();
		byte[] plainText = recieverKeys.decryptSealed(cipherText).get();

		assertEquals(new String(plainText), "message");
	}
}
