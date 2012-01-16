/**
 * 
 */
package shtaag.network.curl.impl.option;

/**
 * @author takei_s
 * @date 2012/01/15
 */
public class KeyOnlyOptionEntity extends OptionEntity {

	
	public KeyOnlyOptionEntity(Option type) {
		this.type = type;
	}
	@Override
	public Option getType() {
		return type;
	}

	@Override
	public String getValue() {
		throw new UnsupportedOperationException();
	}

}
