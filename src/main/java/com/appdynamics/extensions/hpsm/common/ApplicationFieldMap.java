package com.appdynamics.extensions.hpsm.common;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

public class ApplicationFieldMap {
	private String filename;
	
	private static Logger logger = Logger.getLogger(ApplicationFieldMap.class);
	
	public ApplicationFieldMap(String filename){
		this.filename = "." + File.separator + "conf" + File.separator + filename;
	}
	
	public Map<String,String> getRecord(String application){
		
		Map<String, String> fieldMaps = new HashMap<String, String>();
		CSVParser parser = null;
		
		try{
			parser = new CSVParser(new FileReader(this.filename), 
				      CSVFormat.DEFAULT.withHeader());
			for (CSVRecord record : parser){
				String app = record.get("Application");
				if(app.contentEquals(application) == true){
					Map<String,Integer> headers = parser.getHeaderMap();
					for(String headerKey: headers.keySet()){
						if(headerKey.contentEquals("Application")==false){
							fieldMaps.put(headerKey, record.get(headerKey));
						}
					}
					parser.close();
					return fieldMaps;
				}
			}
			logger.error("The application "+application+" mapping is not found in the csv file");
			parser.close();
		}
		catch (Exception e) {
			logger.error("Error reading the ApplicationFieldMap csv file",e);
		}
		
		return null;
		
	}
	
}
