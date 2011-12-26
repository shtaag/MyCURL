/**
 * 
 */
package shtaag.network.curl.framework;

import java.io.File;
import java.io.IOException;

/**
 * @author takei_s
 * @date 2011/12/25
 */
public interface OutputWriter {
	
	public void write(String str, File file) throws IOException;

}
