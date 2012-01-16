/**
 * 
 */
package shtaag.network.curl.impl.option;

/**
 * @author takei_s
 * @date 2011/12/25
 */
public abstract class OptionEntity {
	
	protected Option type;
	
	public abstract Option getType();
	
	public abstract String getValue();

}
