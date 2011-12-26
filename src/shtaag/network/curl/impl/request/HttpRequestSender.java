/**
 * 
 */
package shtaag.network.curl.impl.request;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import shtaag.network.curl.framework.RequestSender;
import shtaag.network.curl.impl.Command;
import shtaag.network.curl.impl.ResponseEntity;
import shtaag.network.curl.impl.UrlFileHandling;

/**
 * @author takei_s
 * @date 2011/12/26
 */
public class HttpRequestSender implements RequestSender {

	private static final String ls = System.getProperty("line.separator"); 
	/* (non-Javadoc)
	 * @see shtaag.network.curl.framework.RequestSender#request(shtaag.network.curl.impl.Command)
	 */
	@Override
	public ResponseEntity request(Command command, UrlFileHandling urlFileHandling) throws IOException {
		
		StringBuilder result = new StringBuilder();
		URL url = urlFileHandling.url;
		File file = urlFileHandling.filename;
		HttpURLConnection conn = null;
		try {
			if (command.isProxy()) {
				// TODO add proxy
			}
			conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod(command.getCommand());
			conn.setInstanceFollowRedirects(false);
			setHeaders(conn, command.getHeaders(), command.isVerbose(), result);
			
			conn.connect();
			
			writeResponseHeaders(conn, command.isVerbose(), result);
			
			result.append(conn.getResponseCode());
			result.append(ls);
			result.append(conn.getResponseMessage());
			result.append(ls);
			
			writeBody(conn, result);
			
			return new ResponseEntity(result.toString(), file);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
	
	/**
	 * @param conn
	 * @param result
	 * @throws IOException 
	 */
	private void writeBody(HttpURLConnection conn, StringBuilder result) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				result.append(line);
				result.append(ls);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void writeResponseHeaders(HttpURLConnection conn, boolean isVerbose, StringBuilder result) {
		Map<String, List<String>> responseHeaders = conn.getHeaderFields();
		if (isVerbose) {
			for (Entry<String, List<String>> entry : responseHeaders.entrySet()) {
				String key = entry.getKey();
				for (String val : entry.getValue()) {
					result.append("< " + key + " : " + val);
					result.append(ls);
				}
			}
		}
	}
	
	private void setHeaders(HttpURLConnection conn, Map<String, String> headers, boolean isVerbose, StringBuilder result) {
		for (java.util.Map.Entry<String, String> entry : headers.entrySet()) {
			
			conn.setRequestProperty(entry.getKey(), entry.getValue());
			if (isVerbose) {
				result.append("> " + entry.getKey() + " : " + entry.getValue());
				result.append(ls);
			}
		}
	}

}
