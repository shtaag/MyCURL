/**
 * 
 */
package shtaag.network.curl.impl.parser;

import java.util.ArrayList;
import java.util.List;

import shtaag.network.curl.impl.option.KeyOnlyOptionEntity;
import shtaag.network.curl.impl.option.KeyValueOptionEntity;
import shtaag.network.curl.impl.option.Option;
import shtaag.network.curl.impl.option.OptionEntity;

/**
 * @author takei_s
 * @date 2011/12/25
 */
public class OptionParser {
	
	public static List<OptionEntity> parse(String[] str) {
	
		List<OptionEntity> resultList = new ArrayList<OptionEntity>();
		for (int i = 0; i < str.length; i++) {
			Option option = null;
			if ((option = Option.check(str[i])) != null) {
				OptionEntity entity = null;
				switch (option) {
					case LOCATION: entity = new KeyOnlyOptionEntity(option); break;
					case VERBOSE: entity = new KeyOnlyOptionEntity(option); break;
					default: {
						if (i+1 >= str.length) {
							throw new RuntimeException("Invalid input. no Value for: " + str[i]);
						}
						if (Option.check(str[i+1]) != null) {
							// 連続してoption指定されているのでvalueがない
							throw new RuntimeException("Invalid input. no Value for: " + str[i]);
						}
						entity = new KeyValueOptionEntity(option, str[i + 1]);
					}
				}
				resultList.add(entity);
			}
		}
		return resultList;
	}
}
