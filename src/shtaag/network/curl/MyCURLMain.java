/**
 * 
 */
package shtaag.network.curl;

import java.io.IOException;
import java.util.List;
import java.util.Queue;

import shtaag.network.curl.impl.Command;
import shtaag.network.curl.impl.CommandFactory;
import shtaag.network.curl.impl.URL;
import shtaag.network.curl.impl.UrlFileHandling;
import shtaag.network.curl.impl.option.OptionEntity;
import shtaag.network.curl.impl.parser.OptionParser;
import shtaag.network.curl.impl.parser.UrlParser;
import shtaag.network.curl.impl.request.MySocket;

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
			// TODO 本当はurlとsender(protocol)をひとまとめにしなければならなかった。
//			RequestSender sender = new HttpRequestSender();
			while (! urlQueue.isEmpty()) {
				UrlFileHandling urlFileHandling = urlQueue.poll();
				MySocket socket = new MySocket(urlFileHandling.url.host, 80);
				socket.doHttpClient(command, urlFileHandling.url.path);
//				ResponseEntity result = sender.request(command, urlFileHandling);
//				command.getWriter().write(result.result, result.file);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

}
