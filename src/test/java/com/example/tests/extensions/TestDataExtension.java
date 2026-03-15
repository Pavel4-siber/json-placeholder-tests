package com.example.tests.extensions;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

public class TestDataExtension implements BeforeEachCallback {

    private static final Namespace NAMESPACE = Namespace.create(TestDataExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        context.getStore(NAMESPACE).put("basePath","/post");
    }
}
