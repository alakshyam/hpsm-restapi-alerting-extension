package com.appdynamics.extensions.hpsm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.appdynamics.extensions.alerts.customevents.EventSummary;
import com.appdynamics.extensions.alerts.customevents.OtherEvent;
import com.appdynamics.extensions.hpsm.api.Alert;
import com.appdynamics.extensions.hpsm.common.ApplicationFieldMap;
import com.appdynamics.extensions.hpsm.common.Configuration;
import com.appdynamics.extensions.hpsm.common.Field;
import com.appdynamics.extensions.hpsm.common.HttpHandler;

public class OtherEventsExtension {

	private static final String NEW_LINE = "\n";
    private static final String SPACE = " ";
    private Configuration config;
    private static Logger logger = Logger.getLogger(OtherEventsExtension.class);
    
    public OtherEventsExtension(Configuration config){
    	this.config = config;
    }
    
    public boolean processAnEvent(OtherEvent event){
    	Alert alert = buildAlert(event);
    	HttpHandler handler = new HttpHandler(config);
    	String incidentID = event.getEventNotificationId();
    	return handler.postAlert(alert, incidentID);
    }
    
	private Alert buildAlert(OtherEvent otherEvent){
    	String comments = buildSummery(otherEvent);
        String shortDescription = buildShortDescription(otherEvent);
        ArrayList<String> description = new ArrayList<String>();
        description.add(comments);
    	
        Alert alert = new Alert();
        alert.setImpact(getImpact(otherEvent));
        alert.setUrgency(getUrgency(otherEvent));
        alert.setTitle(shortDescription);
        alert.setDescription(description);
        alert.setJournalUpdates(description);

        List<Field> fields = config.getFields();

        if (fields != null) {
            for (Field field : fields) {
                if (field.getValue() != null && field.getValue().length() > 0) {
                    alert.addDynamicProperties(field.getName(), field.getValue());
                }
            }
        }

        String csvFileName = config.getApplicationFieldMapping();
        if(csvFileName.contentEquals("") == false){
        	logger.debug("Read Mapping from the csv file "+csvFileName);
        	ApplicationFieldMap applicationFieldMap = new ApplicationFieldMap(csvFileName);
        	Map<String,String> fieldMap = applicationFieldMap.getRecord(otherEvent.getAppName());
        	if (fieldMap != null){
        		for(String fieldKey: fieldMap.keySet()){
        			alert.addDynamicProperties(fieldKey, fieldMap.get(fieldKey));
        			logger.debug("Mapping Keys "+fieldKey+" .. "+fieldMap.get(fieldKey));
            	}
        	}
        	
        }
        
        return alert;
    }
    
    private String buildShortDescription(OtherEvent otherEvent) {
        StringBuilder sb = new StringBuilder("AppDynamics Event");
        sb.append(SPACE).append(otherEvent.getEventNotificationName()).append(SPACE).append("for")
                .append(SPACE).append(otherEvent.getAppName());
        return sb.toString();
    }
    
    private String getImpact(OtherEvent otherEvent){
    	String severity = otherEvent.getSeverity();
        String severityInt = null;
        if ("ERROR".equals(severity)) {
            severityInt = "1";
        } else if ("WARN".equals(severity)) {
            severityInt = "2";
        } else {
            severityInt = "3";
        }
        return severityInt;
    }
    
    private String getUrgency(OtherEvent otherEvent) {
    	String priority = otherEvent.getPriority();
    	Integer priorityInt = Integer.parseInt(priority);
    	if (priorityInt > 3)
    		return "4";
    	else
    		return priority;
    	
    }
    
    private String buildSummery(OtherEvent otherEvent){
    	StringBuilder summery = new StringBuilder();
    	summery.append("Application Name:").append(otherEvent.getAppName()).append(NEW_LINE);
    	summery.append("Alert Time:").append(otherEvent.getEventNotificationTime()).append(NEW_LINE);
        summery.append("Severity:").append(otherEvent.getSeverity()).append(NEW_LINE);
        summery.append("Name of Event:").append(otherEvent.getEventNotificationName()).append(NEW_LINE);
        summery.append("Priority:").append(otherEvent.getPriority()).append(NEW_LINE);
        
        List<EventSummary> eventSummaries = otherEvent.getEventSummaries();
        for (int i = 0; i < eventSummaries.size(); i++) {
        	EventSummary eventSummary = eventSummaries.get(i);
            summery.append("Event Summary #").append(i + 1).append(":").append(NEW_LINE);
            summery.append("Event Summary Type:").append(eventSummary.getEventSummaryType()).append(NEW_LINE);
            summery.append("Event Summary:").append(eventSummary.getEventSummaryString()).append(NEW_LINE);
        }
    	return summery.toString();
    }
}
