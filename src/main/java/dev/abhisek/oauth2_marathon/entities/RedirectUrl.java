package dev.abhisek.oauth2_marathon.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "redirect_urls")
@Getter
@Setter
@ToString
public class RedirectUrl {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String url;
    @JsonIgnore
    @ManyToOne
    private Client client;

}
