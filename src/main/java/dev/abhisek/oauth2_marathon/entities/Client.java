package dev.abhisek.oauth2_marathon.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "clients")
@Getter
@Setter
@ToString
public class Client {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    @Column(name = "client_id")
    private String clientId;

    private String secret;

    @OneToMany(mappedBy = "client", fetch = EAGER, cascade = ALL)
    private List<AuthenticationMethod> authenticationMethods;

    @OneToMany(mappedBy = "client", fetch = EAGER, cascade = ALL)
    private List<GrantType> grantTypes;

    @OneToMany(mappedBy = "client", fetch = EAGER, cascade = ALL)
    private List<RedirectUrl> redirectUrls;

    @OneToMany(mappedBy = "client", fetch = EAGER, cascade = ALL)
    private List<Scope> scopes;

    @OneToOne(mappedBy = "client", fetch = EAGER, cascade = ALL)
    private ClientTokenSettings clientTokenSettings;
}
