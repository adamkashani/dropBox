package core.api;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/socket" ,  configurator = ConfigEndPoint.class)
public class WebSocketEndpoint {
	private int count;
	private List<Session>clientSessions = new ArrayList<>();


	public WebSocketEndpoint() {
		System.out.println("from Construct create WebSocketEndpoint");
	}


	@PostConstruct
	public void init () {
		System.out.println("from postConstruct create WebSocketEndpoint");
	}


	@OnOpen
	public void onOpen (Session session) {
		clientSessions.add(session);
		System.out.println(session.getId());
		System.out.println("New acoount is register" + ++count);
		System.out.println(session.getBasicRemote());		
		System.out.println(session.getMessageHandlers());		
		System.out.println(session.getOpenSessions());		
	}

	@OnMessage
	public void handleTextMessage(String message , Session session ) {
		try {
			System.out.println(" the count : "+count);
			for (Session tempSession : clientSessions) {
				System.out.println("the temp session : "+ tempSession);
				tempSession.getBasicRemote().sendText(message);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("New Text Message Received the message : "+message);
	}

	@OnMessage(maxMessageSize = 1024000)
	public byte[] handleBinaryMessage(byte[] buffer) {
		System.out.println("New Binary Message Received");
		return buffer;
	}

	@OnClose
	public void onClose(Session session) {
		this.clientSessions.remove(session);
		System.out.println("the session close : " +session);
	}

}
