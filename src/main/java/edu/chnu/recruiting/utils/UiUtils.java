package edu.chnu.recruiting.utils;

import com.vaadin.flow.data.value.HasValueChangeMode;
import com.vaadin.flow.data.value.ValueChangeMode;

public class UiUtils {
	public static void setValueChangeMode(ValueChangeMode mode, HasValueChangeMode... hasValueChangeModes) {
		for (HasValueChangeMode hasMode : hasValueChangeModes) {
			hasMode.setValueChangeMode(mode);
		}
	}
}
