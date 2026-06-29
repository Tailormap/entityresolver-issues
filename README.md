# entityresolver-issues

Small Spring Boot 4 + GeoTools test application for reproducing and investigating WFS entity resolver behavior and
related integration issues.

The app exposes simple REST endpoints under `/wfs` that fetch one feature from external WFS services, apply an optional
CQL filter, and return feature attributes as JSON while omitting verbose geometry values.

## Endpoints

- `GET|POST /wfs/provincie` - fetches a single `bestuurlijkegebieden:Provinciegebied` feature from PDOK, filtered to
  `naam='Gelderland'`.
- `GET|POST /wfs/bak` - fetches a single `postgis:bak` feature from the Tailormap snapshot WFS.

## Tests

All tests should pass when running eg. `mvn test` or `mvn install`.

### POJO (Unit) tests

- `WFSGetFeatureUtilTest#testWFSGetFeatureProvinciegebied` - tests the `WFSGetFeatureUtil#getOneFeature` method with the
  PDOK WFS service.
- `WFSGetFeatureUtilTest#testWFSGetFeatureBak` - tests the `WFSGetFeatureUtil#getOneFeature` method with the Tailormap
  snapshot WFS service.

Run separately using `mvn test -Dtest=WFSGetFeatureUtilTest`.

### REST (Spring Boot) tests

- `GET /wfs/test/provincie` - is tested in `WFSGetFeatureControllerTest#getProvincieFeature`.
- `GET /wfs/test/bak` - is tested in `WFSGetFeatureControllerTest#getBakFeature`.

Run separately using `mvn test -Dtest=WFSGetFeatureControllerTest`.

## Run

```bash
./mvnw spring-boot:run
```