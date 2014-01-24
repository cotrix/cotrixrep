package org.cotrix.security.tokens;


public class NameAndPassword {

	public NameAndPassword(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}
	
	private final String name;
	private final String password;
	
	public String name() {
		return name;
	}
	
	public String password() {
		return password;
	}

	@Override
	public String toString() {
		return "NameAndPassword [name=" + name + ", password=...]";
	}
	
	
}
