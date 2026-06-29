/*
 * Copyright (C) 2026 B3Partners B.V.
 *
 * SPDX-License-Identifier: MIT
 */
package nl.b3partners.entityresolverissues;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.Query;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.util.PreventLocalEntityResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WFSGetFeatureUtil {
    private static final Logger logger =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final String capabilitiesUrl;

    public WFSGetFeatureUtil(String capabilitiesUrl) {
        this.capabilitiesUrl = capabilitiesUrl;
    }

    /**
     * Get 1 feature from a WFS service using the provided filter.
     *
     * @param typeName the feature type name
     * @param cqlFilter an optional filter, may be @{code null}
     * @return a map of the attributes, keyed with attribute name, geometry value is omitted
     * @throws CQLException when filter parsing fails
     * @throws IOException when connection to the WFS fails
     */
    public Map<String, String> getOneFeature(String typeName, String cqlFilter) throws CQLException, IOException {
        Query q = new Query();
        q.setTypeName(typeName);
        q.setMaxFeatures(1);
        if (cqlFilter != null && !cqlFilter.isEmpty()) {
            Filter filter = ECQL.toFilter(cqlFilter);
            q.setFilter(filter);
        }

        DataStore ds = null;
        try {
            ds = DataStoreFinder.getDataStore(Map.of(
                    WFSDataStoreFactory.URL.key,
                    this.capabilitiesUrl,
                    WFSDataStoreFactory.ENTITY_RESOLVER.key,
                    PreventLocalEntityResolver.INSTANCE,
                    WFSDataStoreFactory.TRY_GZIP.key,
                    true,
                    WFSDataStoreFactory.TIMEOUT.key,
                    30000));

            logger.info("DataStore created for WFS capabilities URL: {}", this.capabilitiesUrl);
            logger.info("Found the following feature types: {}", (Object) ds.getTypeNames());

            GeometryDescriptor geomAttr = ds.getSchema(typeName).getGeometryDescriptor();
            logger.info("Geometry attribute for type {}: {}", typeName, geomAttr);

            try (SimpleFeatureIterator it =
                    ds.getFeatureSource(typeName).getFeatures(q).features()) {
                if (it.hasNext()) {
                    SimpleFeature feature = it.next();
                    Map<String, String> attributes = new HashMap<>();
                    for (Property property : feature.getProperties()) {
                        if (property.getDescriptor() == geomAttr) {
                            // Skip/replace geometry attribute value, as it might be too verbose
                            attributes.put(property.getName().toString(), "GEOMETRY (/* omitted */)");
                        } else {
                            attributes.put(
                                    property.getName().toString(),
                                    property.getValue() != null
                                            ? property.getValue().toString()
                                            : null);
                        }
                    }
                    return attributes;
                }
                logger.info("No feature found for type {} and filter {}", typeName, cqlFilter);
                return null;
            }
        } finally {
            if (ds != null) {
                ds.dispose();
            }
        }
    }
}
