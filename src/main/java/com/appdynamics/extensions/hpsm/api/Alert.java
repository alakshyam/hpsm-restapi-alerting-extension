package com.appdynamics.extensions.hpsm.api;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Alert {

	@JsonProperty("Title")
	private String title;
	
	@JsonProperty("Description")
    private ArrayList<String> description;

    @JsonProperty("JournalUpdates")
    private ArrayList<String> journalUpdates;

    @JsonProperty("Impact")
    private String impact;

    @JsonProperty("Urgency")
    private String urgency;

    @JsonProperty("Status")
    private String status;
    
    @JsonProperty("Phase")
    private String phase;

    @JsonProperty("ClosureCode")
    private String closureCode;

    @JsonProperty("Solution")
    private ArrayList<String> solution;

    private Map<String, String> dynamicProperties = new HashMap<String, String>();

    public String getTitle() {
		return title;
	}

	public ArrayList<String> getDescription() {
		return description;
	}

	public ArrayList<String> getJournalUpdates() {
		return journalUpdates;
	}

	public String getImpact() {
		return impact;
	}

	public String getUrgency() {
		return urgency;
	}

	public String getStatus() {
		return status;
	}

	public String getPhase() {
		return phase;
	}

	public String getClosureCode() {
		return closureCode;
	}

	public ArrayList<String> getSolution() {
		return solution;
	}

	@JsonAnyGetter
	public Map<String, String> getDynamicProperties() {
		return dynamicProperties;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(ArrayList<String> description) {
		this.description = description;
	}

	public void setJournalUpdates(ArrayList<String> journalUpdates) {
		this.journalUpdates = journalUpdates;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public void setClosureCode(String closureCode) {
		this.closureCode = closureCode;
	}

	public void setSolution(ArrayList<String> solution) {
		this.solution = solution;
	}

	public void setDynamicProperties(Map<String, String> dynamicProperties) {
		this.dynamicProperties = dynamicProperties;
	}

	public void addDynamicProperties(String name, String value) {
        if (dynamicProperties == null) {
            dynamicProperties = new HashMap<String, String>();
        }

        if (value != null && value.length() > 0) {
            dynamicProperties.put(name, value);
        }
    }
}
