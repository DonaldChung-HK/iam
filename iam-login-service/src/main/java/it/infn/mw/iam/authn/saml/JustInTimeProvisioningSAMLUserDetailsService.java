package it.infn.mw.iam.authn.saml;

import static it.infn.mw.iam.authn.saml.util.Saml2Attribute.givenName;
import static it.infn.mw.iam.authn.saml.util.Saml2Attribute.mail;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

import it.infn.mw.iam.authn.InactiveAccountAuthenticationHander;
import it.infn.mw.iam.authn.saml.util.Saml2Attribute;
import it.infn.mw.iam.authn.saml.util.SamlUserIdentifierResolver;
import it.infn.mw.iam.core.user.IamAccountService;
import it.infn.mw.iam.persistence.model.IamAccount;
import it.infn.mw.iam.persistence.model.IamSamlId;
import it.infn.mw.iam.persistence.repository.IamAccountRepository;

public class JustInTimeProvisioningSAMLUserDetailsService extends SAMLUserDetailsServiceSupport
    implements SAMLUserDetailsService {

  private final IamAccountRepository repo;
  private final IamAccountService accountService;

  private static final EnumSet<Saml2Attribute> REQUIRED_SAML_ATTRIBUTES =
      EnumSet.of(Saml2Attribute.mail, Saml2Attribute.givenName, Saml2Attribute.sn);

  public JustInTimeProvisioningSAMLUserDetailsService(SamlUserIdentifierResolver resolver,
      IamAccountService accountService, InactiveAccountAuthenticationHander inactiveAccountHandler,
      IamAccountRepository repo) {
    super(inactiveAccountHandler, resolver);
    this.accountService = accountService;
    this.repo = repo;
  }

  protected void samlCredentialSanityChecks(SAMLCredential credential) {

    for (Saml2Attribute a : REQUIRED_SAML_ATTRIBUTES) {
      if (credential.getAttributeAsString(a.getAttributeName()) == null) {
        throw new UsernameNotFoundException(String.format(
            "Error provisioning user! SAML credential is missing required attribute: %s (%s)",
            a.getAlias(), a.getAttributeName()));
      }
    }
  }


  @Override
  public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {

    IamSamlId samlId = resolverSamlId(credential);

    Optional<IamAccount> account = repo.findBySamlId(samlId);

    if (account.isPresent()) {
      return buildUserFromIamAccount(account.get());
    }

    samlCredentialSanityChecks(credential);
    
    final String randomUuid = UUID.randomUUID().toString();

    // Create account from SAMLCredential
    IamAccount newAccount = new IamAccount();

    newAccount.getSamlIds().add(samlId);
    newAccount.setActive(true);

    if (samlId.getUserId().length() < 128) {
      newAccount.setUsername(samlId.getUserId());
    } else {
      newAccount.setUsername(randomUuid);
    }

    newAccount.getUserInfo()
      .setGivenName(credential.getAttributeAsString(givenName.getAttributeName()));
    
    newAccount.getUserInfo()
      .setFamilyName(credential.getAttributeAsString(Saml2Attribute.sn.getAttributeName()));
    
    newAccount.getUserInfo().setEmail(credential.getAttributeAsString(mail.getAttributeName()));
    
    newAccount = accountService.createAccount(newAccount);
    
    return buildUserFromIamAccount(newAccount);
  }

}
