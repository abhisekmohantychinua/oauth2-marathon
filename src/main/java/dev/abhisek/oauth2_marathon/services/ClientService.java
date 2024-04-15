package dev.abhisek.oauth2_marathon.services;

import dev.abhisek.oauth2_marathon.entities.Client;
import dev.abhisek.oauth2_marathon.mapper.GlobalMapper;
import dev.abhisek.oauth2_marathon.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientService implements RegisteredClientRepository {
    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public void save(RegisteredClient registeredClient) {
        Client client = new Client();

        client.setClientId(registeredClient.getClientId());
        client.setSecret(registeredClient.getClientSecret());
        client.setAuthenticationMethods(
                registeredClient.getClientAuthenticationMethods()
                        .stream().map(a -> GlobalMapper.mapToAuthenticationMethod(a, client))
                        .collect(Collectors.toList())
        );
        client.setGrantTypes(
                registeredClient.getAuthorizationGrantTypes()
                        .stream().map(g -> GlobalMapper.mapToGrantType(g, client))
                        .collect(Collectors.toList())
        );
        client.setRedirectUrls(
                registeredClient.getRedirectUris()
                        .stream().map(r -> GlobalMapper.mapToRedirectUrl(r, client))
                        .collect(Collectors.toList())
        );
        client.setScopes(
                registeredClient.getScopes()
                        .stream().map(s -> GlobalMapper.mapToScopes(s, client))
                        .collect(Collectors.toList())
        );
        clientRepository.save(client);
    }

    @Override
    public RegisteredClient findById(String id) {
        Optional<Client> client = clientRepository.findById(Integer.parseInt(id));
        return client.map(GlobalMapper::mapToRegisteredClient)
                .orElseThrow(() -> new RuntimeException(":("));

    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Optional<Client> client = clientRepository.findByClientId(clientId);
        return client.map(GlobalMapper::mapToRegisteredClient)
                .orElseThrow(() -> new RuntimeException(":("));
    }
}
