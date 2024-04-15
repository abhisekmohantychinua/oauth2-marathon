package dev.abhisek.oauth2_marathon.mapper;

import dev.abhisek.oauth2_marathon.dto.ClientDto;
import dev.abhisek.oauth2_marathon.entities.*;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class GlobalMapper {

    public static Client mapDtoToClient(ClientDto dto) {
        Client client = new Client();
        client.setClientId("client");
        client.setSecret("secret");

        client.setAuthenticationMethods(
                dto
                        .authenticationMethods()
                        .stream().map(a -> mapToAuthenticationMethod(a, client))
                        .collect(Collectors.toList())
        );

        client.setGrantTypes(
                dto
                        .grantTypes()
                        .stream().map(g -> mapToGrantType(g, client))
                        .collect(Collectors.toList())
        );
        client.setRedirectUrls(
                dto
                        .redirectUrls()
                        .stream().map(r -> mapToRedirectUrl(r, client))
                        .collect(Collectors.toList())
        );
        client.setScopes(
                dto
                        .scopes()
                        .stream().map(s -> mapToScopes(s, client))
                        .collect(Collectors.toList())
        );

        client.setClientTokenSettings(mapToTokenSettings(
                dto.accessTokenTtl(),
                dto.type(),
                client
        ));

        return client;
    }

    public static RegisteredClient mapToRegisteredClient(Client client) {
        return RegisteredClient
                .withId(String.valueOf(client.getId()))
                .clientId(client.getClientId())
                .clientSecret(client.getSecret())
                .clientAuthenticationMethods(
                        authenticationMethods(client.getAuthenticationMethods()))
                .authorizationGrantTypes(
                        authorizationGrantTypes(client.getGrantTypes()))
                .redirectUris(
                        redirectUris(client.getRedirectUrls()))
                .scopes(
                        scopes(client.getScopes()))
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(client.getClientTokenSettings().getAccessTokenTTL()))
                        .accessTokenFormat(new OAuth2TokenFormat(client.getClientTokenSettings().getType()))
                        .build())
                .build();
    }

    public static AuthenticationMethod mapToAuthenticationMethod(String authenticationMethod, Client client) {
        AuthenticationMethod auth = new AuthenticationMethod();
        auth.setAuthenticationMethod(authenticationMethod);
        auth.setClient(client);
        return auth;
    }

    public static AuthenticationMethod mapToAuthenticationMethod(ClientAuthenticationMethod clientAuthenticationMethod, Client client) {
        return mapToAuthenticationMethod(clientAuthenticationMethod.getValue(), client);
    }

    public static GrantType mapToGrantType(String grantType, Client client) {
        GrantType grant = new GrantType();
        grant.setGrantType(grantType);
        grant.setClient(client);
        return grant;
    }

    public static GrantType mapToGrantType(AuthorizationGrantType grantType, Client client) {
        return mapToGrantType(grantType.getValue(), client);
    }

    public static RedirectUrl mapToRedirectUrl(String url, Client client) {
        RedirectUrl redirectUrl = new RedirectUrl();
        redirectUrl.setUrl(url);
        redirectUrl.setClient(client);
        return redirectUrl;
    }

    public static Scope mapToScopes(String scope, Client client) {
        Scope sc = new Scope();
        sc.setScope(scope);
        sc.setClient(client);
        return sc;
    }

    public static ClientTokenSettings mapToTokenSettings(int timeToLive, String type, Client client) {
        ClientTokenSettings clientTokenSettings = new ClientTokenSettings();
        clientTokenSettings.setAccessTokenTTL(timeToLive);
        clientTokenSettings.setType(type);
        clientTokenSettings.setClient(client);
        return clientTokenSettings;
    }

    private static Consumer<Set<String>> scopes(List<Scope> scopes) {
        return c -> {
            for (Scope scope : scopes) {
                c.add(scope.getScope());
            }
        };
    }

    private static Consumer<Set<String>> redirectUris(List<RedirectUrl> redirectUrls) {
        return c -> {
            for (RedirectUrl redirectUrl : redirectUrls) {
                c.add(redirectUrl.getUrl());
            }
        };
    }

    private static Consumer<Set<AuthorizationGrantType>> authorizationGrantTypes(List<GrantType> grantTypes) {
        return c -> {
            for (GrantType grantType : grantTypes) {
                c.add(new AuthorizationGrantType(grantType.getGrantType()));
            }
//            c.add(AuthorizationGrantType.AUTHORIZATION_CODE);
//            c.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
//            c.add(AuthorizationGrantType.REFRESH_TOKEN);
        };
    }

    private static Consumer<Set<ClientAuthenticationMethod>> authenticationMethods(List<AuthenticationMethod> authenticationMethods) {
        return c -> {
            for (AuthenticationMethod authenticationMethod : authenticationMethods) {
                c.add(new ClientAuthenticationMethod(authenticationMethod.getAuthenticationMethod()));
            }
        };
    }
}
