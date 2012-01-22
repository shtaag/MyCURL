/**
 * 
 */
package shtaag.network.curl.impl;

/**
 * @author takei_s
 * @date 2012/01/16
 */
public class URL {
	
	public final String host;
	public final int port;
	public final String path;
	
	public URL(String host, int port, String path) {
		this.host = host;
		this.port = port;
		this.path = path;
	}

}
