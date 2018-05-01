package com.appdynamics.extensions.hpsm;

import com.appdynamics.extensions.alerts.customevents.Event;
import com.appdynamics.extensions.alerts.customevents.EventBuilder;
import com.appdynamics.extensions.alerts.customevents.HealthRuleViolationEvent;
import com.appdynamics.extensions.alerts.customevents.OtherEvent;
import com.appdynamics.extensions.hpsm.common.Configuration;
import com.appdynamics.extensions.yml.YmlReader;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Arrays;

public class HPServiceManagerAlertExtension {

	private static Logger logger = Logger.getLogger(HPServiceManagerAlertExtension.class);
    private static final String CONFIG_FILENAME = "." + File.separator + "conf" + File.separator + "config.yaml";
    private Configuration config;
    
    final EventBuilder eventBuilder = new EventBuilder();
    
    public static void main(String[] args) {
    	
    	try {
    		if (logger.isDebugEnabled()) {
                logger.debug("Args passed are " + Arrays.toString(args));
            }
    		Configuration config = YmlReader.readFromFile(CONFIG_FILENAME, Configuration.class);
    		HPServiceManagerAlertExtension serviceManagerAlertExtension = new HPServiceManagerAlertExtension(config);
    		boolean status = serviceManagerAlertExtension.processAnEvent(args);
    		if (status) {
                logger.info("HPSM Extension completed successfully.");
                return;
            } 
    	}catch (Exception e) {
            logger.error("Error processing an event", e);
        }
        logger.error("HPSM Extension completed with errors");
        System.exit(2);
    }
    
    public HPServiceManagerAlertExtension(Configuration config){
    	String msg = "HPSMAlert Version [" + getImplementationTitle() + "]";
        logger.info(msg);
        this.config = config;
    }
    
    public boolean processAnEvent(String[] args) {
    	Event event = eventBuilder.build(args);
        if (event != null) {
            if (event instanceof HealthRuleViolationEvent) {
                HealthRuleViolationEvent violationEvent = (HealthRuleViolationEvent) event;
                HealthRuleViolationEventExtension healthRuleViolationEventExtension = new HealthRuleViolationEventExtension(config);
                return healthRuleViolationEventExtension.processAnEvent(violationEvent);
            } 
            else if(event instanceof OtherEvent){
            	OtherEvent otherEvent = (OtherEvent)event;
            	OtherEventsExtension otherEventsExtension = new OtherEventsExtension(config);
            	return otherEventsExtension.processAnEvent(otherEvent);
            }
            
            else {
                logger.error("Skipping this event as this is not a valid AppDynamics Event. This works only with Health Voilation Event and OtherEvents.");
            }
        }
        return false;
    }
    
    private String getImplementationTitle() {
        return this.getClass().getPackage().getImplementationTitle();
    }
}
