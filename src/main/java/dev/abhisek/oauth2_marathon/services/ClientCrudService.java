package dev.abhisek.oauth2_marathon.services;

import dev.abhisek.oauth2_marathon.dto.ClientDto;
import dev.abhisek.oauth2_marathon.entities.Client;
import dev.abhisek.oauth2_marathon.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static dev.abhisek.oauth2_marathon.mapper.GlobalMapper.mapDtoToClient;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.*;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.*;
import static org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat.REFERENCE;
import static org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat.SELF_CONTAINED;

@Service
@RequiredArgsConstructor
public class ClientCrudService {
    private final ClientRepository repository;

    public ClientDto template() {
        String clientId = "client";
        String secret = "secret";

        List<String> authenticationMethods = Stream.of(
                        CLIENT_SECRET_BASIC,
                        CLIENT_SECRET_JWT,
                        CLIENT_SECRET_POST,
                        PRIVATE_KEY_JWT,
                        NONE
                ).map(ClientAuthenticationMethod::getValue)
                .toList();

        List<String> grantTypes = Stream.of(
                        AUTHORIZATION_CODE,
                        REFRESH_TOKEN,
                        CLIENT_CREDENTIALS,
                        JWT_BEARER,
                        DEVICE_CODE
                ).map(AuthorizationGrantType::getValue)
                .toList();

        List<String> redirectUrls = Stream.of("http://example.com/auth").toList();
        List<String> scopes = Stream.of("openid", "profile").toList();
        int accessTokenTtl = 5;
        String type = REFERENCE.getValue() + " / " + SELF_CONTAINED.getValue();
        return new ClientDto(
                clientId,
                secret,
                authenticationMethods,
                grantTypes,
                redirectUrls,
                scopes,
                accessTokenTtl,
                type
        );
    }

    public Map<String, String> generatePKCE() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] code = new byte[32];
        secureRandom.nextBytes(code);
        String codeVerifier = Base64.getUrlEncoder().withoutPadding().encodeToString(code);

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] digested = messageDigest.digest(codeVerifier.getBytes());
        String codeChallenge = Base64.getUrlEncoder().withoutPadding().encodeToString(digested);
        return Map.of(
                "code verifier", codeVerifier,
                "code challenger", codeChallenge,
                "request code", "http://localhost:8080/oauth2/authorize?" +
                        "response_type=code" +
                        "&client_id=client" +
                        "&scope=openid" +
                        "&redirect_uri=http://example.com/authorization" +
                        "&code_challenge=" + codeChallenge +
                        "&code_challenge_method=S256",
                "request access token", "http://localhost:8080/oauth2/token?" +
                        "client_id=client" +
                        "&redirect_uri=http://example.com/authorization" +
                        "&grant_type=authorization_code" +
                        "&code=TO_BE_FILLED" +
                        "&code_verifier=" + codeVerifier
        );
    }

    public Client saveClient(ClientDto dto) {
        return repository.save(mapDtoToClient(dto));
    }

    public List<Client> getAllClient() {
        return repository.findAll();
    }
}
