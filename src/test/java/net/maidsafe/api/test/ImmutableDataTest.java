package net.maidsafe.api.test;

import net.maidsafe.api.SafeClient;
import net.maidsafe.api.model.CipherOpt;
import net.maidsafe.api.model.ImmutableDataReader;
import net.maidsafe.api.model.ImmutableDataWriter;
import net.maidsafe.api.model.XorName;
import junit.framework.TestCase;

public class ImmutableDataTest extends TestCase {

	public void testReadAndWriteAsPlain() throws Exception {
		ImmutableDataWriter writer;
		ImmutableDataReader reader;
		CipherOpt cipherOpt;
		XorName name;
		SafeClient client;
		byte[] data;
		String sample = "sample data" + Math.random();
		client = Utils.getTestAppWithAccess();
				
		writer = client.immutableData().getWriter().get();		
		cipherOpt = client.cipherOpt().getPlain().get();		
		writer.write(sample.getBytes()).get();		
		name = writer.save(cipherOpt).get();		
		reader = client.immutableData().getReader(name).get();		
		long size = reader.getSize().get();		
		data = reader.read(0, size).get();		
		assertEquals(sample, new String(data));
	}

	public void testReadAndWriteAsSymetric() throws Exception {
		ImmutableDataWriter writer;
		ImmutableDataReader reader;
		CipherOpt cipherOpt;
		XorName name;
		SafeClient client;
		byte[] data;
		String sample = "sample data 2" + Math.random();
		System.out.println(sample);

		client = Utils.getTestAppWithAccess();

		writer = client.immutableData().getWriter().get();
		cipherOpt = client.cipherOpt().getSymmetric().get();
		System.out.println("Writing");
		writer.write(sample.getBytes()).get();
		System.out.println("Wrote");
		name = writer.save(cipherOpt).get();

		reader = client.immutableData().getReader(name).get();
		data = reader.read(0, reader.getSize().get()).get();

		assertEquals(sample, new String(data));
	}

	public void testReadAndWriteAsAsymetric() throws Exception {
		ImmutableDataWriter writer;
		ImmutableDataReader reader;
		CipherOpt cipherOpt;
		XorName name;
		SafeClient client;
		byte[] data;
		String sample = "sample data 3" + Math.random();

		client = Utils.getTestAppWithAccess();

		writer = client.immutableData().getWriter().get();
		cipherOpt = client.cipherOpt()
				.getAsymmetric(client.crypto().getAppPublicEncryptKey().get())
				.get();
		writer.write(sample.getBytes()).get();
		name = writer.save(cipherOpt).get();

		reader = client.immutableData().getReader(name).get();
		data = reader.read(0, reader.getSize().get()).get();

		assertEquals(sample, new String(data));
	}

}
