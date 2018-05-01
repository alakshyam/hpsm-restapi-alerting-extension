package com.appdynamics.extensions.hpsm;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.appdynamics.extensions.alerts.customevents.EvaluationEntity;
import com.appdynamics.extensions.alerts.customevents.HealthRuleViolationEvent;
import com.appdynamics.extensions.alerts.customevents.TriggerCondition;
import com.appdynamics.extensions.hpsm.api.Alert;
import com.appdynamics.extensions.hpsm.common.Configuration;
import com.appdynamics.extensions.hpsm.common.Field;
import com.appdynamics.extensions.hpsm.common.FileSystemStore;
import com.appdynamics.extensions.hpsm.common.HttpHandler;
import com.appdynamics.extensions.hpsm.common.Service;
import com.google.common.base.Strings;

public class HealthRuleViolationEventExtension {

	private static final String NEW_LINE = "\n";
    private static final String SPACE = " ";
    private Configuration config;
    
    public static final String POLICY_CLOSE = "POLICY_CLOSE";
    public static final String POLICY_CANCELED = "POLICY_CANCELED";
    
    private static Logger logger = Logger.getLogger(HealthRuleViolationEventExtension.class);
    		
    public HealthRuleViolationEventExtension(Configuration config){
    	this.config = config;
    }
    
    public boolean processAnEvent(HealthRuleViolationEvent violationEvent){
    	Alert alert = buildAlert(violationEvent);

        String incidentID = violationEvent.getIncidentID();
        String hpsmSysId = FileSystemStore.INSTANCE.getFromStore(incidentID);
                        
        HttpHandler handler = new HttpHandler(config);

        String eventType = violationEvent.getEventType();
        boolean closeEvent = shouldResolveEvent(eventType);
        
        if (Strings.isNullOrEmpty(hpsmSysId)) {
        	if(closeEvent == true){
        		logger.info("Skipping. This is a Policy Close event with no existing mapped HPSM incident in the FileStore.");
        		return true;
        	}
        	else {
        		return handler.postAlert(alert, incidentID);
        	}
        } 
        else {
        	boolean resolveEvent = false;
        	if (closeEvent == true){
            	
        		//Resolve the incident first.
        		resolveEvent = true;
	            closeEvent = false;
	            boolean resolveStatus = handler.updateAlert(alert, hpsmSysId, closeEvent, resolveEvent);
	            if (resolveStatus == true){
	            	
	            	//Now close the incident
	            	resolveEvent = false;
	            	closeEvent = true;
	            	HttpHandler handler2 = new HttpHandler(config);
	            	return handler2.updateAlert(alert, hpsmSysId, closeEvent, resolveEvent);
	            }
	            else
	            	return resolveStatus;
            }
            else
            	return handler.updateAlert(alert, hpsmSysId, closeEvent,resolveEvent);
        }
    	
    }
    
    private Alert buildAlert(HealthRuleViolationEvent violationEvent) {
    	String comments = buildSummery(violationEvent);
        String shortDescription = buildShortDescription(violationEvent);
        ArrayList<String> description = new ArrayList<String>();
        description.add(comments);
        
        Alert alert = new Alert();
        alert.setImpact(getImpact(violationEvent));
        alert.setUrgency(getUrgency(violationEvent));
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

        Boolean foundService = false;
        Service defaultService = new Service();
        List<Service> configServices = config.getServices();
        if (configServices != null) {
        	for (Service service : configServices){
        		if (service.getName().contentEquals(violationEvent.getAppName())){
        			alert.addDynamicProperties("Service", service.getValue());
        			foundService = true;
        		}
        		if (service.getName().contentEquals("DEFAULT")){
        			foundService = false;
        			defaultService.setName("Service");
        			defaultService.setValue(service.getValue());
        		}
        	}
        }
        
        if(foundService == false){
        	alert.addDynamicProperties(defaultService.getName(), defaultService.getValue());
        }
        
        return alert;
    }
    
        
    private String buildShortDescription(HealthRuleViolationEvent violationEvent) {
        StringBuilder sb = new StringBuilder("AppDynamics Policy");
        sb.append(SPACE).append(violationEvent.getHealthRuleName()).append(SPACE).append("for")
                .append(SPACE).append(violationEvent.getAffectedEntityName()).append(SPACE).append("violated");
        return sb.toString();
    }
    
    private String getImpact(HealthRuleViolationEvent violationEvent) {
        String severity = violationEvent.getSeverity();
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
    
    private String getUrgency(HealthRuleViolationEvent violationEvent) {
    	String priority = violationEvent.getPriority();
    	Integer priorityInt = Integer.parseInt(priority);
    	if (priorityInt > 3)
    		return "4";
    	else
    		return priority;
    	
    }
    
    private String buildSummery(HealthRuleViolationEvent violationEvent) {
        StringBuilder summery = new StringBuilder();
        summery.append("Application Name:").append(violationEvent.getAppName()).append(NEW_LINE);
        summery.append("Policy Violation Alert Time:").append(violationEvent.getPvnAlertTime()).append(NEW_LINE);
        summery.append("Severity:").append(violationEvent.getSeverity()).append(NEW_LINE);
        summery.append("Name of Violated Policy:").append(violationEvent.getHealthRuleName()).append(NEW_LINE);
        summery.append("Affected Entity Type:").append(violationEvent.getAffectedEntityType()).append(NEW_LINE);
        summery.append("Name of Affected Entity:").append(violationEvent.getAffectedEntityName()).append(NEW_LINE);

        List<EvaluationEntity> evaluationEntities = violationEvent.getEvaluationEntity();
        for (int i = 0; i < evaluationEntities.size(); i++) {
            EvaluationEntity evaluationEntity = evaluationEntities.get(i);
            summery.append("EVALUATION ENTITY #").append(i + 1).append(":").append(NEW_LINE);
            summery.append("Evaluation Entity:").append(evaluationEntity.getType()).append(NEW_LINE);
            summery.append("Evaluation Entity Name:").append(evaluationEntity.getName()).append(NEW_LINE);

            List<TriggerCondition> triggeredConditions = evaluationEntity.getTriggeredConditions();
            for (int j = 0; j < triggeredConditions.size(); j++) {
                TriggerCondition triggerCondition = triggeredConditions.get(j);
                summery.append("Triggered Condition #").append(j + 1).append(":").append(NEW_LINE).append(NEW_LINE);
                summery.append("Scope Type:").append(triggerCondition.getScopeType()).append(NEW_LINE);
                summery.append("Scope Name:").append(triggerCondition.getScopeName()).append(NEW_LINE);

                if (triggerCondition.getConditionUnitType() != null && triggerCondition.getConditionUnitType().toUpperCase().startsWith("BASELINE")) {
                    summery.append("Is Default Baseline?").append(triggerCondition.isUseDefaultBaseline() ? "true" : "false").append(NEW_LINE);
                    if (!triggerCondition.isUseDefaultBaseline()) {
                        summery.append("Baseline Name:").append(triggerCondition.getBaselineName()).append(NEW_LINE);
                    }
                }
                summery.append(triggerCondition.getConditionName()).append(triggerCondition.getOperator()).append(triggerCondition.getThresholdValue()).append(NEW_LINE);
                summery.append("Violation Value:").append(triggerCondition.getObservedValue()).append(NEW_LINE).append(NEW_LINE);
                summery.append("Incident URL:").append(violationEvent.getIncidentUrl());
            }
        }
        return summery.toString();
    }
    
    private boolean shouldResolveEvent(String eventType) {
    	return eventType != null && (eventType.startsWith(POLICY_CLOSE) || eventType.startsWith(POLICY_CANCELED));
    }
}
