/**
 * 
 */
package shtaag.network.curl.impl.option;

/**
 * @author takei_s
 * @date 2011/12/25
 */
public class KeyValueOptionEntity extends OptionEntity{
	
	private final String value;
	/**
	 * @param type
	 * @param value
	 */
	public KeyValueOptionEntity(Option type, String value) {
		super();
		this.type = type;
		this.value = value;
	}

	@Override
	public Option getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
	

}
