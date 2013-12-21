/**
 * 
 */
package org.acme;

import static java.util.Arrays.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.cotrix.configuration.ConfigurationLocator;
import org.cotrix.configuration.LocationProvider;
import org.junit.Test;


public class LocatorTest {
	
	@Test(expected=IllegalStateException.class)
	public void missingLocation() throws IOException {
		
		new ConfigurationLocator(location(null)).locate();
	}
	
	@Test(expected=IllegalStateException.class)
	public void invalidLocation() throws IOException {
		
		new ConfigurationLocator(location("bad")).locate();
	}
	
	@Test
	public void skipLocation() throws IOException {
		
		new ConfigurationLocator(location(LocationProvider.PASS)).locate();
	}
	
	
	@Test
	public void jndiTest() throws IOException {
		
		List<LocationProvider> list = new ArrayList<LocationProvider>(); 
		list.add(new LocationProvider.Jndi());
		new ConfigurationLocator(list).locate();
	}
	
	
	//helper
	List<LocationProvider> location(final String s) {
		
		LocationProvider p = new LocationProvider() {
			
			@Override
			public String location() {
				return s;
			}
		};
		
		return asList(p);
	}
}
