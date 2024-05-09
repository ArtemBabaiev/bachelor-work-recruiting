package edu.chnu.recruiting.ui.data;

import org.springframework.data.jpa.domain.Specification;

import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.specification.GenericSpecification;
import edu.chnu.recruiting.specification.JoinSearchCriteria;
import edu.chnu.recruiting.specification.SearchCriteria;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionFilter implements IFilter<Position> {
	private SearchCriteria name = new SearchCriteria("name", "like", "");
	private SearchCriteria activeOnly = new SearchCriteria("active", ":", true);
	private SearchCriteria employmentType = new SearchCriteria("employmentType", "!=", null);
	private JoinSearchCriteria companyName = new JoinSearchCriteria("company", "name", "like", "");

	public void setNameCriteria(String value) {
		this.name.setValue(value);
	}

	public void setCompanyNameCriteria(String value) {
		this.companyName.setValue(value);
	}

	public void setEmploymentTypeCriteria(String value) {
		this.employmentType.setValue(value);
		if (value == null) {
			employmentType.setOperation("!=");
		} else {
			employmentType.setOperation(":");
		}
	}

	public Specification<Position> getSpecification() {
		return GenericSpecification.<Position>of(name).and(GenericSpecification.<Position>of(activeOnly))
				.and(GenericSpecification.<Position>of(companyName))
				.and(GenericSpecification.<Position>of(employmentType));
	}
}
