/**
 * 
 */
package org.cotrix.web.common.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.ValueBoxBase;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ValueBoxBaseValidator<T> {
	
	private static final Validator<Object> NO_VALIDATION = new Validator<Object>() {
		
		@Override
		public ValidationResult validate(Object value) {
			return VALID;
		}
	};
	
	@SuppressWarnings("unchecked")
	public static <V> Validator<V> noValidation() {
		return (Validator<V>) NO_VALIDATION;
	}
	
	public static final ValidationResult VALID = new ValidationResult(null);
	public static ValidationResult invalid(String cause) {
		return new ValidationResult(cause);
	}
	
	public static class ValidationResult {
		private String cause;

		private ValidationResult(String cause) {
			this.cause = cause;
		}
	}
	
	public interface Validator<T> {
		public ValidationResult validate(T value);
	}
	
	private ValueBoxBase<T> valueBoxBase;
	private List<Validator<T>> validators = new ArrayList<Validator<T>>();
	private String invalidStyle;
	
	public ValueBoxBaseValidator(ValueBoxBase<T> valueBoxBase, String invalidStyle) {
		this.valueBoxBase = valueBoxBase;
		this.invalidStyle = invalidStyle;
		
		valueBoxBase.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				validate();
			}
		});
	}
	
	public void addValidator(Validator<T> validator) {
		validators.add(validator);
	}
	
	public void removeValidator(Validator<T> validator) {
		validators.remove(validator);
	}
	
	public void cleanValidators() {
		validators.clear();
	}
	
	private void validate() {
		T value = valueBoxBase.getValue();
		for (Validator<T> validator:validators) {
			ValidationResult checkResult = validator.validate(value);
			if (checkResult != VALID) {
				setInvalid(checkResult.cause);
				return;
			}
		}
		setValid();
	}
	
	private void setValid() {
		valueBoxBase.setStyleName(invalidStyle, false);
		valueBoxBase.setTitle("");
	}
	
	private void setInvalid(String cause) {
		valueBoxBase.setStyleName(invalidStyle, true);
		valueBoxBase.setTitle(cause);
	}
	
	
}
