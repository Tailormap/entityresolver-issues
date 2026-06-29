/*
 * Copyright (C) 2026 B3Partners B.V.
 *
 * SPDX-License-Identifier: MIT
 */
package nl.b3partners.entityresolverissues;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Map;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WFSGetFeatureUtilIntegrationTest {
    private static final Logger logger =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String PDOK_WFS_URL =
            "https://service.pdok.nl/kadaster/brk-bestuurlijke-gebieden/wfs/v1_0?SERVICE=WFS&VERSION=2.0.0&REQUEST=GetCapabilities";
    private static final String PDOK_WFS_TYPE_NAME = "bestuurlijkegebieden:Provinciegebied";
    private static final String PDOK_WFS_FILTER = "naam='Gelderland'";

    private static final String TM_WFS_URL =
            "https://snapshot.tailormap.nl/geoserver/ows?service=WFS&acceptversions=2.0.0&request=GetCapabilities";
    private static final String TM_WFS_TYPE_NAME = "postgis:bak";

    @Test
    public void testWFSGetFeatureProvinciegebied() throws CQLException, IOException {
        Map<String, String> feature =
                new WFSGetFeatureUtil(PDOK_WFS_URL).getOneFeature(PDOK_WFS_TYPE_NAME, PDOK_WFS_FILTER);
        assertNotNull(feature);
        logger.info(feature.toString());
        assertThat(feature.get("naam"), equalTo("Gelderland"));
    }

    @Test
    public void testWFSGetFeatureBak() throws CQLException, IOException {
        Map<String, String> feature = new WFSGetFeatureUtil(TM_WFS_URL).getOneFeature(TM_WFS_TYPE_NAME, null);
        assertNotNull(feature);
        logger.info(feature.toString());
    }
}
