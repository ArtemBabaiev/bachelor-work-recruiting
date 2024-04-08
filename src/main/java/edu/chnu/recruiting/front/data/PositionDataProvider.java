package edu.chnu.recruiting.front.data;

import java.util.stream.Stream;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.viewModels.PositionViewModel;
import edu.chnu.recruiting.services.PositionService;
import edu.chnu.recruiting.utils.VaadinToSpring;

public class PositionDataProvider extends AbstractBackEndDataProvider<PositionViewModel, IFilter<Position>> {

	private PositionService positionService;

	public PositionDataProvider(PositionService positionService) {
		super();
		this.positionService = positionService;
	}

	@Override
	protected Stream<PositionViewModel> fetchFromBackEnd(Query<PositionViewModel, IFilter<Position>> query) {
		IFilter<Position> filter = query.getFilter().get();
		Specification<Position> spec = filter.getSpecification();
		PageRequest page = PageRequest.of(query.getPage(), query.getPageSize(), VaadinToSpring.convert(query.getSortOrders()));
		return this.positionService.getAllBy(spec, page).stream();
	}

	@Override
	protected int sizeInBackEnd(Query<PositionViewModel, IFilter<Position>> query) {
		Specification<Position> spec = query.getFilter().get().getSpecification();
		return (int) positionService.countBy(spec);
	}

}
