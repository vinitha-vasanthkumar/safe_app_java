package net.maidsafe.api.model;

import java.io.Serializable;

import net.maidsafe.utils.FfiConstant;

public class XorName implements Serializable {

	private static final long serialVersionUID = 1L;

	private byte[] name = new byte[FfiConstant.XOR_NAME_LEN];

	public XorName(byte[] name) {
		this.name = name;
	}

	public byte[] getRaw() {
		return name;
	}

}
