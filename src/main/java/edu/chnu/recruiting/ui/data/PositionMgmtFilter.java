package edu.chnu.recruiting.ui.data;

import org.springframework.data.jpa.domain.Specification;

import edu.chnu.recruiting.models.Company;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.specification.GenericSpecification;
import edu.chnu.recruiting.specification.SearchCriteria;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionMgmtFilter implements IFilter<Position> {
	private SearchCriteria name = new SearchCriteria("name", "like", "");
	private SearchCriteria company = new SearchCriteria("company", ":", null);

	public PositionMgmtFilter(Company company) {
		this.company.setValue(company);
	}

	public void setNameCriteria(String value) {
		this.name.setValue(value);
	}

	public Specification<Position> getSpecification() {
		return GenericSpecification.<Position>of(name).and(GenericSpecification.<Position>of(company));
	}
}
