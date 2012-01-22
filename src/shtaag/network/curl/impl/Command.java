/**
 * 
 */
package shtaag.network.curl.impl;

import java.io.Serializable;
import java.util.Map;
import java.util.Queue;

import shtaag.network.curl.impl.output.OutputWriter;

/**
 * @author takei_s
 * @date 2011/12/25
 */
public class Command implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Map<String, String> headers;
	private final Map<String, String> requestBody;
	private final String command;
	private Queue<UrlFileHandling> urlFilehandling;
	private final boolean isVerbose;
	private final boolean isRedirect;
	private final String proxy;

	private final OutputWriter writer;


	/**
	 * @param headers
	 * @param command
	 * @param urlFilehandling
	 * @param writer 
	 * @param isVerbose
	 * @param isRedirect 
	 * @param proxy
	 * @param requestBody 
	 */
	public Command(Map<String, String> headers, String command,
			Queue<UrlFileHandling> urlFilehandling, OutputWriter writer, boolean isVerbose, boolean isRedirect, String proxy, Map<String, String> requestBody) {
		
		this.headers = headers;
		this.command = command;
		this.urlFilehandling = urlFilehandling;
		this.writer = writer;
		this.isVerbose = isVerbose;
		this.isRedirect = isRedirect;
		this.proxy = proxy;
		this.requestBody = requestBody;
	}

	/**
	 * @return the headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}
	public Map<String, String> getRequestBody() {
		return requestBody;
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		// TODO -Fの時とかPOSTをどう使うのか
		if (command == null) {
			return "GET";
		}
		return command;
	}


	/**
	 * @return the urlFilehandling
	 */
	public Queue<UrlFileHandling> getUrlFilehandling() {
		return urlFilehandling;
	}

	/**
	 * @param urlFilehandling the urlFilehandling to set
	 */
	public void setUrlFilehandling(Queue<UrlFileHandling> urlFilehandling) {
		this.urlFilehandling = urlFilehandling;
	}

	/**
	 * @return the isVerbose
	 */
	public boolean isVerbose() {
		return isVerbose;
	}
	public boolean isRedirect() {
		return isRedirect;
	}

	/**
	 * @return the proxy
	 */
	public String getProxy() {
		return proxy;
	}

	/**
	 * @return the writer
	 */
	public OutputWriter getWriter() {
		return writer;
	}
	
}
