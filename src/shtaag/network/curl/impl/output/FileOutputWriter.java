/**
 * 
 */
package shtaag.network.curl.impl.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;


/**
 * @author takei_s
 * @date 2011/12/26
 */
public class FileOutputWriter implements OutputWriter {

	/* (non-Javadoc)
	 * @see shtaag.network.curl.framework.OutputWriter#write(java.lang.String)
	 */
	@Override
	public void write(ByteBuffer byteBuffer, File file) throws IOException {
		BufferedWriter writer = null;
		try {
			//　TODO test
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			writer.write(new String(byteBuffer.array(),"UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
