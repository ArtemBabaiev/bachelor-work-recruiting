package edu.chnu.recruiting.front.data;

import org.springframework.data.jpa.domain.Specification;

import edu.chnu.recruiting.models.ApplicationSummary;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.specification.GenericSpecification;
import edu.chnu.recruiting.specification.SearchCriteria;

public class ApplicationMgmtFilter implements IFilter<ApplicationSummary>{
	private SearchCriteria firstName = new SearchCriteria("firstName", "like", "");
	private SearchCriteria lastName = new SearchCriteria("lastName", "like", "");
	private SearchCriteria position = new SearchCriteria("position", ":", null);
	private SearchCriteria status = new SearchCriteria("status", "!=", null);
	
	public void setName(String value) {
		this.firstName.setValue(value);
		this.lastName.setValue(value);
	}
	
	public void setPosition(Position value) {
		this.position.setValue(value);
	}
	
	public void setStatus(String value) {
		this.status.setValue(value);
		if (value == null) {
			this.status.setOperation("!=");
		} else {
			this.status.setOperation(":");
		}
	}

	public Specification<ApplicationSummary> getSpecification() {
		return GenericSpecification.<ApplicationSummary>of(firstName)
				.or(GenericSpecification.<ApplicationSummary>of(lastName))
				.and(GenericSpecification.<ApplicationSummary>of(status))
				.and(GenericSpecification.<ApplicationSummary>of(position))
				;
	}
}
