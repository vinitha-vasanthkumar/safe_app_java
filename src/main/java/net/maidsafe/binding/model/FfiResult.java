package net.maidsafe.binding.model;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class FfiResult extends Structure {
	public int error_code;
	public String description;
	
	@Override
	protected List<String> getFieldOrder() {	
		return Arrays.asList("error_code", "description");
	}
	
	public boolean isError() {
		return this.error_code != 0;
	}
		
	public String errorMessage() {	
		return String.format("Err: %d - %s", this.error_code, this.description);
	}
}
