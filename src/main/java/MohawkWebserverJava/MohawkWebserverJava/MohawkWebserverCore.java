package MohawkWebserverJava.MohawkWebserverJava;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

public class MohawkWebserverCore {
	private String host;
	private int port;
	private HttpClient httpClient;
	private final String REMOTE_STUB = "/mohawk/api/v1";
	private final String EXCEL_MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	public MohawkWebserverCore(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public HttpClient getHttpClient() {
		return this.httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
	private String urlStub() {
		return String.format("http://%s:%s" + REMOTE_STUB, host, port);
	}
	
	private String getParameterAppend(Map<String,String> parameters) {
		String appendQuery ="";

		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			appendQuery = appendQuery + entry.getKey() +"=" + entry.getValue() +"&";
		}
		return appendQuery;
	}
	
	private String stringResponseToString(HttpResponse<String> response) {
		JsonObject rdr = Json.createReader(new StringReader(response.body())).readObject();
		return rdr.getString("result");
	}
	
	private String stringResponseToString(String key, HttpResponse<String> response) {
		JsonObject rdr = Json.createReader(new StringReader(response.body())).readObject();
		return rdr.getString(key);
	}
	
	private int numberResponseToString(HttpResponse<String> response) {
		JsonObject rdr = Json.createReader(new StringReader(response.body())).readObject();
		return rdr.getInt("result");
	}

	private String getErrorMessage(HttpResponse<String> response) {
		JsonObject reader = Json.createReader(new StringReader(response.body())).readObject();
		return reader.getString("message");
	}
	
	/**
	 * Sending a GET request
	 * @param action
	 * @param parameters
	 * @return
	 * @throws URISyntaxException
	 */
	private HttpRequest getGetParamRequest(String action, Map<String,String> parameters) throws URISyntaxException {
		HttpRequest request;
		URI uri = URI.create(urlStub() + action);

		if(parameters != null) {
			String queryParam = getParameterAppend(parameters);
			uri= new URI(uri.getScheme(), uri.getAuthority(),
					uri.getPath(), queryParam, uri.getFragment());

		}

		request = HttpRequest.newBuilder().uri(uri)
				.GET().build();		
		
		return request;
	}
	
	/**
	 * Sending a POST request
	 * @param action
	 * @param parameters
	 * @return
	 * @throws URISyntaxException
	 * @throws JsonProcessingException 
	 */
	private HttpRequest getPostParamRequest(String action, Object parameters) throws URISyntaxException, JsonProcessingException {
		URI uri = URI.create(urlStub() + action);
		Builder request = HttpRequest.newBuilder().uri(uri)
				.setHeader("content-type", "application/json");
		
		ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(parameters);
                
        if (parameters == null) {
        	request = request.POST(BodyPublishers.ofString("{}"));
        } else {
        	request = request.POST(BodyPublishers.ofString(requestBody));
        }	
		
		return request.build();
	}
	
	/**
	 * Sending a POST request
	 * @param action
	 * @param parameters
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException 
	 */
	private HttpRequest getPostBinaryParamRequest(String action, File file, String contentType) throws URISyntaxException, IOException {
		URI uri = URI.create(urlStub() + action);
		System.out.println(file.toPath());
		HttpRequest request = HttpRequest.newBuilder().uri(uri)
				.setHeader("content-type", contentType)
				.POST(BodyPublishers.ofByteArray(Files.readAllBytes(file.toPath())))
				.build();
		
		return request;
	}
	
	public String getVersion() {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getGetParamRequest("/version", null),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			//catch the error of incorrect output
		}
		return stringResponseToString(response);
	}
	
	public String getLidStatus() {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getGetParamRequest("/lid_status", null),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			//catch the error of incorrect output
		}
		return stringResponseToString(response);
	}
	
	public String getMohawkStatus() {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getGetParamRequest("/mohawk_status", null),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			//catch the error of incorrect output
		}
		return stringResponseToString(response);
	}
	
	public int getFanSpeed() {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getGetParamRequest("/fan_speed", null),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			//catch the error of incorrect output
		}
		return numberResponseToString(response);
	}
	
	public int getTemperature() {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getGetParamRequest("/temperature", null),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			//catch the error of incorrect output
		}
		return numberResponseToString(response);
	}
	
	/**
	 * Returns the status of the pins as a JSON object, it is recommended to create a class to represent the pins when used in production
	 * @return
	 */
	public JsonArray getPinsStatus() {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getGetParamRequest("/pins_status", null),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			//catch the error of incorrect output
		}
		return Json.createReader(new StringReader(response.body())).readArray();
	}
	
	public int getFormat() {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getGetParamRequest("/format", null),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			//catch the error of incorrect output
		}
		return numberResponseToString(response);
	}
	
	public String getWorklistStatus() throws Exception {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getGetParamRequest("/worklist/status", null),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			throw new Exception(getErrorMessage(response));
		}
		return stringResponseToString(response);
	}
	

	public JsonObject getWorklistProgress() throws Exception {
		HttpResponse<String> response = null;
		
		try {
			response = this.getHttpClient().send(getGetParamRequest("/worklist", null),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			throw new Exception(getErrorMessage(response));
		}
		return Json.createReader(new StringReader(response.body())).readObject();
	}
	
	public String resetPins() {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getPostParamRequest("/reset_pins", null),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			//catch the error of incorrect output
		}
		return stringResponseToString(response);
	}
	
	public JsonArray pinsUp(List<Object> parameters) throws Exception {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getPostParamRequest("/pins_up", parameters),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			throw new Exception(getErrorMessage(response));
		}
		return Json.createReader(new StringReader(response.body())).readArray();
	}
	
	//to do: configure reader for type null
	public JsonObject configureReader(String type) {
		HttpResponse<String> response = null;
		
		Map<String, String> param = new HashMap<>();
		param.put("type", type);
		
		try {
			response = this.getHttpClient().send(getPostParamRequest("/reader", param),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			//catch the error of incorrect output
		}
		return Json.createReader(new StringReader(response.body())).readObject();
	}

	public String readBarcode() {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getPostParamRequest("/read_barcode", null),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			//catch the error of incorrect output
		}
		return stringResponseToString(response);
	}
	
	public String loadRack(Map<String, Object> params) throws Exception {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getPostParamRequest("/set_rack_barcode", params),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			throw new Exception(getErrorMessage(response));
		}
		return stringResponseToString(response);
	}
	
	public String loadWorklist(List<Object> parameters) throws Exception {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getPostParamRequest("/worklist/load_json", parameters),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			throw new Exception(getErrorMessage(response));
		}
		return stringResponseToString(response);
	}
	
	public String finishWorklist() throws Exception {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getPostParamRequest("/worklist/finish", null),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			throw new Exception(getErrorMessage(response));
		}
		return stringResponseToString(response);
	}
	
	//to do: make this work
	public String loadExcelWorklist(File worklist) throws Exception {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getPostBinaryParamRequest("/worklist/load_excel", worklist, EXCEL_MEDIA_TYPE),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			throw new Exception(getErrorMessage(response));
		}
		return stringResponseToString(response);
	}
	
	//to do: make this work
	public String loadXMLWorklist(File worklist) throws Exception {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getPostBinaryParamRequest("/worklist/load_xml", worklist, "application/xml"),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			throw new Exception(getErrorMessage(response));
		}
		return stringResponseToString(response);
	}
	
	//to do: make this work
	public String loadCSVWorklist(File worklist) throws Exception {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getPostBinaryParamRequest("/worklist/load_csv", worklist, "text/csv"),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			throw new Exception(getErrorMessage(response));
		}
		return stringResponseToString(response);
	}
	
	public JsonObject reportToJson() throws Exception {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getGetParamRequest("/report_to_json", null),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			throw new Exception(getErrorMessage(response));
		}
		return Json.createReader(new StringReader(response.body())).readObject();
	}
	
	public File reportToXML() throws Exception {
		HttpResponse<Path> response = null;
		try {
			response = this.getHttpClient().send(getGetParamRequest("/report_to_xml", null),
					BodyHandlers.ofFile(Path.of("report.xml")));
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			//catch the error of incorrect output
		}
		
		return response.body().toFile();
	}
	
	public File reportToExcel() throws Exception {
		HttpResponse<Path> response = null;
		try {
			response = this.getHttpClient().send(getGetParamRequest("/report_to_excel", null),
					BodyHandlers.ofFile(Path.of("report.xlsx")));
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			//catch the error of incorrect output
		}
		
		return response.body().toFile();
	}
	
	public File reportToCSV() throws Exception {
		HttpResponse<Path> response = null;
		try {
			response = this.getHttpClient().send(getGetParamRequest("/report_to_csv", null),
					BodyHandlers.ofFile(Path.of("report.csv")));
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			//catch the error of incorrect output
		}
	
		return response.body().toFile();
	}
	
	public String shutdown() throws Exception {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getPostParamRequest("/shutdown", null),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
		if (response.statusCode() != 200) {
			throw new Exception(getErrorMessage(response));
		}
		return stringResponseToString(response);
	}
	
	public void forceShutdown() {
		HttpResponse<String> response = null;
		try {
			response = this.getHttpClient().send(getPostParamRequest("/force_shutdown", null),
					BodyHandlers.ofString());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			//catch exception with some logs
		}
	}
}
