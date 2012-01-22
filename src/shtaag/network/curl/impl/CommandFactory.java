/**
 * 
 */
package shtaag.network.curl.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Queue;

import shtaag.network.curl.impl.option.Option;
import shtaag.network.curl.impl.option.OptionEntity;
import shtaag.network.curl.impl.output.FileOutputWriter;
import shtaag.network.curl.impl.output.OutputWriter;
import shtaag.network.curl.impl.output.StandardOutWriter;

/**
 * @author takei_s
 * @date 2011/12/25
 */
public class CommandFactory {

	public Command getCommand(List<OptionEntity> optionList, List<URL> urlList) throws IOException {
		
		// -H
		Map<String, String> headers = getHeaders(optionList);
		// -X
		String command = selectCommand(optionList);
		// -o
		List<File> files = getFileList(optionList);
		Queue<UrlFileHandling> urlFilehandling = combineUrlFile(urlList, files);
		OutputWriter writer = getWriter(files);
		// -v
		boolean isVerbose = isVerbose(optionList);
		// -L
		boolean isRedirect = isRedirect(optionList);
		// -x
		String proxy = proxy(optionList);
		// -d, -F
		Map<String, String> requestBody = getRequestBody(optionList);
		return new Command(headers, command, urlFilehandling, writer, isVerbose, isRedirect, proxy, requestBody);
	}

	/**
	 * @param optionList
	 * @return
	 * @throws IOException 
	 */
	private Map<String, String> getRequestBody(List<OptionEntity> optionList) throws IOException {
		Map<String, String> result = new HashMap<String, String>();
		for (OptionEntity option : optionList) {
			if (option.getType().equals(Option.FORM)) {
				String[] keyValuePair = option.getValue().split("=");
				result.put(keyValuePair[0], keyValuePair[1]);
			} else if (option.getType().equals(Option.DATA)) {
				Map<String, String> datas = parseData(option.getValue());
				// TODO foreach entryでなぜかできない。。。
				for (Iterator<Entry<String, String>> it = datas.entrySet().iterator(); it.hasNext();) {
					Entry<String, String> entry = it.next();
					result.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return result;
	}

	/**
	 * @param value
	 * @return
	 * @throws IOException 
	 */
	private Map<String, String> parseData(String value) throws IOException {
		Map<String, String> resultMap = new HashMap<String, String>();
		if (value.indexOf("@") == 0) {
			// @datafile
			File file = new File(value.substring(1));
			InputStream stream = null;
			Properties props = new Properties();
			try {
				stream = new FileInputStream(file);
				props.load(stream);
				for (Entry<Object, Object> entry: props.entrySet()) {
					resultMap.put((String)entry.getKey(), (String)entry.getValue());
				}
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			String[] keyValuePair = value.split("=");
			resultMap.put(keyValuePair[0], keyValuePair[1]);
		}
		return resultMap;
	}

	private boolean isRedirect(List<OptionEntity> optionList) {
		for (OptionEntity option : optionList) {
			if (option.getType().equals(Option.LOCATION)) {
				return true;
			}
		}
		return false;
	}

	private OutputWriter getWriter(List<File> files) {
		if (files.isEmpty()) {
			return new StandardOutWriter();
		}
		return new FileOutputWriter();
	}

	private Queue<UrlFileHandling> combineUrlFile(List<URL> urlList,
			List<File> files) {
		
		Queue<UrlFileHandling> result = new ArrayDeque<UrlFileHandling>();
		for (int i = 0; i < urlList.size(); i++) {
			if (files.isEmpty()) {
				result.add(new UrlFileHandling(urlList.get(i), new UrlFileHandling.StdOut("stdout")));
			} else {
				result.add(new UrlFileHandling(urlList.get(i), files.get(i)));
			}
		}
		return result;
	}


	/**
	 * @param optionList
	 * @return
	 */
	private String proxy(List<OptionEntity> optionList) {
		// TODO 複数対応
		for (OptionEntity option : optionList) {
			if (option.getType().equals(Option.PROXYHOST)) {
				if (option.getValue() == null) {
					throw new IllegalArgumentException("The value for proxy is null.");
				}
				return option.getValue();
			}
		}
		return null;
	}

	/**
	 * @param optionList
	 * @return
	 */
	private boolean isVerbose(List<OptionEntity> optionList) {
		for (OptionEntity option : optionList) {
			if (option.getType().equals(Option.VERBOSE)) {
				return true;
			}
		}
		return false;
	}

	private Map<String, String> getHeaders(List<OptionEntity> optionList) {
		Map<String, String> result = new HashMap<String, String>();
		for (OptionEntity option : optionList) {
			if (option.getType().equals(Option.HEADER)) {
				String[] keyValuePair = parseHeader(option.getValue());
				result.put(keyValuePair[0], keyValuePair[1]);
			}
		}
		return result;
	}
	private String[] parseHeader(String str) {
		String[] result = str.split(":");
		if (result.length > 2) {
			throw new IllegalArgumentException("Illegal Header Value: " + str);
		}
		return result;
	}
	
	private String selectCommand(List<OptionEntity> optionList) {
		// TODO 二つ以上来るとあと勝ちになってる
		String command = null;
		for (OptionEntity option : optionList) {
			if (option.getType().equals(Option.COMMAND)) {
				command = option.getValue();
			}
		}
		return command;
	}
	
	private List<File> getFileList(List<OptionEntity> optionList) {
		List<File> result = new ArrayList<File>();
		for (OptionEntity option : optionList) {
			if (option.getType().equals(Option.FILE)) {
				result.add(new File(option.getValue()));
			}
		}
		if (result.isEmpty()) {
			return Collections.emptyList();
		}
		return result;
	}

}
