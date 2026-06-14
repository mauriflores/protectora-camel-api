package com.uminas.protectora.routes;

import com.uminas.protectora.model.WhatsAppMessage;
import com.uminas.protectora.model.RescueReport;
import com.uminas.protectora.service.ConversationStateService;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class WebhookRoute extends RouteBuilder {

    private final ConversationStateService conversationStateService;

    public WebhookRoute(ConversationStateService conversationStateService) {
        this.conversationStateService = conversationStateService;
    }

    @Override
    public void configure() {
        from("platform-http:/webhook/whatsapp?httpMethodRestrict=GET")
                .routeId("webhook-whatsapp-verification-route")
                .process(exchange -> {

                    String mode = exchange.getIn().getHeader("hub.mode", String.class);
                    String token = exchange.getIn().getHeader("hub.verify_token", String.class);
                    String challenge = exchange.getIn().getHeader("hub.challenge", String.class);

                    String verifyToken = "protectora-token";

                    System.out.println("Verificando webhook Meta...");
                    System.out.println("hub.mode: " + mode);
                    System.out.println("hub.verify_token: " + token);
                    System.out.println("hub.challenge: " + challenge);

                    if ("subscribe".equals(mode) && verifyToken.equals(token)) {
                        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
                        exchange.getMessage().setBody(challenge);
                    } else {
                        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 403);
                        exchange.getMessage().setBody("Forbidden");
                    }
                });

        from("platform-http:/webhook/whatsapp?httpMethodRestrict=POST")
                .routeId("webhook-whatsapp-route")

                .convertBodyTo(String.class)

                .process(exchange -> {
                    String rawBody = exchange.getMessage().getBody(String.class);

                    System.out.println("=== BODY REAL RECIBIDO DESDE META ===");
                    System.out.println(rawBody);
                    System.out.println("====================================");

                    exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
                    exchange.getMessage().setBody("EVENT_RECEIVED");
                });
    }
}