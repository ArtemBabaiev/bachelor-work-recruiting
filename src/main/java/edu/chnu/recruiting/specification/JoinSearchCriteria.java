package edu.chnu.recruiting.specification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinSearchCriteria extends SearchCriteria {
	private String joinAttribute;
	
	public JoinSearchCriteria(String joinAttribute, String key, String operation, Object value) {
		super(key, operation, value);
		this.joinAttribute = joinAttribute;
	}

}
