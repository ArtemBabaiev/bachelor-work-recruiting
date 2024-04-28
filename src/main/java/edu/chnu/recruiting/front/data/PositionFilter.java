package edu.chnu.recruiting.front.data;

import org.springframework.data.jpa.domain.Specification;

import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.specification.GenericSpecification;
import edu.chnu.recruiting.specification.SearchCriteria;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionFilter implements IFilter<Position>{
	private SearchCriteria name = new SearchCriteria("name", "like", "");
	private SearchCriteria activeOnly = new SearchCriteria("active", ":", true);

	public void setNameCriteria(String value) {
		this.name.setValue(value);
	}

	public Specification<Position> getSpecification() {
		return GenericSpecification.<Position>of(name).and(GenericSpecification.<Position>of(activeOnly));
	}
}
