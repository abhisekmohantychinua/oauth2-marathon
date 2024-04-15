package dev.abhisek.oauth2_marathon.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "authentication_methods")
@Getter
@Setter
@ToString
public class AuthenticationMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "authentication_method")
    private String authenticationMethod;

    @JsonIgnore
    @ManyToOne
    private Client client;


}
