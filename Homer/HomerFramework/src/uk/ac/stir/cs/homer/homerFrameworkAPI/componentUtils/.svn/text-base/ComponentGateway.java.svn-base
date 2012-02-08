package uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils;

public interface ComponentGateway {
	
	public static class Singleton {
		static ComponentGateway singleton;
		
		public static ComponentGateway get() {
			if(singleton == null) {
				throw new IllegalStateException("not initialized yet!");
			}
			return singleton;
		}
		
		public static void set(ComponentGateway s) {
			singleton = s;
		}
	}
	
	void registerComponent(HomerComponent component);
	void triggerOccured(String sysDeviceId, String triggerId, String[] paramData);
	void updateState(String systemDeviceID, String conditionIDofNewState, String[] parameters);
}