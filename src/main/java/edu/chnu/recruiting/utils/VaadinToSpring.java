package edu.chnu.recruiting.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;

import edu.chnu.recruiting.front.data.PositionFilter;
import edu.chnu.recruiting.models.Position;

public class VaadinToSpring {
	public static Sort convert(List<QuerySortOrder> vaadinOrders) {
		List<Order> orders = new ArrayList<Order>();
		for (QuerySortOrder querySortOrder : vaadinOrders) {
			Order order = new Order(
					querySortOrder.getDirection() == SortDirection.ASCENDING ? Direction.ASC : Direction.DESC,
					querySortOrder.getSorted());
			orders.add(order);

		}

		return Sort.by(orders);

	}
}
