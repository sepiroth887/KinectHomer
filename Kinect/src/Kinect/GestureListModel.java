package Kinect;

import java.util.ArrayList;

import javax.swing.DefaultListModel;

@SuppressWarnings("serial")
public class GestureListModel extends DefaultListModel<Gesture> {

	private ArrayList<Gesture> gestures;
	
	public GestureListModel(String[] gestures){
		if(gestures == null) return;
		
		this.gestures = new ArrayList<Gesture>();
		this.parseGestures(gestures);
	}
	
	public void updateModel(String[] gestures){
		this.gestures.clear();
		this.parseGestures(gestures);
		this.fireContentsChanged(this, 0, this.getSize());
	}
	
	public void updateModel(Gesture g){
		
		int existingIndex = -1;
		
		for(Gesture gest : gestures){
			if(gest.equals(g)){
				existingIndex = gestures.indexOf(gest);
			}
		}
		
		if(existingIndex != -1){
			gestures.remove(existingIndex);
		}
		
		this.gestures.add(g);
		this.fireContentsChanged(this, 0, gestures.size());
	}
	
	private void parseGestures(String[] gestures){
		for(String gesture : gestures){
			String[] comps = gesture.split(";");
			
			//System.out.println(comps.length);
			
			if(comps.length != 2) return;
			
			Gesture gest = new Gesture(comps[0].trim(),comps[1].trim());
			
			this.gestures.add(gest);
			
		}
	}
	
	
	
	@Override
	public Gesture getElementAt(int arg0) {
		// TODO Auto-generated method stub
		return gestures.get(arg0);
	}

	@Override
	public int getSize() {
		
		int size = 0;
		
		if(gestures != null){
			size = gestures.size();
		}
		
		return size;
	}
	
}
