package it.infn.mw.iam.test.api.tokens;

import static it.infn.mw.iam.api.tokens.TokensControllerSupport.APPLICATION_JSON_CONTENT_TYPE;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.mitre.oauth2.model.ClientDetailsEntity;
import org.mitre.oauth2.model.OAuth2AccessTokenEntity;
import org.mitre.oauth2.model.OAuth2RefreshTokenEntity;
import org.mitre.oauth2.service.ClientDetailsEntityService;
import org.mitre.oauth2.service.impl.DefaultOAuth2ProviderTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.infn.mw.iam.api.tokens.Constants;
import it.infn.mw.iam.api.tokens.model.AccessToken;
import it.infn.mw.iam.api.tokens.model.RefreshToken;
import it.infn.mw.iam.api.tokens.model.TokensListResponse;
import it.infn.mw.iam.core.user.exception.IamAccountException;
import it.infn.mw.iam.persistence.model.IamAccount;
import it.infn.mw.iam.persistence.repository.IamAccountRepository;
import it.infn.mw.iam.persistence.repository.IamOAuthAccessTokenRepository;
import it.infn.mw.iam.persistence.repository.IamOAuthRefreshTokenRepository;
import it.infn.mw.iam.test.util.oauth.MockOAuth2Request;

public class TestTokensUtils {

  protected static final String REFRESH_TOKENS_BASE_PATH = Constants.REFRESH_TOKENS_ENDPOINT;
  protected static final String ACCESS_TOKENS_BASE_PATH = Constants.ACCESS_TOKENS_ENDPOINT;

  @Autowired
  protected IamOAuthAccessTokenRepository accessTokenRepository;

  @Autowired
  protected IamOAuthRefreshTokenRepository refreshTokenRepository;

  @Autowired
  private ClientDetailsEntityService clientDetailsService;

  @Autowired
  protected IamAccountRepository accountRepository;

  @Autowired
  protected DefaultOAuth2ProviderTokenService tokenService;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private ObjectMapper mapper;

  protected MockMvc mvc;

  public void initMvc() {
    initMvc(context);
  }

  public void initMvc(WebApplicationContext context) {
    mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).alwaysDo(print())
        .build();
  }

  private OAuth2Authentication oauth2Authentication(ClientDetailsEntity client, String username,
      String[] scopes) {

    Authentication userAuth = null;

    if (username != null) {
      userAuth = new UsernamePasswordAuthenticationToken(username, "");
    }

    MockOAuth2Request req = new MockOAuth2Request(client.getClientId(), scopes);
    OAuth2Authentication auth = new OAuth2Authentication(req, userAuth);

    return auth;
  }

  public ClientDetailsEntity loadTestClient(String clientId) {
    return clientDetailsService.loadClientByClientId(clientId);
  }

  public IamAccount loadTestUser(String userId) {
    return accountRepository.findByUsername(userId)
        .orElseThrow(() -> new IamAccountException("User not found"));
  }

  public OAuth2AccessTokenEntity buildAccessToken(ClientDetailsEntity client, String username,
      String[] scopes) {
    OAuth2AccessTokenEntity token =
        tokenService.createAccessToken(oauth2Authentication(client, username, scopes));
    return token;
  }

  public OAuth2AccessTokenEntity buildExpiredAccessToken(ClientDetailsEntity client,
      String username, String[] scopes) {

    OAuth2AccessTokenEntity token =
        tokenService.createAccessToken(oauth2Authentication(client, username, scopes));
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.DATE, -10);
    token.setExpiration(cal.getTime());
    accessTokenRepository.save(token);
    return token;
  }

  public OAuth2AccessTokenEntity buildAccessTokenWithExpiredRefreshToken(ClientDetailsEntity client,
      String username, String[] scopes) {

    OAuth2AccessTokenEntity token =
        tokenService.createAccessToken(oauth2Authentication(client, username, scopes));
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.DATE, -10);
    OAuth2RefreshTokenEntity refreshToken = token.getRefreshToken();
    refreshToken.setExpiration(cal.getTime());
    refreshTokenRepository.save(refreshToken);
    return token;
  }

  public OAuth2AccessTokenEntity buildAccessToken(ClientDetailsEntity client, String[] scopes) {
    OAuth2AccessTokenEntity token =
        tokenService.createAccessToken(oauth2Authentication(client, null, scopes));
    return token;
  }

  public void clearAllTokens() {
    accessTokenRepository.deleteAll();
    refreshTokenRepository.deleteAll();
  }

  public Authentication anonymousAuthenticationToken() {
    return new AnonymousAuthenticationToken("key", "anonymous",
        AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
  }

  protected LinkedMultiValueMap<String, String> getParams(List<Pair<String, String>> params) {

    LinkedMultiValueMap<String, String> out = new LinkedMultiValueMap<String, String>();
    params.forEach(param -> out.add(param.getKey(), param.getValue()));
    return out;
  }

  protected TokensListResponse<AccessToken> getAccessTokenList() throws JsonParseException,
      JsonMappingException, UnsupportedEncodingException, IOException, Exception {

    return getAccessTokenList(new LinkedMultiValueMap<String, String>());
  }

  protected TokensListResponse<AccessToken> getAccessTokenList(MultiValueMap<String, String> params)
      throws JsonParseException, JsonMappingException, UnsupportedEncodingException, IOException,
      Exception {

    /* @formatter:off */
    return mapper.readValue(
        mvc.perform(get(ACCESS_TOKENS_BASE_PATH)
            .contentType(APPLICATION_JSON_CONTENT_TYPE)
            .params(params))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(), new TypeReference<TokensListResponse<AccessToken>>() {});
    /* @formatter:on */
  }

  protected TokensListResponse<RefreshToken> getRefreshTokenList() throws JsonParseException,
      JsonMappingException, UnsupportedEncodingException, IOException, Exception {

    return getRefreshTokenList(new LinkedMultiValueMap<String, String>());
  }

  protected TokensListResponse<RefreshToken> getRefreshTokenList(MultiValueMap<String, String> params)
      throws JsonParseException, JsonMappingException, UnsupportedEncodingException, IOException,
      Exception {

    /* @formatter:off */
    return mapper.readValue(
        mvc.perform(get(REFRESH_TOKENS_BASE_PATH)
            .contentType(APPLICATION_JSON_CONTENT_TYPE)
            .params(params))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(), new TypeReference<TokensListResponse<RefreshToken>>() {});
    /* @formatter:on */
  }
}
