package it.infn.mw.iam.test.api.config;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import it.infn.mw.iam.IamLoginService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {IamLoginService.class})
@WebAppConfiguration
@Transactional
//@formatter:off
@TestPropertySource(properties = {
    "iam.privacy-policy.url=https://policy.example",
    "iam.privacy-policy.text=Privacy Statement"
})
//@formatter:on
public class PrivacyPolicyConfigurationTests {

  private static final String ENDPOINT = "/iam/config/privacy-policy";

  @Autowired
  private WebApplicationContext context;

  private MockMvc mvc;

  @Before
  public void setup() {
    mvc =
        MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).alwaysDo(log()).build();
  }

  @Test
  public void testPrivacyPolicy() throws Exception {

    mvc.perform(get((ENDPOINT)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.url").value("https://policy.example"))
      .andExpect(jsonPath("$.text").value("Privacy Statement"));

  }

}
