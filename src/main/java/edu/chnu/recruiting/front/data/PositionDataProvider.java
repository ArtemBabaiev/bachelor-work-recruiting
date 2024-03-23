package edu.chnu.recruiting.front.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;

import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.services.PositionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PositionDataProvider extends AbstractBackEndDataProvider<Position, PositionFilter>{
	
	private PositionService positionService;
	
	public PositionDataProvider(PositionService positionService){
		super();
		this.positionService = positionService;
	}

	@Override
	protected Stream<Position> fetchFromBackEnd(Query<Position, PositionFilter> query) {
		String nameSearch = query.getFilter().get().getSearchTerm();
		
		var page = PageRequest.of(query.getPage(), query.getPageSize(), collectSorts(query.getSortOrders()));
		log.info("The query");
		return this.positionService.getFilteredPositions(nameSearch, page).stream();
	}

	@Override
	protected int sizeInBackEnd(Query<Position, PositionFilter> query) {
		log.info("Size");
		return (int) fetchFromBackEnd(query).count();
	}

	private Sort collectSorts(List<QuerySortOrder> vaadinOrders) {
		List<Order> orders = new ArrayList<Order>();
		for (QuerySortOrder querySortOrder : vaadinOrders) {
			Order order = new Order(
					querySortOrder.getDirection() == SortDirection.ASCENDING? Direction.ASC: Direction.DESC, 
					querySortOrder.getSorted());
			orders.add(order);
			
		}
		
		return Sort.by(orders);
		
	}
	
}
