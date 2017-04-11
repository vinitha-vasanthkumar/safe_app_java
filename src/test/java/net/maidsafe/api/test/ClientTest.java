package net.maidsafe.api.test;

import net.maidsafe.api.Client;
import junit.framework.TestCase;

public class ClientTest extends TestCase {

	public void testAuthorisation() {
		Client client;
		client = new Client();
		assertEquals(client.authorise(), 0);
	}
	
}
