package com.example.api.core;

import io.qameta.allure.Allure;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.Getter;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.util.function.Consumer;

public class FluentResponse<T> {

    private final Response response;
    @Getter
    private T body;

    private FluentResponse(Response response) {
        this.response = response;
        attachResponse();
    }

    public static FluentResponse<?> of(Response response) {
        return new FluentResponse<>(response);
    }

    public FluentResponse<T> status(int expected) {

        int actual = response.getStatusCode();

        if (actual != expected) {
            throw new AssertionError(
                    "Expected status " + expected + " but got " + actual
            );
        }

        return this;
    }

    public FluentResponse<T> schema(String schema) {

        response.then().body(matchesJsonSchemaInClasspath(schema));
        return this;
    }

    public FluentResponse<T> body(Class<T> clazz) {

        this.body = response.as(clazz);
        return this;
    }

    public FluentResponse<T> body(TypeRef<T> typeRef) {

        this.body = response.as(typeRef);
        return this;
    }

    public FluentResponse<T> assertThat(Consumer<T> assertions) {

        assertions.accept(body);
        return this;
    }

    private void attachResponse() {
        Allure.addAttachment("Response", response.prettyPrint());
    }
}