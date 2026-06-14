package com.uminas.protectora.routes;

import com.uminas.protectora.model.RescueReport;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CaseRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("direct:create-case")
                .routeId("create-case-route")
                .process(exchange -> {
                    RescueReport report =
                            exchange.getMessage().getBody(RescueReport.class);

                    System.out.println("=== NUEVO CASE SIMULADO ===");
                    System.out.println("Telefono: " + report.getPhone());
                    System.out.println("Descripcion: " + report.getDescription());
                    System.out.println("Ubicacion: " + report.getLocation());
                });
    }
}