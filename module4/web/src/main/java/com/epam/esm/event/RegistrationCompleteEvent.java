package com.epam.esm.event;

import com.epam.esm.domain.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * RegistrationCompleteEvent
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@Getter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private User user;
    private String applicationUrl;

    public RegistrationCompleteEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
