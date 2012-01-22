/**
 * 
 */
package shtaag.network.curl.impl;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import shtaag.network.curl.impl.option.Option;

/**
 * Return Queue of URLs from String[].<br>
 * Only HTTP is supported.<br>
 * @author takei_s
 * @date 2011/12/25
 */
public class UrlParser {
	
	public static final int DEFAULT_PORT = 80;
	public static List<URL> parse(String[] str) {
		List<URL> result = new ArrayList<URL>();
		for (int i = 0; i < str.length; i++) {
			
			if (Option.check(str[i]) == null) {
				if (i > 0) {
					Option option = Option.check(str[i - 1]);
					if (option == null) {
						result.add(constructUrl(str[i], DEFAULT_PORT));
					}
					switch (option) {
					case LOCATION: result.add(constructUrl(str[i], DEFAULT_PORT)); break;
					case VERBOSE: result.add(constructUrl(str[i], DEFAULT_PORT)); break;
					default: break;
					}
				} else {
					result.add(constructUrl(str[i], DEFAULT_PORT));
				}
			}
		}
	
		return result;
		
	}
	
	private static final String HTTP_HEAD = "http://";
	private static final String FS = System.getProperty("file.separator");
	/**
	 * @param string
	 * @return
	 * @throws MalformedURLException 
	 */
	public static URL constructUrl(String str, int port) {
		if (! str.startsWith(HTTP_HEAD)) {
				return new URL(getHost(str), port, str.substring(getHost(str).length()));
		}
		String withoutProtcol = str.replace(HTTP_HEAD, "");
		return new URL(getHost(withoutProtcol), port, withoutProtcol.substring(getHost(withoutProtcol).length()));
	}
	
	private static String getHost(String str) {
		return str.split(FS)[0];
	}

}
