/**
 * Client Application for Plugtest 2012, Paris, France
 * 
 * Execute with argument Identifier (e.g., TD_COAP_CORE_01)
 */
package org.ws4d.coap.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ws4d.coap.connection.BasicCoapChannelManager;
import org.ws4d.coap.connection.BasicCoapSocketHandler;
import org.ws4d.coap.interfaces.CoapChannel;
import org.ws4d.coap.interfaces.CoapChannelManager;
import org.ws4d.coap.interfaces.CoapClient;
import org.ws4d.coap.interfaces.CoapClientChannel;
import org.ws4d.coap.interfaces.CoapRequest;
import org.ws4d.coap.interfaces.CoapResponse;
import org.ws4d.coap.messages.BasicCoapRequest.CoapRequestCode;
import org.ws4d.coap.messages.CoapMediaType;

/**
 * @author Nico Laum <nico.laum@uni-rostock.de>
 * @author Christian Lerche <christian.lerche@uni-rostock.de>
 * 
 */
public class PlugtestClient implements CoapClient{
    CoapChannelManager channelManager = null;
    CoapClientChannel clientChannel = null;
    CoapRequest request = null; 
    private static Logger logger = Logger.getLogger(BasicCoapSocketHandler.class.getName());
    boolean exitAfterResponse = true;

	public static void main(String[] args) {
		if (args.length > 1 || args.length < 1) {
			System.err.println("illegal number of arguments");
			System.exit(1);
		}
		
		logger.setLevel(Level.WARNING);
		PlugtestClient client = new PlugtestClient();
		client.start(args[0]);
	}
	
	
	public void start(String testcase){
		System.out.println("===START=== (Run Test Client: " + testcase + ")");
		String testId = testcase;
		
		if (testId.equals("TD_COAP_CORE_01")) {
			init(true, CoapRequestCode.GET);
			request.setUriPath("/test");
		} 
		else if (testId.equals("TD_COAP_CORE_02")) {
			init(true, CoapRequestCode.POST);
			request.setUriPath("/test");
			request.setPayload("Content of new resource /test");
			request.setContentType(CoapMediaType.text_plain);
		} 
		else if (testId.equals("TD_COAP_CORE_03")) {
			init(true, CoapRequestCode.PUT);
			request.setUriPath("/test");
			request.setPayload("Content of new resource /test");
			request.setContentType(CoapMediaType.text_plain);
		} 
		else if (testId.equals("TD_COAP_CORE_04")) {
			init(true, CoapRequestCode.DELETE);
			request.setUriPath("/test");
		} 
		else if (testId.equals("TD_COAP_CORE_05")) {
			init(false, CoapRequestCode.GET);
			request.setUriPath("/test");
		} 
		else if (testId.equals("TD_COAP_CORE_06")) {
			init(false, CoapRequestCode.POST);
			request.setUriPath("/test");
			request.setPayload("Content of new resource /test");
			request.setContentType(CoapMediaType.text_plain);
		} 
		else if (testId.equals("TD_COAP_CORE_07")) {
			init(false, CoapRequestCode.PUT);
			request.setUriPath("/test");
			request.setPayload("Content of new resource /test");
			request.setContentType(CoapMediaType.text_plain);
		}
		else if (testId.equals("TD_COAP_CORE_08")) {
			init(false, CoapRequestCode.DELETE);
			request.setUriPath("/test");
		} 
		else if (testId.equals("TD_COAP_CORE_09")) {
			//TODO
			System.out.println("===Failure=== (NOT IMPLEMENTED)");
			System.exit(-1);
		} 
		else if (testId.equals("TD_COAP_CORE_10")) {
			init(true, CoapRequestCode.GET);
			request.setUriPath("/test");
			request.setToken("AABBCCDD".getBytes());
		} 
		else if (testId.equals("TD_COAP_CORE_11")) {
			init(true, CoapRequestCode.GET);
			request.setUriPath("/test");
		} 
		else if (testId.equals("TD_COAP_CORE_12")) {
			init(true, CoapRequestCode.GET);
			request.setUriPath("/seg1/seg2/seg3");
		} 
		else if (testId.equals("TD_COAP_CORE_13")) {
			init(true, CoapRequestCode.GET);
			request.setUriPath("/query");
			request.setUriQuery("first=1&second=2&third=3");
		} 
		else if (testId.equals("TD_COAP_CORE_14")) {
		} 
		else if (testId.equals("TD_COAP_CORE_15")) {
		} 
		else {
			System.out.println("===Failure=== (unknown test case)");
			System.exit(-1);
		}		
		run();
	}
    
    
	public void init(boolean reliable, CoapRequestCode requestCode) {
		channelManager = BasicCoapChannelManager.getInstance();
		channelManager.setMessageId(1000);
		
		try {
			clientChannel = channelManager.connect(this, InetAddress.getByName(TestConfiguration.TEST_SERVER_ADDRESS), TestConfiguration.TEST_SERVER_PORT);
			if (clientChannel == null){
				System.out.println("Connect failed.");
				System.exit(-1);
			}
			request = clientChannel.createRequest(reliable, requestCode);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void run() {
		if(request.getPayload() != null){
			System.out.println("Send Request: " + request.toString() + " (" + new String(request.getPayload()) +")");
		}else {
			System.out.println("Send Request: " + request.toString());
		}
		clientChannel.sendMessage(request);
	}




	@Override
	public void onConnectionFailed(CoapClientChannel channel, boolean notReachable, boolean resetByServer) {
		System.out.println("Connection Failed");
		System.exit(-1);
	}

	@Override
	public void onResponse(CoapClientChannel channel, CoapResponse response) {
		if (response.getPayload() != null){
			System.out.println("Response: " + response.toString() + " (" + new String(response.getPayload()) +")");
		} else {
			System.out.println("Response: " + response.toString());
		}
		if (exitAfterResponse){
			System.out.println("===END===");
			System.exit(0);
		}
	}
}
