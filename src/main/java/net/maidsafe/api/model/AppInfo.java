package net.maidsafe.api.model;

public class AppInfo {

	private String id;
	private String name;
	private String scope;
	private String vendor;

	public AppInfo(String id, String name, String vendor) throws Exception {
		if (id == null || name == null || vendor == null || id.isEmpty()
				|| name.isEmpty() || vendor.isEmpty()) {
			throw new Exception("id, name, vendor can not be null or empty");
		}
		this.id = id;
		this.name = name;
		this.vendor = vendor;
	}

	public AppInfo(String id, String name, String vendor, String scope)
			throws Exception {
		this(id, name, vendor);
		this.scope = scope;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getScope() {
		return scope;
	}

	public String getVendor() {
		return vendor;
	}

}
