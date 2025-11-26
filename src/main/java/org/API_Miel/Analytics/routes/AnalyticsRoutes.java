package org.API_Miel.Analytics.routes;

import io.javalin.Javalin;
import org.API_Miel.Analytics.controllers.AnalyticsController;

public class AnalyticsRoutes {
    private final AnalyticsController controller;

    public AnalyticsRoutes() {
        this.controller = new AnalyticsController();
    }

    public void register(Javalin app) {
        // Endpoint: GET /api/analytics/ventas
        app.get("/api/analytics/ventas", controller::getSalesAnalytics);
    }
}