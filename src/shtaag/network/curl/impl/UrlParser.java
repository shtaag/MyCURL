/**
 * 
 */
package shtaag.network.curl.impl;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import shtaag.network.curl.impl.option.KeyOnlyOptionEntity;
import shtaag.network.curl.impl.option.KeyValueOptionEntity;
import shtaag.network.curl.impl.option.Option;

/**
 * Return Queue of URLs from String[].<br>
 * Only HTTP is supported.<br>
 * @author takei_s
 * @date 2011/12/25
 */
public class UrlParser {
	
	public static List<URL> parse(String[] str) {
		List<URL> result = new ArrayList<URL>();
		for (int i = 0; i < str.length; i++) {
			
			if (Option.check(str[i]) == null) {
				if (i > 0) {
					Option option = Option.check(str[i - 1]);
					if (option == null) {
						result.add(constructUrl(str[i]));
					}
					switch (option) {
					case LOCATION: result.add(constructUrl(str[i])); break;
					case VERBOSE: result.add(constructUrl(str[i])); break;
					default: break;
					}
				} else {
					result.add(constructUrl(str[i]));
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
	private static URL constructUrl(String str) {
		if (! str.startsWith(HTTP_HEAD)) {
				return new URL(getHost(str), str.substring(getHost(str).length()));
		}
		String withoutProtcol = str.replace(HTTP_HEAD, "");
		return new URL(getHost(withoutProtcol), withoutProtcol.substring(getHost(withoutProtcol).length()));
	}
	
	private static String getHost(String str) {
		return str.split(FS)[0];
	}

}
