/**
 * 
 */
package shtaag.network.curl.impl.output;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;


/**
 * @author takei_s
 * @date 2011/12/26
 */
public class StandardOutWriter implements OutputWriter {

	
	/* (non-Javadoc)
	 * @see shtaag.network.curl.framework.OutputWriter#write(java.lang.String)
	 */
	@Override
	public void write(ByteBuffer byteBuffer, File stdout) throws IOException {
		
		System.out.println(new String(byteBuffer.array(), "UTF-8"));
		
	}
	

}
