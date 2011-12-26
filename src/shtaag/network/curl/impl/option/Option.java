/**
 * 
 */
package shtaag.network.curl.impl.option;

/**
 * @author takei_s
 * @date 2011/12/25
 */
public enum Option {
	
	HEADER("-H")
	, COMMAND("-X")
	, FILE("-o")
	, VERBOSE("-v")
	, LOCATION("-L")
	, PROXYHOST("-x")
	, DATA("-d")
	, FORM("-F");
	
	private String name;
	
	private Option(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
	
	public static Option check(String str) {
		if (str.equals(Option.HEADER.toString())) return HEADER;
		if (str.equals(Option.COMMAND.toString())) return COMMAND;
		if (str.equals(Option.FILE.toString())) return FILE;
		if (str.equals(Option.VERBOSE.toString())) return VERBOSE;
		if (str.equals(Option.LOCATION.toString())) return LOCATION;
		if (str.equals(Option.PROXYHOST.toString())) return PROXYHOST;
		if (str.equals(Option.DATA.toString())) return DATA;
		if (str.equals(Option.FORM.toString())) return FORM;
		
		return null;
	}

}
