package dev.abhisek.oauth2_marathon.dto;

import java.util.List;

public record ClientDto(
        String clientId,
        String secret,
        List<String> authenticationMethods,
        List<String> grantTypes,
        List<String> redirectUrls,
        List<String> scopes,
        int accessTokenTtl,
        String type
) {
}
