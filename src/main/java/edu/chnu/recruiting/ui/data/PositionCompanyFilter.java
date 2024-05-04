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
public class PositionCompanyFilter implements IFilter<Position> {
	private SearchCriteria name = new SearchCriteria("name", "like", "");
	private SearchCriteria activeOnly = new SearchCriteria("active", ":", true);
	private JoinSearchCriteria companyId = new JoinSearchCriteria("company", "id", ":", null);

	public PositionCompanyFilter(Long companyId) {
		this.setCompanyIdCriteria(companyId);
	}
	
	public void setNameCriteria(String value) {
		this.name.setValue(value);
	}

	public void setCompanyIdCriteria(Long id) {
		this.companyId.setValue(id);
	}

	public Specification<Position> getSpecification() {
		return GenericSpecification.<Position>of(companyId).and(GenericSpecification.<Position>of(name))
				.and(GenericSpecification.<Position>of(activeOnly));
	}
}