package dev.abhisek.oauth2_marathon.repository;

import dev.abhisek.oauth2_marathon.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    @Query("""
                SELECT c FROM Client c WHERE c.clientId = :clientId
            """)
    Optional<Client> findByClientId(String clientId);
}
