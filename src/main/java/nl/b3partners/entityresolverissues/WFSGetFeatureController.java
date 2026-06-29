/*
 * Copyright (C) 2026 B3Partners B.V.
 *
 * SPDX-License-Identifier: MIT
 */
package nl.b3partners.entityresolverissues;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.util.Map;
import org.geotools.filter.text.cql2.CQLException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A REST controller that gets a single feature from a WFS service using a CQL filter. The feature attributes are
 * returned as a JSON object, with the geometry attribute value omitted. The parameters for the WFS requests are
 * hardcoded.
 */
@RestController
@RequestMapping(path = "/wfs", produces = MediaType.APPLICATION_JSON_VALUE)
public class WFSGetFeatureController {

    private static final String PDOK_WFS_URL =
            "https://service.pdok.nl/kadaster/brk-bestuurlijke-gebieden/wfs/v1_0?SERVICE=WFS&VERSION=2.0.0&REQUEST=GetCapabilities";
    private static final String PDOK_WFS_TYPE_NAME = "bestuurlijkegebieden:Provinciegebied";
    private static final String PDOK_WFS_FILTER = "naam='Gelderland'";
    private static final String TM_WFS_URL =
            "https://snapshot.tailormap.nl/geoserver/ows?service=WFS&acceptversions=2.0.0&request=GetCapabilities";
    private static final String TM_WFS_TYPE_NAME = "postgis:bak";

    /**
     * get a "provincie" feature.
     *
     * @return the feature attributes as a JSON encoded map, with the geometry attribute value omitted
     * @throws IOException when WFS request fails
     * @throws CQLException when filter parsing fails
     */
    @RequestMapping(
            method = {GET, POST},
            path = "/provincie")
    public ResponseEntity<?> getProvincieFeature() throws IOException, CQLException {
        Map<String, String> feature =
                new WFSGetFeatureUtil(PDOK_WFS_URL).getOneFeature(PDOK_WFS_TYPE_NAME, PDOK_WFS_FILTER);
        if (feature != null && !feature.isEmpty()) {
            return ResponseEntity.ok(feature);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get a "bak" feature.
     *
     * @return the feature attributes as a JSON encoded map, with the geometry attribute value omitted
     * @throws IOException when WFS request fails
     * @throws CQLException when filter parsing fails
     */
    @RequestMapping(
            method = {GET, POST},
            path = "/bak")
    public ResponseEntity<?> getBakFeature() throws IOException, CQLException {
        Map<String, String> feature = new WFSGetFeatureUtil(TM_WFS_URL).getOneFeature(TM_WFS_TYPE_NAME, null);
        if (feature != null && !feature.isEmpty()) {
            return ResponseEntity.ok(feature);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
