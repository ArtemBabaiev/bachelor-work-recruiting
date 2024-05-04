package edu.chnu.recruiting.ui.exceptionHandlers;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import edu.chnu.recruiting.exceptions.ResourceNotFoundException;
import edu.chnu.recruiting.ui.MainLayout;
import jakarta.servlet.http.HttpServletResponse;

@Tag(Tag.DIV)
@ParentLayout(MainLayout.class)
@AnonymousAllowed
public class ResourceNotFoundExceptionHandler extends Component
implements HasErrorParameter<ResourceNotFoundException>{

	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<ResourceNotFoundException> parameter) {
		getElement().setText(
	            "Resource not found");
	        return HttpServletResponse.SC_NOT_FOUND;
	}

}
