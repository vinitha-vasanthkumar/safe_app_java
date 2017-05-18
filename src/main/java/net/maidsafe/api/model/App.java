package net.maidsafe.api.model;

import com.sun.jna.Pointer;

public class App {
	private AppInfo appInfo;
	private Keys keys;
	private AccessContainerMeta accessContainerMeta;
	private Pointer appHandle;

	public App(AppInfo info, Keys keys, AccessContainerMeta accessContainerMeta) {
		this.appInfo = info;
		this.keys = keys;
		this.accessContainerMeta = accessContainerMeta;
	}

	public Pointer getAppHandle() {
		return appHandle;
	}

	public void setAppHandle(Pointer appHandle) {
		this.appHandle = appHandle;
	}

	public AppInfo getAppInfo() {
		return appInfo;
	}

	public Keys getKeys() {
		return keys;
	}
	
	public AccessContainerMeta getAccessContainerMeta() {
		return accessContainerMeta;
	}
	
	public boolean isAnonymous() {
		return this.keys == null;
	}
}
