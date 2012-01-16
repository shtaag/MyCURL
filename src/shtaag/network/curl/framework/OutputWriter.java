/**
 * 
 */
package shtaag.network.curl.framework;

import java.io.IOException;

/**
 * @author takei_s
 * @date 2011/12/25
 */
public interface OutputWriter {
	
	public void write(String str) throws IOException;

}
