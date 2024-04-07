package edu.chnu.recruiting.models;

import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.utils.WizardConverterJson;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "application")
public class ApplicationFull extends Application {
	@Convert(converter = WizardConverterJson.class)
	@Column(name = "wizard_data", columnDefinition="JSON")
	private Wizard wizardData;
}
