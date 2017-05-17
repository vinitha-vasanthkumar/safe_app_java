package net.maidsafe.api.model;

import net.maidsafe.binding.model.AccessContInfo;

public class AccessContainerMeta {

	private byte[] id;
	// Type tag
	private long tag;
	// Nonce
	private byte[] nonce;
	
	public AccessContainerMeta(AccessContInfo info) {
		id = info.id;
		tag = info.tag;
		nonce = info.nonce;
	}

	public byte[] getId() {
		return id;
	}

	public long getTag() {
		return tag;
	}

	public byte[] getNonce() {
		return nonce;
	}
		
}
