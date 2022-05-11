package com.epam.esm.event.listener;

import com.epam.esm.domain.User;
import com.epam.esm.event.RegistrationCompleteEvent;
import com.epam.esm.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * RegistrationCompleteEventListener
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@Slf4j
@Component
@AllArgsConstructor
public class RegistrationCompleteEventListener
        implements ApplicationListener<RegistrationCompleteEvent> {

    private final UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //Create verification token for the user with link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user);
        //send mail to user
        String url =
                event.getApplicationUrl()
                        + "/verifyRegistration?token="
                        + token;

        //sendVerificationEmail()
        log.info("Link to verify account: {}", url);
    }
}
