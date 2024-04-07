package edu.chnu.recruiting.specification;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class GenericSpecification<T> implements Specification<T> {
	protected SearchCriteria criteria;

	public GenericSpecification(SearchCriteria criteria) {
		super();
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (criteria.getOperation().equalsIgnoreCase(">")) {
			return this.greaterThan(root, query, builder);
		} else if (criteria.getOperation().equalsIgnoreCase("<")) {
			return this.lessThan(root, query, builder);
		} else if (criteria.getOperation().equalsIgnoreCase("!=")) {
			if (criteria.getValue() == null) {
				return this.notNull(root, query, builder);
			}
			return this.notEqual(root, query, builder);
		} else if (criteria.getOperation().equalsIgnoreCase(":")) {
			return this.equal(root, query, builder);
		} else if (criteria.getOperation().equalsIgnoreCase("like")) {
			return this.like(root, query, builder);
		}
		return null;
	}

	private Predicate greaterThan(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (criteria instanceof JoinSearchCriteria jsc) {
			Join<?, T> join = root.join(jsc.getJoinAttribute());
			return builder.greaterThan(join.<String>get(jsc.getKey()), criteria.getValue().toString());
		} else {
			return builder.greaterThan(root.<String>get(criteria.getKey()), criteria.getValue().toString());
		}
	}

	private Predicate lessThan(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (criteria instanceof JoinSearchCriteria jsc) {
			Join<?, T> join = root.join(jsc.getJoinAttribute());
			return builder.lessThan(join.<String>get(jsc.getKey()), criteria.getValue().toString());
		} else {
			return builder.lessThan(root.<String>get(criteria.getKey()), criteria.getValue().toString());
		}
	}

	private Predicate notEqual(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (criteria instanceof JoinSearchCriteria jsc) {
			Join<?, T> join = root.join(jsc.getJoinAttribute());
			return builder.notEqual(join.get(jsc.getKey()), criteria.getValue());
		} else {
			return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
		}
	}

	private Predicate notNull(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (criteria instanceof JoinSearchCriteria jsc) {
			Join<?, T> join = root.join(jsc.getJoinAttribute());
			return builder.isNotNull(join.get(jsc.getKey()));
		} else {
			return builder.isNotNull(root.get(criteria.getKey()));
		}
	}

	private Predicate equal(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (criteria instanceof JoinSearchCriteria jsc) {
			Join<?, T> join = root.join(jsc.getJoinAttribute());
			return builder.equal(join.get(jsc.getKey()), criteria.getValue());
		} else {
			return builder.equal(root.get(criteria.getKey()), criteria.getValue());
		}
	}

	private Predicate like(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (criteria instanceof JoinSearchCriteria jsc) {
			Join<Object, T> join = root.join(jsc.getJoinAttribute());
			return builder.like(join.<String>get(jsc.getKey()), likePattern(criteria.getValue()));
		} else {
			return builder.like(root.<String>get(criteria.getKey()), likePattern(criteria.getValue()));
		}
	}

	private String likePattern(Object value) {
		if (value == null) {
			return "%%";
		}
		return "%" + value + "%";
	}
	
	public static <T> GenericSpecification<T> of(SearchCriteria criteria) {
		return new GenericSpecification<T>(criteria);
	}
}
