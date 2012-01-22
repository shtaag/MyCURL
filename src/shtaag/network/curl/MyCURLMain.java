/**
 * 
 */
package shtaag.network.curl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Queue;

import shtaag.network.curl.impl.Command;
import shtaag.network.curl.impl.CommandFactory;
import shtaag.network.curl.impl.URL;
import shtaag.network.curl.impl.UrlFileHandling;
import shtaag.network.curl.impl.UrlParser;
import shtaag.network.curl.impl.option.OptionEntity;
import shtaag.network.curl.impl.option.OptionParser;
import shtaag.network.curl.impl.socket.MySocket;

/**
 * @author takei_s
 * @date 2011/12/24
 */
public class MyCURLMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			MyCURLMain instance = new MyCURLMain();
			instance.execute(args);
			
		} catch (Exception e) {
			System.out.println(">>> Error End. <<<");
			e.printStackTrace();
			System.exit(9);
		}
		System.out.println(">>> Normal End. <<<");
		System.exit(0);
	}
	
	public void execute(String[] args) {
		
		try {
			List<OptionEntity> optionList = OptionParser.parse(args);
			List<URL> urlList = UrlParser.parse(args);
			
			if (urlList.isEmpty()) {
				throw new RuntimeException("no Url request");
			}
			
			Command command = new CommandFactory().getCommand(optionList, urlList);
			
			Queue<UrlFileHandling> urlQueue = command.getUrlFilehandling();
			// TODO 本当はurlとsocket(protocol)をひとまとめにしなければならなかった。
			while (! urlQueue.isEmpty()) {
				UrlFileHandling urlFileHandling = urlQueue.poll();
				
				ByteBuffer resBytes = null;
				URL url = (command.getProxy() != null) ? getProxiedUrl(command.getProxy()) : urlFileHandling.url;
				if (command.isRedirect()) {
					do {
						if (isRedirect(resBytes)) {
							url = getRedirectUrl(resBytes);
						}
						MySocket socket = new MySocket(url.host, url.port);
						resBytes = socket.doHttpClient(command, urlFileHandling.url.path);
					} while (isRedirect(resBytes));
					
				} else {
					MySocket socket = new MySocket(url.host, url.port);
					resBytes = socket.doHttpClient(command, urlFileHandling.url.path);
				}
					
				command.getWriter().write(resBytes, urlFileHandling.filename);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * @param resBytes
	 * @return
	 */
	private URL getRedirectUrl(ByteBuffer resBytes) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param resBytes
	 * @return
	 */
	private boolean isRedirect(ByteBuffer resBytes) {
		// TODO Auto-generated method stub
		return false;
	}

	private URL getProxiedUrl(String proxy) {
		String[] values = proxy.split(":");
		if (values.length > 2 || values.length == 0) {
			throw new IllegalArgumentException("proxy value is invalid.");
		}
		int port = (values.length == 1) ? 1080 : Integer.parseInt(values[1]);
		return UrlParser.constructUrl(values[0], port);
	}

}
