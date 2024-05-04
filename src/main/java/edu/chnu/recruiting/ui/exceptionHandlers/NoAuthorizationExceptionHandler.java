package edu.chnu.recruiting.ui.exceptionHandlers;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import edu.chnu.recruiting.exceptions.NoAuthorizationException;
import edu.chnu.recruiting.ui.MainLayout;
import jakarta.servlet.http.HttpServletResponse;

@Tag(Tag.DIV)
@ParentLayout(MainLayout.class)
@AnonymousAllowed
public class NoAuthorizationExceptionHandler extends Component implements HasErrorParameter<NoAuthorizationException> {

	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NoAuthorizationException> parameter) {
		getElement().setText("Unauthorized action performed");
		return HttpServletResponse.SC_UNAUTHORIZED;
	}

}
