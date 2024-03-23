package edu.chnu.recruiting.front.views.position;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import edu.chnu.recruiting.front.data.PositionDataProvider;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.front.views.ErrorViews;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.services.PositionService;

@PageTitle("Positions listing")
@Route(value = "positions/:posId", layout = MainLayout.class)
@AnonymousAllowed
public class PositionView extends VerticalLayout implements BeforeEnterObserver {

    private Long posId = null;
    private Position model;

    private PositionService positionService;

	public PositionView(PositionService positionService) {
		this.positionService = positionService;
	}
    
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
    	this.posId = Long.parseLong(event.getRouteParameters().get("posId").get());
    	try {
			model = positionService.getPosition(posId);
			configure();
		} catch (NotFoundException e) {
			add(ErrorViews.get404(e.getMessage()));
		}
    }

	private void configure() {
		add(new H2(model.getName()));
		
		
	}
}