package org.example;

import io.micronaut.context.annotation.Property;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class EntsoeConfigTest {
    @Inject
    private EntsoeConfig entsoeConfig;

    @Property(name = "API_URL")
    private String API_URL;

    @Test
    public void testPropertyLoading() {
        assertNotNull(API_URL);
    }

    @Test
    public void testEntsoeConfigValues() {
        assertNotNull(entsoeConfig);
        assertNotNull(entsoeConfig.getApiUrl());
    }
}
