package edu.chnu.recruiting.front.components.picker;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.textfield.NumberField;

public class DecimalRangePicker extends CustomField<PickerRange<Double>> {
	NumberField start;
	NumberField end;


    public DecimalRangePicker(String label) {
        this();
        setLabel(label);
    }

	
	public DecimalRangePicker() {
		super();
		start = new NumberField();
        start.setPlaceholder("0");
        // Sets title for screen readers
        start.getElement().executeJs(
                "this.focusElement.setAttribute('title', 'Min salary')");

        end = new NumberField();
        end.setPlaceholder("100");
        end.getElement().executeJs(
                "this.focusElement.setAttribute('title', 'Max salary')");
        setModelValue(new PickerRange<Double>(), false);
        add(start, new Text(" – "), end);
	}
	
	@Override
	protected PickerRange<Double> generateModelValue() {
		return new PickerRange<Double>(start.getValue(), end.getValue());
	}

	@Override
	protected void setPresentationValue(PickerRange<Double> newPresentationValue) {
		start.setValue(newPresentationValue.getStart());
		end.setValue(newPresentationValue.getEnd());
	}
	
	
}
