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
	
	private String getStringFieldFromResponse(String key, String response) {
		JsonObject rdr = Json.createReader(new StringReader(response)).readObject();
		return rdr.getString(key);
	}
	
	private String getStringResultFromResponse(String response) {
		JsonObject rdr = Json.createReader(new StringReader(response)).readObject();
		return rdr.getString("result");
	}
	
	private int getNumberResultFromResponse(String response) {
		JsonObject rdr = Json.createReader(new StringReader(response)).readObject();
		return rdr.getInt("result");
	}
	
	public String getResponseContent(CloseableHttpResponse response) throws UnsupportedOperationException, IOException {
		InputStream responseContent = response.getEntity().getContent();
		String message = new BufferedReader(new InputStreamReader(responseContent))
				   .lines().collect(Collectors.joining("\n"));
		
		return message;
	}
	
	public CloseableHttpResponse executePost(String endpoint, String contentType, HttpEntity entity) throws ClientProtocolException, IOException {
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
	
	public CloseableHttpResponse executeGet(String endpoint) throws ClientProtocolException, IOException {
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
			throw new Exception(getStringFieldFromResponse("message", content));
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
			throw new Exception(getStringFieldFromResponse("message", content));
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
			throw new Exception(getStringFieldFromResponse("message", content));
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
			throw new Exception(getStringFieldFromResponse("message", content));
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
			throw new Exception(getStringFieldFromResponse("message", content));
		}
		return getNumberResultFromResponse(content);
	}
	
	/**
	 * Returns the status of the pins as a JSON object, it is recommended to create a class to represent the pins when used in production
	 * @return
	 */
	public JsonArray getPinsStatus() {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executeGet("/pins_status");
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			//catch the error of incorrect output
		}
		return Json.createReader(new StringReader(content)).readArray();
	}
	
	public int getFormat() {
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = executeGet("/format");
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			//catch the error of incorrect output
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
			//catch the error of incorrect output
		}
		return getStringResultFromResponse(content);
	}
	

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
			throw new Exception(getStringFieldFromResponse("message", content));
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
			throw new Exception(getStringFieldFromResponse("message", content));
		}
		return getStringResultFromResponse(content);
	}
	
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
			throw new Exception(getStringFieldFromResponse("message", content));
		}
		System.out.println("Pins up result : " + content);
		return Json.createReader(new StringReader(content)).readArray();
	}
	
	//to do: configure reader for type null
	public JsonObject configureReader(String type) throws Exception {
		CloseableHttpResponse response = null;
		String content = "";
		
		Map<String, String> param = new HashMap<>();
		param.put("type", type);
		
		try {
			String serializedParam = new ObjectMapper().writeValueAsString(param);
			
			response = executePost("/reset_pins", "application/json", new StringEntity(serializedParam));
			content = getResponseContent(response);
		} catch (IOException e) {
			//catch exception with some logs
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(getStringFieldFromResponse("message", content));
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
			throw new Exception(getStringFieldFromResponse("message", content));
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
			throw new Exception(getStringFieldFromResponse("message", content));
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
			throw new Exception(getStringFieldFromResponse("message", content));
		}
		

		System.out.println("Load worklist result : " + content);
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
			throw new Exception(getStringFieldFromResponse("message", content));
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
			throw new Exception(getStringFieldFromResponse("message", content));
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
			throw new Exception(getStringFieldFromResponse("message", content));
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
			throw new Exception(getStringFieldFromResponse("message", content));
		}
		return getStringResultFromResponse(content);
	}
	
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
			throw new Exception(getStringFieldFromResponse("message", content));
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
			throw new Exception(getStringFieldFromResponse("message", getResponseContent(response)));
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
			throw new Exception(getStringFieldFromResponse("message", getResponseContent(response)));
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
			throw new Exception(getStringFieldFromResponse("message", getResponseContent(response)));
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
			throw new Exception(getStringFieldFromResponse("message", content));
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
