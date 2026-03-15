package com.example.api.core;

import io.qameta.allure.Allure;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.parser.OpenAPIV3Parser;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static io.qameta.allure.Allure.addAttachment;

public class CoverageTracker {

    private static final Set<String> coveredEndpoints = ConcurrentHashMap.newKeySet();

    private static final Map<String, Set<String>> apiSpecEndpoints = new HashMap<>();

    public static void loadOpenAPISpec(String specPath) {
        OpenAPI openAPI = new OpenAPIV3Parser().read(specPath);
        if (openAPI == null || openAPI.getPaths() == null) {
            throw new RuntimeException("Failed to parse OpenAPI spec: " + specPath);
        }
        for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
            String path = entry.getKey();
            PathItem item = entry.getValue();
            Set<String> methods = new HashSet<>();
            if (item.getGet() != null) methods.add("GET");
            if (item.getPost() != null) methods.add("POST");
            if (item.getPut() != null) methods.add("PUT");
            if (item.getDelete() != null) methods.add("DELETE");
            if (item.getPatch() != null) methods.add("PATCH");
            apiSpecEndpoints.put(path, methods);
        }
    }


    public static void log(String httpMethod, String path, Map<String, Object> queryParams, Object body) {
        String normalizedPath = normalizePath(path);
        String key = buildKey(httpMethod, normalizedPath, queryParams, body);
        coveredEndpoints.add(key);


        addAttachment("Covered Endpoint",
                String.format("%s %s\nQueryParams: %s\nBody: %s",
                        httpMethod, normalizedPath,
                        queryParams != null ? queryParams.toString() : "{}",
                        body != null ? body.toString() : "{}"));
    }

    private static String buildKey(String method, String path, Map<String, Object> queryParams, Object body) {
        String qp = queryParams != null ? queryParams.toString() : "{}";
        String bd = body != null ? body.toString() : "{}";
        return method.toUpperCase() + " " + path + " ? " + qp + " # " + bd;
    }


    public static String normalizePath(String path) {
        return path.replaceAll("/\\d+", "/{id}");
    }

    public static void attachAllureSummary() {
        int total = 0;
        int covered = 0;
        List<String> uncovered = new ArrayList<>();

        for (Map.Entry<String, Set<String>> entry : apiSpecEndpoints.entrySet()) {
            String path = entry.getKey();
            for (String method : entry.getValue()) {
                total++;
                boolean isCovered = coveredEndpoints.stream().anyMatch(k -> k.startsWith(method + " " + path));
                if (isCovered) {
                    covered++;
                } else {
                    uncovered.add(method + " " + path);
                }
            }
        }

        double coveragePercent = total == 0 ? 0 : (covered * 100.0 / total);

        StringBuilder sb = new StringBuilder();
        sb.append("Total endpoints: ").append(total).append("\n");
        sb.append("Covered endpoints: ").append(covered).append("\n");
        sb.append("Coverage: ").append(String.format("%.2f", coveragePercent)).append("%\n\n");
        sb.append("✅ Covered Endpoints:\n");
        coveredEndpoints.forEach(k -> sb.append(k).append("\n"));
        sb.append("\n❌ Uncovered Endpoints:\n");
        uncovered.forEach(k -> sb.append(k).append("\n"));

        addAttachment("API Coverage Summary", sb.toString());
    }

    public static Map<String, Object> getCoverageJson() {
        List<String> uncoveredEndpoints = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : apiSpecEndpoints.entrySet()) {
            String path = entry.getKey();
            for (String method : entry.getValue()) {
                boolean isCovered = coveredEndpoints.stream().anyMatch(k -> k.startsWith(method + " " + path));
                if (!isCovered) uncoveredEndpoints.add(method + " " + path);
            }
        }
        Map<String, Object> report = new HashMap<>();
        report.put("coveredEndpoints", coveredEndpoints);
        report.put("uncoveredEndpoints", uncoveredEndpoints);
        report.put("coveragePercent", coveredEndpoints.size() * 100.0 / (coveredEndpoints.size() + uncoveredEndpoints.size()));
        return report;
    }

    public static void reset() {
        coveredEndpoints.clear();
    }
}