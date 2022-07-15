package MohawkWebserverJava.MohawkWebserverJava;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

@ClientEndpoint
public class NotificationWebsocketClient {
	private Session session = null;
    
    public NotificationWebsocketClient(String url) {
        try {
        	URI endpointURI = new URI(url);
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        System.out.println("Client has opened the connection");
    }
    
	@OnClose
	public void onClose(Session session) {
		this.session = null;
        System.out.println("Client has closed the connection");
	}
    
    @OnMessage
    public void processMessage(String message) {
    	System.out.println();
        System.out.println("Notification message: " + getNotificationMessage(message));
        System.out.println("Notification payload: " + getNotificationPayload(message));
    	System.out.println();
    }
    
    private String getNotificationMessage(String message) {
		JsonObject rdr = Json.createReader(new StringReader(message)).readObject();
		return rdr.getString("notification_message");
	}

	private JsonValue getNotificationPayload(String message) {
		JsonObject rdr = Json.createReader(new StringReader(message)).readObject();
		return rdr.get("payload");
	}

	public void closeSession() {
    	try {
			this.session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
