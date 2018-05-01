package com.appdynamics.extensions.hpsm.api;

import com.appdynamics.extensions.hpsm.common.Configuration;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class AlertBuilder {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	public static String convertIntoJsonString(Alert alert) {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.writeValueAsString(alert);
        } catch (JsonProcessingException e) {
            throw new DataParsingException("Unable to convert the Alert to JSON", e);
        }
    }

    public static String convertIntoString(Alert alert, Configuration config) {
    	String prefix = "{\"Incident\": ";
    	String suffix = "}";
    	String completeJson = prefix+convertIntoJsonString(alert)+suffix;
        return completeJson;
    }
    
    public static String convertIntoUpdateString(Alert alert, Configuration config, String incidentId, boolean closeEvent, boolean resolveEvent) {
    	String prefix = "{\"Incident\": ";
    	String suffix = "}";
    	String completeJson = prefix+convertIntoJsonUpdateString(alert, config, closeEvent, resolveEvent)+suffix;
    	return completeJson;
    }
    
    private static String convertIntoJsonUpdateString(Alert alert, Configuration config, boolean closeEvent, boolean resolveEvent) {

        alert.setImpact(null);
        alert.setUrgency(null);
        alert.setDescription(null);
        alert.setTitle(null);
        alert.setDescription(null);
        if (resolveEvent) {
        	alert.setClosureCode("Resolved Successfully");
        	alert.setPhase("Review");
        	alert.setStatus("Resolved");
        	ArrayList<String> solution = new ArrayList<String>();
            solution.add(config.getCloseNotesText());
            alert.setSolution(solution);
            alert.setDynamicProperties(null);
        }
        else if(closeEvent) {
        	alert.setClosureCode("Resolved Successfully");
        	alert.setPhase("Closure");
        	alert.setStatus("Closed");
        	ArrayList<String> solution = new ArrayList<String>();
            solution.add(config.getCloseNotesText());
            alert.setSolution(solution);
            alert.setDynamicProperties(null);
        }

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.writeValueAsString(alert);
        } catch (JsonProcessingException e) {
            throw new DataParsingException("Unable to convert the Alert to JSON", e);
        }
    }
}
