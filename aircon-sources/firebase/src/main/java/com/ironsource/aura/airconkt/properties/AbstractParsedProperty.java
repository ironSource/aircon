package com.ironsource.aura.airconkt.properties;

/**
 * Created on 11/11/2018.
 */
public abstract class AbstractParsedProperty <T>
		extends AbstractProperty<T> {

	public AbstractParsedProperty(final String key) {
		super(key);
	}

	@Override
	public final T convertToType(String property) {
		try {
			return parse(property);
		} catch (Exception e) {
			return null;
		}
	}

	protected abstract T parse(String property);
}
