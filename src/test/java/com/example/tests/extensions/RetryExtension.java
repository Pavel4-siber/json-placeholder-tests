package com.example.tests.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

public class RetryExtension implements TestExecutionExceptionHandler {

    private static final int MAX_RETRIES = 3;

    @Override
    public void handleTestExecutionException(ExtensionContext context,
                                             Throwable throwable) throws Throwable {
        {

            int retries =
                    context.getStore(ExtensionContext.Namespace.GLOBAL)
                            .getOrDefault("retries", Integer.class, 0);

            if (retries < MAX_RETRIES) {

                context.getStore(ExtensionContext.Namespace.GLOBAL)
                        .put("retries", retries + 1);

                throw throwable;
            }

            throw throwable;
        }
    }
}
