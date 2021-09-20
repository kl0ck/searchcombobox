package com.github.kl0ck.searchcombobox;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author kl0ck
 *
 * @param <K>
 * @param <V>
 */
public class ComboItem<K, V> {
	
	private K key;
	private V value;
	
	public ComboItem() {
		key = null;
		value = null;
	}
	
	public ComboItem(K key, V value) {
		this.key = Objects.requireNonNull(key);
		this.value = Objects.requireNonNull(value);
	}
	
	@SuppressWarnings("unchecked")
	public ComboItem(Object item, String propertyKey, String propertyValue) {
		Objects.requireNonNull(item);
		Objects.requireNonNull(propertyKey);
		Objects.requireNonNull(propertyValue);
		
		try {
			
			for (Field field : item.getClass().getDeclaredFields()) {
				String fieldName = field.getName();
				Object fieldValue = field.get(item);
				
				if (Objects.equals(fieldName, propertyKey)) {
					key = (K) fieldValue;
				}
				
				if (Objects.equals(fieldName, propertyValue)) {
					value = (V) fieldValue;
				}
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <K, V> ComboItem<K, V> build() {
		return new ComboItem<>();
	}
	
	public static <K, V> ComboItem<K, V> build(K key, V value) {
		return new ComboItem<>(key, value);
	}
	
	public static ComboItem<?, ?> build(Object item, String propertyKey, String propertyValue) {
		return new ComboItem<>(item, propertyKey, propertyValue);
	}
	
	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}
	
	public boolean isBlank() {
		return key == null && value == null;
	}

	@Override
	public String toString() {
		return value != null ? value.toString() : "";
	}
	
}
