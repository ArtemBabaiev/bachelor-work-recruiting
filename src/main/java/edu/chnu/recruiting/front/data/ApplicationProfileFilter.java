package edu.chnu.recruiting.front.data;

import org.springframework.data.jpa.domain.Specification;

import edu.chnu.recruiting.models.Application;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.specification.GenericSpecification;
import edu.chnu.recruiting.specification.JoinSearchCriteria;
import edu.chnu.recruiting.specification.SearchCriteria;

public class ApplicationProfileFilter implements IFilter<Application>{
	private JoinSearchCriteria positionName = new JoinSearchCriteria("position", "name", "like", null);
	private SearchCriteria status = new SearchCriteria("status", "!=", null);
	private SearchCriteria user = new SearchCriteria("user", ":", null);
	public ApplicationProfileFilter(User user) {
		this.user.setValue(user);
	}
	
	public void setStatus(String value) {
		this.status.setValue(value);
		if (value == null) {
			this.status.setOperation("!=");
		} else {
			this.status.setOperation(":");
		}
	}
	
	public void setPositionName(String value) {
		this.positionName.setValue(value);
	}

	@Override
	public Specification<Application> getSpecification() {
		 return GenericSpecification.<Application>of(user)
			.and(GenericSpecification.<Application>of(status))
			.and(GenericSpecification.<Application>of(positionName));
	}

}
