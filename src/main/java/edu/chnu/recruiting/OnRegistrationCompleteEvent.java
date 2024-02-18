package edu.chnu.recruiting;

import org.springframework.context.ApplicationEvent;

import edu.chnu.recruiting.models.security.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
	private User user;

	public OnRegistrationCompleteEvent(User user) {
		super(user);
		this.user = user;
	}
}
