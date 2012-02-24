package com.sepiroth.ooha.kinect.gesture;

import uk.ac.stir.cs.homer.homerFrameworkAPI.systemUtils.SystemGateway;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Action;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDevice;

public class Gesture {
	private String name;
	private String context;
	private String permission;
	private Action action;
	private UserDevice device;
	
	public Gesture(String name, String context){
		this.setName(name);
		this.setContext(context);
		this.setPermission("ALL");
	}

	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * @return the permission
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * @param permission the permission to set
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		return getName()+" | "+getContext()+" | "+getPermission();
	}
	
	public boolean equals(Gesture b){
		return this.name.equals(b.getName()) && this.context.equals(b.getContext());
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public UserDevice getDevice() {
		return device;
	}

	public void setDevice(UserDevice device) {
		this.device = device;
	}
	
	public void triggerAction(){
		SystemGateway.Singleton.get().doAction(device.getId(),action.getId(),null);
	}
	
	
}
