package dev.abhisek.oauth2_marathon.controller;

import dev.abhisek.oauth2_marathon.dto.ClientDto;
import dev.abhisek.oauth2_marathon.entities.Client;
import dev.abhisek.oauth2_marathon.services.ClientCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dynamics")
@RequiredArgsConstructor
public class ClientController {
    private final ClientCrudService service;

    @GetMapping("template")
    private ResponseEntity<ClientDto> template() {
        return ResponseEntity.ok(service.template());
    }

    @GetMapping("pkce")
    private ResponseEntity<Map<String, String>> generatePKCE() throws NoSuchAlgorithmException {
        return ResponseEntity.ok(service.generatePKCE());
    }

    @PostMapping("client")
    private ResponseEntity<Client> saveClient(@RequestBody ClientDto dto) {
        return ResponseEntity.ok(service.saveClient(dto));
    }

    @GetMapping("client")
    private ResponseEntity<List<Client>> getAll() {
        return ResponseEntity.ok(service.getAllClient());
    }
}
