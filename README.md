# AppDynamics HP Service Manager REST API Alerting Extension

## Use Case
HP Service Manager (HPSM) now part of MicroFocus ([https://software.microfocus.com/en-us/products/service-management-automation-suite/overview](https://software.microfocus.com/en-us/home)) is a IT service management (ITSM) Tool using ITIL framework providing a web interface for Corporate changes, releases and interactions supported by a Service Catalog and CMDB. AppDynamics integrates directly with HPSM to create tickets in response to alerts. With the HPSM integration you can leverage your existing incident management infrastructure to notify your operations team to resolve performance degradation issues.

**Note**: This extension works with both AppDynamics Health Rule Violation events and Other Events. Auto close of incidents is only for Health Rule Voilations and Other Events incidents are not auto closed. 

## Prerequisites

To post events to HPSM an Operator User should have restful capability. For details refer to HP Service Manager Web Services (https://docs.microfocus.com/SM/9.41/Codeless/Content/Resources/PDF_PD/HP_Service_Manager_Web_Services_codeless.pdf)

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

   If you don't have custom.xml already, create one with the below xml content

      ```
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
      Uncomment the appropriate executable tag based on windows or linux/unix machine.

    5. Update the config.yaml file in <CONTROLLER_HOME_DIR>/custom/actions/hpsm-restapi-alert/conf/ directory with the domain, username, password and HPSMVersion. You can also configure the default HPSM fields like AssignmentGroup, Assignee, Area, company, category. The services mapping is provided for applications in AppDynamics. If there is no specific service mapping, use default service value.

 6. Check if the version of java in your environment is 1.8 and above. If not download the latest 1.8 Java and provide the path in the hpsm-alert.bat/hpsm-alert.sh as applicable 

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
	#Do not add properties Title, Description, JournalUpdates, Impact and Urgency here, as they are added by the extension automatically
	fields:
	   - name: "AssignmentGroup"
	     value: "Service Desk"
	   - name: "Assignee"
	     value: "test.user"
	   - name: "Area"
	     value: "Automated Ticketing"
	   - name: "Company"
	     value: "AppDynamics"
	   - name: "Category"
	     value: "incident"
	
	#HPSM service mapping to AppDynamics Applications
	#Name is AppDynamics Application, Value is corresponding HPSM Service CI
	#Keep a default value of CI if you have so and remove all other mappings
	services:
	   - name: "AppdAppName1"
	     value: "CI16108"
	   - name: "AppdAppName2"
	     value: "CI15448"
	   - name: "AppdAppName3"
	     value: "CI16107"
	   - name: "AppdAppName4"
	     value: "CI17290"
	   - name: "AppdAppName5"
	     value: "CI17291"
	   - name: "AppdAppName6"
	     value: "CI17048"
	   - name: "AppdAppName7"
	     value: "CI16106"
	   - name: "AppdAppName8"
	     value: "CI16099"
	   - name: "DEFAULT"
	     value: "CI16108"

```

### Password Encryption
To set passwordEncrypted in config.yaml, follow the steps below:

1. Download the util jar to encrypt the password from [here](https://github.com/Appdynamics/maven-repo/blob/master/releases/com/appdynamics/appd-exts-commons/1.1.2/appd-exts-commons-1.1.2.jar).
2. Run command:

   	~~~   
   	java -cp appd-exts-commons-1.1.2.jar com.appdynamics.extensions.crypto.Encryptor EncryptionKey CredentialToEncrypt
   	
   	For example: 
   	java -cp "appd-exts-commons-1.1.2.jar" com.appdynamics.extensions.crypto.Encryptor test password
   
   	~~~
   	
3. Set the resulting encrypted password in passwordEncrypted field and the encryption key used in encryptionKey field in config.yaml file

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
This alerting extension is based on the existing https://github.com/Appdynamics/servicenow-api-alerting-extension. Thanks to the authors of the extension!
