/**
 * 
 */
package shtaag.network.curl.impl;

import java.io.File;

/**
 * @author takei_s
 * @date 2011/12/26
 */
public class ResponseEntity {

	public final String result;
	public final File file;

	/**
	 * @param result
	 * @param file
	 */
	public ResponseEntity(String result, File file) {
		this.result = result;
		this.file = file;
	}

}
