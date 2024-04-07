package edu.chnu.recruiting.models.wizard;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class WizardStep {
	private Integer id;
	private String name;
	private List<WizardField> fields = new ArrayList<WizardField>();

	public void addField(WizardField field) {
		this.fields.add(field);
	}

	@JsonIgnore
	public Object getFieldValue(int fieldId) {
		return this.fields.stream().filter(f -> f.getId().equals(fieldId)).findFirst().get().getUserValue();
	}

	public void setFieldValue(int fieldId, Object value) {
		this.fields.stream().filter(f -> f.getId().equals(fieldId)).findFirst().get().setUserValue(value);
	}
}
