package nl.b3partners.entityresolverissues;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(
        classes = {
            GeoToolsConfiguration.class,
        })
@ComponentScan(basePackages = {"nl.b3partners.entityresolverissues"})
@EnableWebMvc
@AutoConfigureMockMvc
class WFSGetFeatureControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getProvincieFeature() throws Exception {
        final String url = "/wfs/provincie";
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.naam").value("Gelderland"))
                .andExpect(jsonPath("$.code").value("25"))
                .andExpect(jsonPath("$.geom").doesNotExist());
    }

    @Test
    void getBakFeature() throws Exception {
        final String url = "/wfs/bak";
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.creationdate").isNotEmpty())
                .andExpect(jsonPath("$.terminationdate").isEmpty())
                .andExpect(jsonPath("$.geom").value("POINT (130887.591 459392.138)"));
    }
}
