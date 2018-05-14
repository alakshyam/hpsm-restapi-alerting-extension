# AppDynamics HP Service Manager REST API Alerting Extension

## Use Case
HP Service Manager ([HPSM](https://software.microfocus.com/en-us/products/service-management-automation-suite/overview)) now part of [MicroFocus](https://software.microfocus.com/en-us/home) is a IT service management (ITSM) Tool using ITIL framework providing a web interface for Corporate changes, releases and interactions supported by a Service Catalog and CMDB. AppDynamics integrates directly with HPSM to create tickets in response to alerts. With the HPSM integration you can leverage your existing incident management infrastructure to notify your operations team to resolve performance degradation issues.

**Note**: This extension works with both AppDynamics Health Rule Violation events and Other Events. Auto close of incidents is only for Health Rule Violations and Other Events incidents are not auto closed. 

## Prerequisites

To post events to HPSM an Operator User should have restful capability. For details refer to [HP Service Manager Web Services](https://docs.microfocus.com/SM/9.41/Codeless/Content/Resources/PDF_PD/HP_Service_Manager_Web_Services_codeless.pdf) document.

## Installation Steps

 1. Run "mvn clean install"

 2. Find the zip file at 'target/hpsm-restapi-alert.zip'.

 3. Unzip the hpsm-restapi-alert.zip file into <CONTROLLER_HOME_DIR>/custom/actions/ . You should have  <CONTROLLER_HOME_DIR>/custom/actions/hpsm-restapi-alert created.

 4. Check if you have custom.xml file in <CONTROLLER_HOME_DIR>/custom/actions/ directory. If yes, add the following xml to the <custom-actions> element.
	```
   	<action>
		<type>hpsm-restapi-alert</type>
		<!-- For Linux/Unix *.sh -->
		<executable>hpsm-alert.sh</executable>
		<!-- For windows *.bat -->
		<!--<executable>hpsm-alert.bat</executable>-->
	</action>
  	```
	```
	<!-- 
		If you don't have custom.xml already, create one with the below xml content.
		Uncomment the appropriate executable tag based on windows or linux/unix machine.
	-->
	<custom-actions>
   		<action>
       	<type>hpsm-restapi-alert</type>
       	<!-- For Linux/Unix *.sh -->
          <executable>hpsm-alert.sh</executable>
          <!-- For windows *.bat -->
          <!--<executable>hpsm-alert.bat</executable>-->
       </action>
    </custom-actions>
    ```

 5. Update the config.yml file in <CONTROLLER_HOME_DIR>/custom/actions/hpsm-restapi-alert/conf/ directory with the rest URL, username, password and HPSMVersion. The rest url is different from SOAP URL for HP Service Manager and is new addition. This integration is compatible only with Rest URL. You can also configure the default HPSM fields like AssignmentGroup, Assignee, Area, company, category. If you have specific fields in HPSM which have values dependent on AppDynamics Applications such AssignmentGroup, Service, etc, then create a mapping csv file and place it in conf folder. Mention the csv file path name in config.yml. The csv file should have the first column as Application. There should not be duplicate column names in the csv file.

 6. Check if the version of java in your environment is 1.8 and above. If not download the latest 1.8 Java and provide the path in the hpsm-alert.bat/hpsm-alert.sh as applicable 

## Configuration

### Note
Please make sure to not use tab (\t) while editing yaml files. You may want to validate the yaml file using a yaml validator http://yamllint.com/


```
	#HPSM Rest URL
	url: "http://<HPSM Hostname>:<REST Service Port>/SM/9/rest/incidents"
	
	#HPSM Rest User (Basic Authentication)
	username: "UserName"
	
	#HPSM Password, provide password or passwordEncrypted and encryptionKey.
	password:
	
	passwordEncrypted: "EncryptypedPassword"
	encryptionKey: "KEY"
	
	#HPSM Version
	hpsmVersion: "9.41"
	
	#Close notes text to be posted when resolving the incident
	closeNotesText: "This incident is resolved"
	
	#Proxy server URI
	proxyUri:
	#Proxy server user name
	proxyUser:
	#Proxy server password
	proxyPassword:
	
	#HPSM User Specific
	#Please define your HPSM specific fields here like AssignmentGroup, Assignee, Area, Category and Company.
	#The name should be a valid Incident table's column name.
	#Do not add properties Title, Description, JournalUpdates, Impact and Urgency here, as they are added by the extension automatically.
	#If you have mapping of service and assignment group to AppD applications, then add them to CSV file and provide path in applicationFieldMapping
	fields:
	   - name: "Assignee"
	     value: "test.user"
	   - name: "Area"
	     value: "Automated Ticketing"
	   - name: "Company"
	     value: "AppDynamics"
	   - name: "Category"
	     value: "incident"
	
	#If you have application specific field mappings, such as service or AssignmentGroup, provide the filename. If not leave blank.
	applicationFieldMapping: "appFields.csv"

```

### Credentials Encryption
Please visit [this page](https://community.appdynamics.com/t5/Knowledge-Base/How-to-use-Password-Encryption-with-Extensions/ta-p/29397) to get detailed instructions on password encryption. The steps in this document will guide you through the whole process.

## HPSM Ticket
Open Ticket

![Alt text](openTicket.png?raw=true "OpenTicket")

Categorization

![Alt text](categorize.png?raw=true "categorizationAndAssignment")

Ticket Activity

![Alt text](pastActivities.png?raw=true "pastActivities")

Resolve Ticket

![Alt text](resolveStatus.png?raw=true "resolveStatus")

Close Ticket

![Alt text](closedTicket.png?raw=true "ClosedTicket")


## Support
For any questions or changes or feature request, please contact [Alakshya M](mailto:alakshya.m@gmail.com).

### Note: 
This alerting extension is based on the existing [ServiceNow API Alerting Extension](https://github.com/Appdynamics/servicenow-api-alerting-extension). Thanks to the authors of the extension!
