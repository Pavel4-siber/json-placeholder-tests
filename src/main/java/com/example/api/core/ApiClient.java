package com.example.api.core;

import io.restassured.response.Response;

import java.util.Map;

import static com.example.api.core.CoverageTracker.log;
import static com.example.api.core.RequestSpecs.getRequestSpec;
import static io.restassured.RestAssured.given;

public class ApiClient {

    protected Response get(String path) {
        log("GET", path, null, null);
        return given()
                .spec(getRequestSpec())
                .when()
                .get(path);
    }

    protected Response post(String path, Object body) {
        log("POST", path, null, body);
        return given()
                .spec(getRequestSpec())
                .body(body)
                .when()
                .post(path);
    }

    protected Response put(String path, Object body) {
        log("PUT", path, null, body);
        return given()
                .spec(getRequestSpec())
                .body(body)
                .when()
                .put(path);
    }

    protected Response delete(String path) {
        log("DELETE", path, null, null);
        return given()
                .spec(getRequestSpec())
                .when()
                .delete(path);
    }

    protected Response patch(String path, Map<String, Object> patchBody) {
        log("PATCH", path, null, patchBody);
        return given()
                .spec(getRequestSpec())
                .body(patchBody)
                .when()
                .patch(path);
    }

}
