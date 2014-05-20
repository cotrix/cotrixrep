/**
 * 
 */
package org.cotrix.web.common.client.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class InstanceMap<K,V> {
	
	private List<Entry<K,V>> entries;
	
	public InstanceMap() {
		entries = new ArrayList<Entry<K,V>>();
	}
	
	public void put(K key, V value) {
		SimpleEntry<K, V> entry = new SimpleEntry<K, V>(key, value);
		entries.add(entry);
	}
	
	public V get(K key) {
		for (Entry<K, V> entry:entries) if (entry.getKey() == key) return entry.getValue();
		return null;
	}
	
	public K getByValue(V value) {
		for (Entry<K, V> entry:entries) if (entry.getValue() == value) return entry.getKey();
		return null;
	}
	
	public V remove(K key) {
		Iterator<Entry<K, V>> entriesIterator = entries.iterator();
		while(entriesIterator.hasNext()) {
			Entry<K, V> entry = entriesIterator.next();
			if (entry.getKey() == key) {
				entriesIterator.remove();
				return entry.getValue();
			}
		}
		return null;
	}
	
	public void clear() {
		entries.clear();
	}
	
	private static class SimpleEntry<K,V> implements Entry<K, V> {
		
		private K key;
		private V value;

		/**
		 * @param key
		 * @param value
		 */
		private SimpleEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			V old = this.value;
			this.value = value;
			return old;
		}
		
	}
}
