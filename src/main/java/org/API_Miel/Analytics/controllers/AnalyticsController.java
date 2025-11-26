package org.API_Miel.Analytics.controllers;

import io.javalin.http.Context;
import org.API_Miel.Analytics.services.AnalyticsService;
import java.util.Map;

public class AnalyticsController {
    private final AnalyticsService service;

    public AnalyticsController() {
        this.service = new AnalyticsService();
    }

    public void getSalesAnalytics(Context ctx) {
        try {
            var data = service.getGeneralAnalytics();
            ctx.json(Map.of("success", true, "data", data));
        } catch (Exception e) {
            ctx.status(500).json(Map.of("success", false, "message", e.getMessage()));
        }
    }
}