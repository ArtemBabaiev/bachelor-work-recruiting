package edu.chnu.recruiting.models.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;

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
	
	public Object getFieldValue(String fieldId) {
		return this.fields.stream().filter(f -> f.getId().equals(fieldId)).findFirst().get().getUserValue();
	}
}
