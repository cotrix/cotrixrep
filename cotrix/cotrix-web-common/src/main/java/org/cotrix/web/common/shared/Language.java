/**
 * 
 */
package org.cotrix.web.common.shared;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public enum Language {
	//FIXME NONE SHOULD HAVE null as code
	NONE("none", ""),
	ARABIC("Arabic", "ar"),
	CHINESE("Chinese", "zh"),
	ENGLISH("English", "en"),
	FRENCH("French", "fr"),
	RUSSIAN("Russian", "ru"),
	SPANISH("Spanish", "es");
	
	private String name;
	private String code;
	
	private Language(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
	
	public static Language fromCode(String code) {
		for (Language language:values()) if (language.code.equals(code)) return language;
		throw new IllegalArgumentException("Unknwon language code "+code);
	}
}
