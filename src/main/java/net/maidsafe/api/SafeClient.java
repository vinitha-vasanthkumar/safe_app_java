package net.maidsafe.api;

import net.maidsafe.api.model.App;

public class SafeClient {
	private final App app;
	private final Crypto crypto;

	public SafeClient(App app) {
		this.app = app;
		crypto = new Crypto(app);
	}

	public boolean isAnonymous() {
		return app.isAnonymous();
	}

	public Crypto getCrypto() {
		return crypto;
	}

}
