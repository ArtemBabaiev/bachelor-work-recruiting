package edu.chnu.recruiting.specification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SearchCriteria {
	private String key;
    private String operation;
    private Object value;
    
    
}
