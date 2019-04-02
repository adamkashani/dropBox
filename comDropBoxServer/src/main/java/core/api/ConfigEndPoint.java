package core.api;

import java.util.List;

import javax.websocket.Extension;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;


public class ConfigEndPoint extends ServerEndpointConfig.Configurator{
	
	public static WebSocketEndpoint webSocketEndpoint = new WebSocketEndpoint();

//    public String getNegotiatedSubprotocol(List<String> supported, List<String> requested) {
//        // Plug your own algorithm here
//    }

//    public List<Extension> getNegotiatedExtensions(List<Extension> installed, List<Extension> requested) {
//        // Plug your own algorithm here
//    	return null;
//    }

//    public boolean checkOrigin(String originHeaderValue) {
//        // Plug your own algorithm here
//    }

//    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
//        // Plug your own algorithm here
//    }

    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
//        	super.getEndpointInstance(webSocketEndpoint.getClass());
    	return (T) webSocketEndpoint;
    }
	

}
