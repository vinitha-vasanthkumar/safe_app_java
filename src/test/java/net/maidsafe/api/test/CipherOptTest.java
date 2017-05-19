package net.maidsafe.api.test;

import net.maidsafe.api.SafeClient;
import net.maidsafe.api.model.CipherOpt;
import net.maidsafe.api.model.PublicEncryptKey;
import junit.framework.TestCase;

public class CipherOptTest extends TestCase {

	public void testGetPlainCipherOpt() throws Exception {
		SafeClient client = Utils.getTestAppWithAccess();

		CipherOpt opt = client.cipherOpt().getPlain().get();
		assert (opt != null);
	}

	public void testGetSymmetricCipherOpt() throws Exception {
		SafeClient client = Utils.getTestAppWithAccess();

		CipherOpt opt = client.cipherOpt().getSymmetric().get();
		assert (opt != null);
	}

	public void testGetAsymmetricCipherOpt() throws Exception {
		SafeClient client = Utils.getTestAppWithAccess();

		PublicEncryptKey pubKey = client.crypto().getAppPublicEncryptKey()
				.get();
		CipherOpt opt = client.cipherOpt().getAsymmetric(pubKey).get();
		assert (opt != null);
	}

}
