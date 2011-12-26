/**
 * 
 */
package shtaag.network.curl.framework;

import java.io.IOException;

import shtaag.network.curl.impl.Command;
import shtaag.network.curl.impl.ResponseEntity;
import shtaag.network.curl.impl.UrlFileHandling;

/**
 * @author takei_s
 * @date 2011/12/25
 */
public interface RequestSender {

	public ResponseEntity request(Command command, UrlFileHandling urlFilehandling) throws IOException;
}
