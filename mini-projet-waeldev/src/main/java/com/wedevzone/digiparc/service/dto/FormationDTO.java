package com.wedevzone.digiparc.service.dto;

import com.wedevzone.digiparc.domain.enumeration.Type;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wedevzone.digiparc.domain.Formation} entity.
 */
public class FormationDTO implements Serializable {

    private Long id;

    @NotNull
    private String identifiant;

    @NotNull
    private String description;

    private String domaine;

    private Type type;

    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    @NotNull
    private String lesHeuresDeLaFormation;

    private String lePrix;

    private String nomInstructeur;

    private Set<SubscriberDTO> subscribers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomaine() {
        return domaine;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getLesHeuresDeLaFormation() {
        return lesHeuresDeLaFormation;
    }

    public void setLesHeuresDeLaFormation(String lesHeuresDeLaFormation) {
        this.lesHeuresDeLaFormation = lesHeuresDeLaFormation;
    }

    public String getLePrix() {
        return lePrix;
    }

    public void setLePrix(String lePrix) {
        this.lePrix = lePrix;
    }

    public String getNomInstructeur() {
        return nomInstructeur;
    }

    public void setNomInstructeur(String nomInstructeur) {
        this.nomInstructeur = nomInstructeur;
    }

    public Set<SubscriberDTO> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<SubscriberDTO> subscribers) {
        this.subscribers = subscribers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormationDTO)) {
            return false;
        }

        FormationDTO formationDTO = (FormationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormationDTO{" +
            "id=" + getId() +
            ", identifiant='" + getIdentifiant() + "'" +
            ", description='" + getDescription() + "'" +
            ", domaine='" + getDomaine() + "'" +
            ", type='" + getType() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", lesHeuresDeLaFormation='" + getLesHeuresDeLaFormation() + "'" +
            ", lePrix='" + getLePrix() + "'" +
            ", nomInstructeur='" + getNomInstructeur() + "'" +
            ", subscribers=" + getSubscribers() +
            "}";
    }
}
