/**
 * 
 */
package shtaag.network.curl.impl.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import shtaag.network.curl.impl.option.Option;

/**
 * Return Queue of URLs from String[].<br>
 * Only HTTP is supported.<br>
 * Protocol is supplemented if it doesn't have.
 * @author takei_s
 * @date 2011/12/25
 */
public class UrlParser {
	
	public static List<URL> parse(String[] str) throws MalformedURLException {
		List<URL> result = new ArrayList<URL>();
		for (int i = 0; i < str.length; i++) {
			
			if (Option.check(str[i]) == null) {
				if (i > 0) {
					if (Option.check(str[i - 1]) == null) {
						result.add(supplUrl(str[i]));
					}
				} else {
					result.add(supplUrl(str[i]));
				}
			}
		}
	
		return result;
		
	}
	
	private static final String HTTP_HEAD = "http://";
	/**
	 * @param string
	 * @return
	 * @throws MalformedURLException 
	 */
	private static URL supplUrl(String str) throws MalformedURLException {
		try {
			if (! str.startsWith(HTTP_HEAD)) {
					return new URL(HTTP_HEAD + str);
			}
			return new URL(str);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;
		}
	}

}
