package Kinect;

public class Gesture {
	private String name;
	private String context;
	private String permission;
	
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
	
	
}
