package net.maidsafe.binding.model;

import com.sun.jna.Structure;
import net.maidsafe.api.model.AppInfo;

import java.util.Arrays;
import java.util.List;

public class AppExchangeInfo extends Structure {

	public String id;
	public String scope;
	public String name;
	public String vendor;
	
	public AppExchangeInfo() {}
	
	public AppExchangeInfo(AppInfo info) {
		id = info.getId();
		name = info.getName();
		scope = info.getScope();
		vendor = info.getVendor();		
	}
	
	@Override
	protected List<String> getFieldOrder() {		
		return Arrays.asList("id", "scope", "name", "vendor");
	}
		
}
