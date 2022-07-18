package MohawkWebserverJava.MohawkWebserverJava;

import java.io.File;
import java.net.URISyntaxException;
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
	private final static String NOTIFICATION_URL = "ws://localhost:8025/mohawk/notifications";
	public static NotificationWebsocketClient notificationClient;
	
    public static void main( String[] args ) throws URISyntaxException, InterruptedException {
        MohawkWebserverCore mohawk = new MohawkWebserverCore(HOST, PORT);
        
        //Start the notification client
        startNotificationClient();
//        startNotificationClientAndKeepAlive();
        
        //Get version
        String version;
		try {
			version = mohawk.getVersion();
	        System.out.println("Version: " + version);
		} catch (Exception e) {
	        System.out.println("Version: " + e.getMessage());
		}
        
        
        //Get lid status
        String lidStatus;
		try {
			lidStatus = mohawk.getLidStatus();
	        System.out.println("Lid status: " + lidStatus);
		} catch (Exception e) {
	        System.out.println("Lid status: " + e.getMessage());
		}
        
        
        //Get mohawk status
        String mohawkStatus;
		try {
			mohawkStatus = mohawk.getMohawkStatus();
	        System.out.println("Mohawk status: " + mohawkStatus);
		} catch (Exception e) {
	        System.out.println("Mohawk status: " + e.getMessage());
		}
        
        
        //Get temperature
        int temperature;
		try {
			temperature = mohawk.getTemperature();
	        System.out.println("Temperature: " + temperature);
		} catch (Exception e) {
	        System.out.println("Temperature: " + e.getMessage());
		}
        
        
        //Get fan speed
        int fanSpeed;
		try {
			fanSpeed = mohawk.getFanSpeed();
	        System.out.println("Fan speed: " + fanSpeed);
		} catch (Exception e) {
	        System.out.println("Fan speed: " + e.getMessage());
		}
        
        
        //Get pins status
        JsonArray pinsStatus;
		try {
			pinsStatus = mohawk.getPinsStatus();
	        System.out.println("Pins status: " + pinsStatus);
		} catch (Exception e) {
	        System.out.println("Pins status: " + e.getMessage());
		}
        
        
        //Get format
        int format;
		try {
			format = mohawk.getFormat();
	        System.out.println("Format: " + format);
		} catch (Exception e) {
	        System.out.println("Format: " + e.getMessage());
		}
        
        
        //Reset pins
        String resetPinsResult;
		try {
			resetPinsResult = mohawk.resetPins();
	        System.out.println("Reset pins result: " + resetPinsResult);
		} catch (Exception e) {
	        System.out.println("Reset pins result: " + e.getMessage());
		}
        
        
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
        params.put("rack_barcode", "LVL100074983");
        params.put("reset_pins", "true");
        String loadRackResult = null;
		try {
			loadRackResult = mohawk.loadRack(params);
	        System.out.println("Load rack result: " + loadRackResult);
		} catch (Exception e) {
	        System.out.println("Load rack result: " + e.getMessage());
		}
		
		
		//Configure reader
		JsonObject reader;
		try {
			reader = mohawk.configureReader(null);
			System.out.println("Configure reader result: " + reader);
		} catch (Exception e) {
			System.out.println("Configure reader result: " + e.getMessage());
		}
		

		//Read rack barcode
		String barcode;
		try {
			barcode = mohawk.readBarcode();
			System.out.println("Rack barode: " + barcode);
		} catch (Exception e) {
			System.out.println("Rack barode: " + e.getMessage());
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
		
		
		//Load excel file
		try {
			File excelWorklist = new File("C:\\Users\\AndreeaStefanescu\\Desktop\\picklist.xlsx");
			String loadExcelWorklist = mohawk.loadExcelWorklist(excelWorklist);
			System.out.println("Load excel worklist result: " + loadExcelWorklist);
		} catch (Exception e) {
			System.out.println("Load excel worklist result: " + e.getMessage());
		}
		
		
		//Finish worklist
		try {
			String finishWorklistResult = mohawk.finishWorklist();
			System.out.println("Finish worklist result: " + finishWorklistResult);
		} catch (Exception e) {
			System.out.println("Finish worklist result: " + e.getMessage());
		}

		
		//Load xml file
		try {
			File xmlWorklist = new File("C:\\Users\\AndreeaStefanescu\\Desktop\\picklist.xml");
			String loadXmlWorklist = mohawk.loadXMLWorklist(xmlWorklist);
			System.out.println("Load xml worklist result: " + loadXmlWorklist);
		} catch (Exception e) {
			System.out.println("Load xml worklist result: " + e.getMessage());
		}
		
		
		//Finish worklist
		try {
			String finishWorklistResult = mohawk.finishWorklist();
			System.out.println("Finish worklist result: " + finishWorklistResult);
		} catch (Exception e) {
			System.out.println("Finish worklist result: " + e.getMessage());
		}

		
		//Load csv file
		try {
			File csvWorklist = new File("C:\\Users\\AndreeaStefanescu\\Desktop\\picklist.csv");
			String loadCsvWorklist = mohawk.loadCSVWorklist(csvWorklist);
			System.out.println("Load xml worklist result: " + loadCsvWorklist);
		} catch (Exception e) {
			System.out.println("Load xml worklist result: " + e.getMessage());
		}
		
		
		//Shutdown
		try {
			String shutdownResult = mohawk.shutdown();
			System.out.println("Shutdown result: " + shutdownResult);
		} catch (Exception e) {
			System.out.println("Shutdown result: " + e.getMessage());
		}
		
		
		//Force shutdown
		mohawk.forceShutdown();
		System.out.println("Forced shutdown!");
		
		
		//call close session to close the notification client, either at the end of the operations or after some time
		Thread.sleep(3000);
		notificationClient.closeSession();
    }
    
    /**
     * Use this to start the notification client on the main thread 
     * and have it close after all operations are done
     */
    public static void startNotificationClient() {
    	notificationClient = new NotificationWebsocketClient(NOTIFICATION_URL);
    }
    
    /**
     * Use this to start the notification client on a new thread
     * and keep it running until the program is terminated
     * 
     * @throws InterruptedException 
     */
    public static void startNotificationClientAndKeepAlive() throws InterruptedException {
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
			        notificationClient = new NotificationWebsocketClient(NOTIFICATION_URL);
			        
			        Runtime.getRuntime().addShutdownHook(new Thread(notificationClient::closeSession));
			        
			        // Need this to keep the session running
					Thread.currentThread().join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
    	
    	//wait for the notification server to start
    	Thread.sleep(500);
    }
}
