/*
 * Copyright (C) 2026 B3Partners B.V.
 *
 * SPDX-License-Identifier: MIT
 */
package nl.b3partners.entityresolverissues;

import jakarta.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import org.geotools.util.PreventLocalEntityResolver;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeoToolsConfiguration {
    private static final Logger logger =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @PostConstruct
    public void configureGeoTools() {
        logger.info("Initialise GeoTools");
        GeoTools.init();
        logger.info("Add system default entity resolver to hints");
        Hints.putSystemDefault(Hints.ENTITY_RESOLVER, PreventLocalEntityResolver.INSTANCE);
    }
}
