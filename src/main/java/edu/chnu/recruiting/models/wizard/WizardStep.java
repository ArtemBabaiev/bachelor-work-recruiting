package edu.chnu.recruiting.models.wizard;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class WizardStep {
	private String id;
	private String name;
	private int orderIndex;
	private List<WizardField> fields = new ArrayList<WizardField>();
	
	public void addField(WizardField field) {
		this.fields.add(field);
	}
}
