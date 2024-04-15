package dev.abhisek.oauth2_marathon.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "grant_types")
@Getter
@Setter
@ToString
public class GrantType {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    @Column(name = "grant_type")
    private String grantType;

    @JsonIgnore
    @ManyToOne
    private Client client;
}
