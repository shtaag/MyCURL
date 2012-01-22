/**
 * 
 */
package shtaag.network.curl.impl.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;


/**
 * @author takei_s
 * @date 2011/12/26
 */
public class StandardOutWriter implements OutputWriter {

	
	/* (non-Javadoc)
	 * @see shtaag.network.curl.framework.OutputWriter#write(java.lang.String)
	 */
	@Override
	public void write(String str, File stdout) throws IOException {
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
		try {
			writer.write(str);
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
