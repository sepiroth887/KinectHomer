package uk.ac.stir.cs.homer.userInterfaceJavaClient.log;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import uk.ac.stir.cs.homer.homerFrameworkAPI.events.EventUtils;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase; 
import uk.ac.stir.cs.homer.serviceDatabase.objects.Log;

public class EventLogFrame extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JTextArea allTextArea;
	private JTextArea triggerTextArea;
	private JTextArea actionTextArea;
	private JTextArea stateTextArea;
	
	private EventHandler triggerEventHandler;
	private EventHandler actionEventHandler;
	private EventHandler stateEventHandler;
	
	private boolean filterEvents = false;
	
	public EventLogFrame(BundleContext context, HomerDatabase database) {
		initaliseGUI();
		registerListeners(context, database);
	}

	private void initaliseGUI() {
		
		
		allTextArea = new JTextArea(500, 500);
		allTextArea.setWrapStyleWord(true);
		allTextArea.setLineWrap(true);
		JScrollPane allTextAreaScrollPane = new JScrollPane(allTextArea);
		allTextAreaScrollPane.setAutoscrolls(true);
		allTextAreaScrollPane.setWheelScrollingEnabled(true);
		
		triggerTextArea = new JTextArea(500, 500);
		triggerTextArea.setWrapStyleWord(true);
		triggerTextArea.setLineWrap(true);
		JScrollPane triggerTextAreaScrollPane = new JScrollPane(triggerTextArea);
		triggerTextAreaScrollPane.setAutoscrolls(true);
		triggerTextAreaScrollPane.setWheelScrollingEnabled(true);
		
		stateTextArea = new JTextArea(500, 500);
		stateTextArea.setWrapStyleWord(true);
		stateTextArea.setLineWrap(true);
		JScrollPane stateTextAreaScrollPane = new JScrollPane(stateTextArea);
		stateTextAreaScrollPane.setAutoscrolls(true);
		stateTextAreaScrollPane.setWheelScrollingEnabled(true);
		
		actionTextArea = new JTextArea(500, 500);
		actionTextArea.setWrapStyleWord(true);
		actionTextArea.setLineWrap(true);
		JScrollPane actionTextAreaScrollPane = new JScrollPane(actionTextArea);
		actionTextAreaScrollPane.setAutoscrolls(true);
		actionTextAreaScrollPane.setWheelScrollingEnabled(true);
		
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("All", allTextAreaScrollPane);
		tabbedPane.addTab("Triggers", triggerTextAreaScrollPane);
		tabbedPane.addTab("State Changes", stateTextAreaScrollPane);
		tabbedPane.addTab("Actions", actionTextAreaScrollPane);
		
		final JCheckBox checkBox = new JCheckBox();
		checkBox.setText("Filter Events");
		checkBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				filterEvents = checkBox.isSelected();
			}
		});
		
		this.setLayout(new BorderLayout());
		this.add(checkBox, BorderLayout.NORTH);
		this.add(tabbedPane, BorderLayout.CENTER);
	}
	
	private void registerListeners(BundleContext context, final HomerDatabase database) {
		
		triggerEventHandler = new EventHandler(){
			public void handleEvent(Event event) {
				Log l = EventUtils.getLog(event, database);
				if ((!filterEvents) || (filterEvents && l.isDisplay()))
				{
					allTextArea.append(l.getUserFriendly() + "\n\n");
					triggerTextArea.append(l.getUserFriendly() + "\n\n");
				}
			}
		};
		stateEventHandler = new EventHandler(){
			public void handleEvent(Event event) {
				Log l = EventUtils.getLog(event, database);
				if ((!filterEvents) || (filterEvents && l.isDisplay()))
				{
					allTextArea.append(l.getUserFriendly() + "\n\n");
					stateTextArea.append(l.getUserFriendly() + "\n\n");
				}
			}
		};
		actionEventHandler = new EventHandler(){
			public void handleEvent(Event event) {
				Log l = EventUtils.getLog(event, database);
				if ((!filterEvents) || (filterEvents && l.isDisplay()))
				{
					allTextArea.append(l.getUserFriendly() + "\n\n");
					actionTextArea.append(l.getUserFriendly() + "\n\n");
				}
			}
		};
		EventUtils.registerTriggerListener(context, triggerEventHandler, null, null, null, null, null, null, null);
		EventUtils.registerStateChangeListener(context, stateEventHandler, null, null, null, null, null, null, null);
		EventUtils.registerActionListener(context, actionEventHandler, null, null, null, null, null, null, null);
	}
}
