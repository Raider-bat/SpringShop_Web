package kz.gexa.spring.shop.session;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.servlet.http.HttpSessionEvent;

@Configuration
public class SessionEventListener extends HttpSessionEventPublisher{
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        super.sessionCreated(event);
        event.getSession().setMaxInactiveInterval(120*60);
    }
}
