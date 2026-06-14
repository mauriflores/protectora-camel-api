package com.uminas.protectora.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ConversationRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("direct:saludo")
                .routeId("saludo-conversation-route")
                .setHeader("Content-Type", constant("application/json"))
                .setBody(constant("""
                {
                  "respuesta": "Hola ciudadano, ¿en qué puedo ayudarte?"
                }
                """));

        from("direct:rescate")
                .routeId("rescate-conversation-route")
                .setHeader("Content-Type", constant("application/json"))
                .setBody(constant("""
                {
                  "respuesta": "Entendido. Vamos a registrar un reporte de rescate."
                }
                """));

        from("direct:no-entendido")
                .routeId("no-entendido-conversation-route")
                .setHeader("Content-Type", constant("application/json"))
                .setBody(constant("""
                {
                  "respuesta": "No entendí tu solicitud."
                }
                """));
    }
}