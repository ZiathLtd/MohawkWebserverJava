package MohawkWebserverJava.MohawkWebserverJava;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

public class MohawkWebserverCore {
	private String host;
	private int port;
	private final String REMOTE_STUB = "/mohawk/api/v1";
	private final String EXCEL_MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	public MohawkWebserverCore(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	private String urlStub() {
		return String.format("http://%s:%s" + REMOTE_STUB, host, port);
	}
	
	/**
	 * Returns the string value from the message field in the http response.
	 * This is used for exceptions coming from the web server.
	 * 
	 * The error object also contains the fields: error, path, timestamp
	 * 
	 * @param response
	 * @return
	 */
	private String getErrorMessageFromResponse(String response) {
		JsonObject error = Json.createReader(new StringReader(response)).readObject();
		return error.getString("message");
	}
	
	/**
	 * Returns the string value from the 'result' field in the http response
	 * 
	 * @param response
	 * @return
	 */
	private String getStringResultFromResponse(String response) {
		JsonObject result = Json.createReader(new StringReader(response)).readObject();
		return result.getString("result");
	}
	
	/**
	 * Returns the integer value from the 'result' field in the http response
	 * 
	 * @param response
	 * @return
	 */
	private int getNumberResultFromResponse(String response) {
		JsonObject result = Json.createReader(new StringReader(response)).readObject();
		return result.getInt("result");
	}
	
	/**
	 * Return the content of the http response as a string
	 * 
	 * @param response
	 * @return message
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	private String getResponseContent(CloseableHttpResponse response) throws UnsupportedOperationException, IOException {
		InputStream responseContent = response.getEntity().getContent();
		String message = new BufferedReader(new InputStreamReader(responseContent))
				   .lines().collect(Collectors.joining("\n"));
		
		return message;
	}
	
	/**
	 * Execute http post request to given endpoint.
	 * Adds the given content type to the request header and 
	 * the given entity to the request body if the entity is not null.
	 * 
	 * Returns the response of the http request.
	 * 
	 * @param endpoint
	 * @param contentType 
	 * @param entity
	 * @return response
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private CloseableHttpResponse executePost(String endpoint, String contentType, HttpEntity entity) throws ClientProtocolException, IOException {
	    CloseableHttpClient client = HttpClients.createDefault();
	    HttpPost httpPost = new HttpPost(urlStub() + endpoint);
	    if (entity != null) {
		    httpPost.setEntity(entity);
		    httpPost.setHeader("content-type", contentType);
	    }
	    
		CloseableHttpResponse response = client.execute(httpPost);
		client.close();
		return response;
	}
	
	/**
	 * Execute http get request to given endpoint.
	 * 
	 * Returns the response of the http request.
	 * 
	 * @param endpoint
	 * @return response
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private CloseableHttpResponse executeGet(String endpoint) throws ClientProtocolException, IOException {
	    CloseableHttpClient client = HttpClients.createDefault();
	    HttpGet httpGet = new HttpGet(urlStub() + endpoint);
	    
		CloseableHttpResponse response = client.execute(httpGet);
		client.close();
		return response;
	}
	
	public String getVersion() throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executeGet("/version");
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		return getStringResultFromResponse(content);
	}
	
	public String getLidStatus() throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executeGet("/lid_status");
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		return getStringResultFromResponse(content);
	}
	
	public String getMohawkStatus() throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executeGet("/mohawk_status");
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		return getStringResultFromResponse(content);
	}
	
	public int getFanSpeed() throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executeGet("/fan_speed");
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		return getNumberResultFromResponse(content);
	}
	
	public int getTemperature() throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executeGet("/temperature");
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		return getNumberResultFromResponse(content);
	}
	
	/**
	 * Returns the status of the pins as a JSON Array.
	 * It is recommended to create a class to represent the pins when used in production
	 * @return
	 * @throws Exception 
	 */
	public JsonArray getPinsStatus() throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executeGet("/pins_status");
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		return Json.createReader(new StringReader(content)).readArray();
	}
	
	public int getFormat() throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executeGet("/format");
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		return getNumberResultFromResponse(content);
	}
	
	public String getWorklistStatus() throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executeGet("/worklist/status");
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		return getStringResultFromResponse(content);
	}
	
	/**
	 * Returns the worklist as a JSON Object.
	 * It is recommended to create a class to represent the worklist when used in production.
	 * @return
	 */
	public JsonObject getWorklistProgress() throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executeGet("/worklist");
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		
		return Json.createReader(new StringReader(content)).readObject();
	}
	
	public String resetPins() throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executePost("/reset_pins", null, null);
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		return getStringResultFromResponse(content);
	}
	
	/**
	 * Returns the status of the pins as a JSON Array.
	 * It is recommended to create a class to represent the pins when used in production.
	 * @return
	 */
	public JsonArray pinsUp(List<Object> parameters) throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		
		try {
			String serializedParameters = new ObjectMapper().writeValueAsString(parameters);
			
			response = executePost("/pins_up", "application/json", new StringEntity(serializedParameters));
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}

		return Json.createReader(new StringReader(content)).readArray();
	}
	
	/**
	 * Returns the configuration of the reader as a JSON Object.
	 * It is recommended to create a class to represent the configuration when used in production.
	 * @return
	 */
	public JsonObject configureReader(String type) throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		
		String serializedParam;
		if (type == null) {
			serializedParam = new ObjectMapper().writeValueAsString(JsonObject.EMPTY_JSON_OBJECT);
		} else {
			Map<String, String> param = new HashMap<>();
			param.put("type", type);
			serializedParam = new ObjectMapper().writeValueAsString(param);
		}
		
		try {
			response = executePost("/reader", "application/json", new StringEntity(serializedParam));
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		return Json.createReader(new StringReader(content)).readObject();
	}

	public String readBarcode() throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executePost("/read_barcode", null, null);
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		return getStringResultFromResponse(content);
	}
	
	public String loadRack(Map<String, Object> params) throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			String serializedParams = new ObjectMapper().writeValueAsString(params);
			
			response = executePost("/set_rack_barcode", "application/json", new StringEntity(serializedParams));
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		return getStringResultFromResponse(content);
	}
	
	public String loadWorklist(List<Object> parameters) throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			String serializedParams = new ObjectMapper().writeValueAsString(parameters);
			
			response = executePost("/worklist/load_json", "application/json", new StringEntity(serializedParams));
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		
		return getStringResultFromResponse(content);
	}
	
	public String finishWorklist() throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executePost("/worklist/finish", null, null);
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		return getStringResultFromResponse(content);
	}
	
	public String cancelWorklist() throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executePost("/worklist/cancel", null, null);
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		return getStringResultFromResponse(content);
	}
	
	public String loadExcelWorklist(File worklist) throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executePost("/worklist/load_excel", EXCEL_MEDIA_TYPE, new FileEntity(worklist));
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		return getStringResultFromResponse(content);
	}
	
	public String loadXMLWorklist(File worklist) throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executePost("/worklist/load_xml", "application/xml", new FileEntity(worklist));
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		return getStringResultFromResponse(content);
	}
	
	public String loadCSVWorklist(File worklist) throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executePost("/worklist/load_csv", "text/csv", new FileEntity(worklist));
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		return getStringResultFromResponse(content);
	}
	
	/**
	 * Returns the report as a JSON Object.
	 * It is recommended to create a class to represent the report when used in production.
	 * @return
	 */
	public JsonObject reportToJson() throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executeGet("/report_to_json");
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		
		return Json.createReader(new StringReader(content)).readObject();
	}
	
	public File reportToXML() throws Exception {
		CloseableHttpResponse response = null;
		File report = new File("report.xml");
		try {
			response = executeGet("/report_to_xml");
			
			HttpEntity entity = response.getEntity();
		    if (entity != null) {
		        try (FileOutputStream outstream = new FileOutputStream(report)) {
		            entity.writeTo(outstream);
		        }
		    }
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(getResponseContent(response)));
		}
		
		return report;
	}
	
	public File reportToExcel() throws Exception {
		CloseableHttpResponse response = null;
		File report = new File("report.xlsx");
		try {
			response = executeGet("/report_to_excel");
			
			HttpEntity entity = response.getEntity();
		    if (entity != null) {
		        try (FileOutputStream outstream = new FileOutputStream(report)) {
		            entity.writeTo(outstream);
		        }
		    }
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(getResponseContent(response)));
		}
		
		return report;
	}
	
	public File reportToCSV() throws Exception {
		CloseableHttpResponse response = null;
		File report = new File("report.csv");
		try {
			response = executeGet("/report_to_csv");
			
			HttpEntity entity = response.getEntity();
		    if (entity != null) {
		        try (FileOutputStream outstream = new FileOutputStream(report)) {
		            entity.writeTo(outstream);
		        }
		    }
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(getResponseContent(response)));
		}
		
		return report;
	}
	
	public String shutdown() throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executePost("/shutdown", null, null);
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getErrorMessageFromResponse(content));
		}
		return getStringResultFromResponse(content);
	}
	
	public void forceShutdown() {
		try {
			executePost("/force_shutdown", null, null);
		} catch (IOException e) {
			//catch exception with some logs
		}
	}
}
