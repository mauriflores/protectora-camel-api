package com.uminas.protectora.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SaludoRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("platform-http:/saludo?httpMethodRestrict=GET")
                .routeId("saludo-route")
                .setHeader("Content-Type", constant("application/json"))
                .setBody(constant("{\"mensaje\":\"Hola desde Apache Camel\"}"));
    }

    public static class WhatsAppMessage {

        private String phone;
        private String message;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}