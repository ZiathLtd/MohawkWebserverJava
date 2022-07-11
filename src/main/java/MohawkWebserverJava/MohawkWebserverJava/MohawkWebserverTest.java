package MohawkWebserverJava.MohawkWebserverJava;

import java.io.File;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

/**
 * Hello world!
 *
 */
public class MohawkWebserverTest 
{
	private final static String HOST = "localhost";
	private final static int PORT = 8556;
	
    public static void main( String[] args )
    {
        MohawkWebserverCore mohawk = new MohawkWebserverCore(HOST, PORT);
        mohawk.setHttpClient(HttpClient.newBuilder().version(Version.HTTP_2) // this is the default
				.build());
        
        
        //Get version
        String version = mohawk.getVersion();
        System.out.println("Version: " + version);
        
        
        //Get lid status
        String lidStatus = mohawk.getLidStatus();
        System.out.println("Lid status: " + lidStatus);
        
        
        //Get mohawk status
        String mohawkStatus = mohawk.getMohawkStatus();
        System.out.println("Mohawk status: " + mohawkStatus);
        
        
        //Get temperature
        int temperature = mohawk.getTemperature();
        System.out.println("Temperature: " + temperature);
        
        
        //Get fan speed
        int fanSpeed = mohawk.getFanSpeed();
        System.out.println("Fan speed: " + fanSpeed);
        
        
        //Get pins status
        JsonArray pinsStatus = mohawk.getPinsStatus();
        System.out.println("Pins status: " + pinsStatus);
        
        
        //Get format
        int format = mohawk.getFormat();
        System.out.println("Format: " + format);
        
        
        //Reset pins
        String resetPinsResult = mohawk.resetPins();
        System.out.println("Reset pins result: " + resetPinsResult);
        
        
        //Pins up
        List<Object> pins = new ArrayList<>();
        pins.add(new Pin(1, 4));
        pins.add(new Pin(2, 2));
        JsonArray pinsUpResult = null;
		try {
			pinsUpResult = mohawk.pinsUp(pins);
	        System.out.println("Pins up result: " + pinsUpResult);
		} catch (Exception e) {
	        System.out.println("Pins up result: " + e.getMessage());
		}
		
		
		//Configure reader
		JsonObject reader = mohawk.configureReader("ZIATH");
		System.out.println("Configure reader result: " + reader);
		

		//Read rack barcode
		String barcode = mohawk.readBarcode();
		System.out.println("Rack barode: " + barcode);
		

        //Load json worklist
        List<Object> worklist = new ArrayList<>();
        worklist.add(new WorklistItem(1, 4, "1D", "001"));
        worklist.add(new WorklistItem(2, 2, "2B", "001"));
        worklist.add(new WorklistItem(1, 3, "1C", "002"));
        worklist.add(new WorklistItem(3, 1, "3A", "002"));
        String loadJsonWorklistResult = null;
		try {
			loadJsonWorklistResult = mohawk.loadWorklist(worklist);
	        System.out.println("Load worklist result: " + loadJsonWorklistResult);
		} catch (Exception e) {
	        System.out.println("Load worklist result: " + e.getMessage());
		}
		
		
        //Load rack
        Map<String, Object> params = new HashMap<>();
        params.put("rack_barcode", "002");
        params.put("reset_pins", "true");
        String loadRackResult = null;
		try {
			loadRackResult = mohawk.loadRack(params);
	        System.out.println("Load rack result: " + loadRackResult);
		} catch (Exception e) {
	        System.out.println("Load rack result: " + e.getMessage());
		}

		//Worklist progress
		JsonObject worklistProgress;
		try {
			worklistProgress = mohawk.getWorklistProgress();
			System.out.println("Worklist progress: " + worklistProgress);
		} catch (Exception e2) {
			System.out.println("Worklist progress: " + e2.getMessage());
		}
		

		//Worklist status
		String worklistStatus;
		try {
			worklistStatus = mohawk.getWorklistStatus();
			System.out.println("Worklist status: " + worklistStatus);
		} catch (Exception e1) {
			System.out.println("Worklist status: " + e1.getMessage());
		}

		
		//Finish worklist
		try {
			String finishWorklistResult = mohawk.finishWorklist();
			System.out.println("Finish worklist result: " + finishWorklistResult);
		} catch (Exception e) {
			System.out.println("Finish worklist result: " + e.getMessage());
		}
		
		
		//Report to json
		try {
			JsonObject jsonReport = mohawk.reportToJson();
			System.out.println("Json Report: " + jsonReport);
		} catch (Exception e) {
			System.out.println("Json Report: " + e.getMessage());
		}
		
		
		//Report to XML
		try {
			File xmlReport = mohawk.reportToXML();
			System.out.println("XML Report: " + xmlReport.toPath());
		} catch (Exception e) {
			System.out.println("XML Report: " + e.getMessage());
		}
		
		
		//Report to Excel
		try {
			File excelReport = mohawk.reportToExcel();
			System.out.println("Excel Report: " + excelReport.toPath());
		} catch (Exception e) {
			System.out.println("Excel Report: " + e.getMessage());
		}
		
		
		//Report to CSV
		try {
			File csvReport = mohawk.reportToCSV();
			System.out.println("CSV Report: " + csvReport.toPath());
		} catch (Exception e) {
			System.out.println("CSV Report: " + e.getMessage());
		}
		
		
		//Shutdown
		try {
			String shutdownResult = mohawk.shutdown();
			System.out.println("Shutdown result: " + shutdownResult);
		} catch (Exception e) {
			System.out.println("Shutdown result: " + e.getMessage());
		}
		
		
		//Force shudown
		mohawk.forceShutdown();
		System.out.println("Forced shutdown!");
		
		
		//Load excel file
//		try {
//			File excelWorklist = new File("C:\\Users\\AndreeaStefanescu\\Desktop\\picklist.xlsx");
//			String loadExcelWorklist = mohawk.loadExcelWorklist(excelWorklist);
//			System.out.println("Load excel worklist result: " + loadExcelWorklist);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("Load excel worklist result: " + e.getMessage());
//		}
    }
}
