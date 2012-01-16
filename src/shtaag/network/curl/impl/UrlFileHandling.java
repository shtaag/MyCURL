/**
 * 
 */
package shtaag.network.curl.impl;

import java.io.File;

/**
 * @author takei_s
 * @date 2011/12/26
 */
public class UrlFileHandling {
	
	public final URL url;
	public final File filename;
	/**
	 * @param url
	 * @param filename
	 */
	public UrlFileHandling(URL url, File filename) {
		super();
		this.url = url;
		this.filename = filename;
	}
	
	public static class StdOut extends File {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String pathName = "stdout";
		/**
		 * @param pathname
		 */
		public StdOut(String pathName) {
			super(pathName);
		}
		
	}
	

}
