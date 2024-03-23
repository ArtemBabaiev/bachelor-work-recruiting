package edu.chnu.recruiting.front.components.picker;

import java.util.ArrayList;
import java.util.Currency;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.textfield.NumberField;

public class SalaryRangePicker extends CustomField<SalaryRange> {
	NumberField start;
	NumberField end;
	ComboBox<String> code;

	public SalaryRangePicker(String label) {
		this();
		setLabel(label);
	}

	public SalaryRangePicker() {
		super();
		start = new NumberField();
		start.setPlaceholder("0");
		// Sets title for screen readers
		start.getElement().executeJs("this.focusElement.setAttribute('title', 'Min salary')");

		end = new NumberField();
		end.setPlaceholder("100");
		end.getElement().executeJs("this.focusElement.setAttribute('title', 'Max salary')");

		code = new ComboBox<String>();
		code.getElement().executeJs("this.focusElement.setAttribute('title', 'CurrencyCode')");
		code.setItems(Currency.getAvailableCurrencies().stream().map(c -> c.getCurrencyCode()).toList());
		code.setAllowCustomValue(false);
		
		
		setModelValue(new SalaryRange(), false);
		add(start, new Text(" – "), end, new Text(" – "), code);
	}

	@Override
	protected SalaryRange generateModelValue() {
		return new SalaryRange(start.getValue(), end.getValue(), "USD");
	}

	@Override
	protected void setPresentationValue(SalaryRange newPresentationValue) {
		start.setValue(newPresentationValue.getStart());
		end.setValue(newPresentationValue.getEnd());
		code.setValue(newPresentationValue.getCurrencyCode());
	}

}
