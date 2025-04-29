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

    @Property(name = "entsoe.apiUrlTemplate")
    private String apiUrlTemplate;

    @Test
    public void testPropertyLoading() {
        assertNotNull(apiUrlTemplate);
    }

    @Test
    public void testEntsoeConfigValues() {
        assertNotNull(entsoeConfig);
        assertNotNull(entsoeConfig.getApiUrlTemplate());
    }
}
