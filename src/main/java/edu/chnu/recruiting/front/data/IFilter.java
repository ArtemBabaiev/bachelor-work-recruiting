package edu.chnu.recruiting.front.data;

import org.springframework.data.jpa.domain.Specification;

public interface IFilter <T> {
	public Specification<T> getSpecification();
}
