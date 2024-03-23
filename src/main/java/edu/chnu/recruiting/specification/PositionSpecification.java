package edu.chnu.recruiting.specification;

import org.springframework.data.jpa.domain.Specification;

import edu.chnu.recruiting.models.Position;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class PositionSpecification implements Specification<Position> {

	private SearchCriteria criteria;

	public PositionSpecification(SearchCriteria criteria) {
		super();
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Position> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (criteria.getOperation().equalsIgnoreCase(":")) {
			if (root.get(criteria.getKey()).getJavaType() == String.class) {
				return criteriaBuilder.like(root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
			} else {
				return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
			}
		}
		// TODO Auto-generated method stub
		return null;
	}

}
