package com.appdynamics.extensions.hpsm.common;

/*import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;*/

//import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class RestHandler {

	/*Client client;
	String getURL = "http://enavvmemdev02.adgm.com:13089/SM/9/rest/incidents/IM68775";

	public RestHandler(){
		client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("alakshya.maheria","Qwe-1234"));
	}
	public static void main(String[] args) {
		RestHandler restHandler = new RestHandler();
		restHandler.getRequest();
	}
	
	public void getRequest(){
		WebResource webResource = client.resource(getURL);
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		if(response.getStatus()!=200){
			throw new RuntimeException("HTTP Error: "+ response.getStatus());
		}
		
		String result = response.getEntity(String.class);
		System.out.println("Response from the Server: ");

		System.out.println(result);

	}*/
	

}
