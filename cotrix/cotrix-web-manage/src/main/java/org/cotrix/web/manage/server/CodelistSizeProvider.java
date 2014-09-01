/**
 * 
 */
package org.cotrix.web.manage.server;

import java.util.Iterator;

import org.cotrix.domain.codelist.Code;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodelistSizeProvider {
	
	public int getSize();
	
	public static class ConstantCodelistSizeProvider implements CodelistSizeProvider {

		int size;

		public ConstantCodelistSizeProvider(int size) {
			this.size = size;
		}

		@Override
		public int getSize() {
			return size;
		}
		
	}
	
	public static class CounterCodelistSizeProvider implements CodelistSizeProvider {

		Iterable<Code> iterable;

		public CounterCodelistSizeProvider(Iterable<Code> iterable) {
			this.iterable = iterable;
		}

		@Override
		public int getSize() {
			int size = 0;
			for (Iterator<Code> it=iterable.iterator(); it.hasNext(); it.next()) size++;
			return size;
		}
		
	}
	
	

}
