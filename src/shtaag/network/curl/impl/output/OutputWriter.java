/**
 * 
 */
package shtaag.network.curl.impl.output;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author takei_s
 * @date 2011/12/25
 */
public interface OutputWriter {
	
	public void write(ByteBuffer byteBuffer, File file) throws IOException;

}
