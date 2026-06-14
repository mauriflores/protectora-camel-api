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

                .unmarshal().json(WhatsAppMessage.class)

                .process(exchange -> {
                    WhatsAppMessage message =
                            exchange.getMessage().getBody(WhatsAppMessage.class);

                    String phone = message.getPhone();
                    String text = message.getMessage();

                    String currentState = conversationStateService.getState(phone);

                    System.out.println("Telefono: " + phone);
                    System.out.println("Mensaje: " + text);
                    System.out.println("Estado actual: " + currentState);

                    if (currentState == null) {
                        conversationStateService.saveState(phone, "WAITING_DESCRIPTION");

                        exchange.getMessage().setBody("""
                        {
                          "respuesta": "Hola, contame qué pasó con el animal."
                        }
                        """);

                    } else if ("WAITING_DESCRIPTION".equals(currentState)) {

                        conversationStateService.saveDescription(phone, text);
                        conversationStateService.saveState(phone, "WAITING_LOCATION");

                        exchange.getMessage().setBody("""
                        {
                          "respuesta": "Gracias. ¿Dónde se encuentra el animal?"
                        }
                        """);

                    } else if ("WAITING_LOCATION".equals(currentState)) {

                        conversationStateService.saveLocation(phone, text);

                        String description =
                                conversationStateService.getDescription(phone);

                        String location =
                                conversationStateService.getLocation(phone);
                        RescueReport report = new RescueReport(
                                phone,
                                description,
                                location
                        );

                        exchange.getMessage().setBody(report);

                        exchange.getContext()
                                .createProducerTemplate()
                                .send("direct:create-case", exchange);

                        conversationStateService.clearConversation(phone);

                        exchange.getMessage().setBody(""" 
                        {
                        "respuesta": "Gracias. Registramos el reporte y será derivado para seguimiento."
                        }
                        """);


                    } else {
                        conversationStateService.clearConversation(phone);

                        exchange.getMessage().setBody("""
                        {
                          "respuesta": "Reiniciamos la conversación. Escribí nuevamente tu reporte."
                        }
                        """);
                    }
                })

                .setHeader("Content-Type", constant("application/json"));
    }
}