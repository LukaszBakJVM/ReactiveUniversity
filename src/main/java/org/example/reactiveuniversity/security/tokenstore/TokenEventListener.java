package org.example.reactiveuniversity.security.tokenstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TokenEventListener {
    private final TokenStore tokenStore;
    private final Logger logger =  LoggerFactory.getLogger(TokenEventListener.class);

    public TokenEventListener(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }
    @EventListener
    public void onTokenUpdated(TokenUpdatedEvent event) {
        logger.info("Reading file: {}",event.getFileName());
        tokenStore.read();
    }
}