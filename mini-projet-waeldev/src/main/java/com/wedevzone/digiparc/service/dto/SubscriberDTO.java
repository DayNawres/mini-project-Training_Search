package com.wedevzone.digiparc.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wedevzone.digiparc.domain.Subscriber} entity.
 */
public class SubscriberDTO implements Serializable {

    private Long id;

    @NotNull
    private String cIN;

    @NotNull
    private String nom;

    @NotNull
    private String prenom;

    private Integer age;

    @NotNull
    private String statut;

    private InscriptionDTO inscription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getcIN() {
        return cIN;
    }

    public void setcIN(String cIN) {
        this.cIN = cIN;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public InscriptionDTO getInscription() {
        return inscription;
    }

    public void setInscription(InscriptionDTO inscription) {
        this.inscription = inscription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriberDTO)) {
            return false;
        }

        SubscriberDTO subscriberDTO = (SubscriberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subscriberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriberDTO{" +
            "id=" + getId() +
            ", cIN='" + getcIN() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", age=" + getAge() +
            ", statut='" + getStatut() + "'" +
            ", inscription=" + getInscription() +
            "}";
    }
}
