package com.wedevzone.digiparc.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wedevzone.digiparc.domain.Emplacement} entity.
 */
public class EmplacementDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer identifiant;

    @NotNull
    private String type;

    private String centreDeFormation;

    private String webSiteLink;

    private String adresse;

    private FormationDTO formation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(Integer identifiant) {
        this.identifiant = identifiant;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCentreDeFormation() {
        return centreDeFormation;
    }

    public void setCentreDeFormation(String centreDeFormation) {
        this.centreDeFormation = centreDeFormation;
    }

    public String getWebSiteLink() {
        return webSiteLink;
    }

    public void setWebSiteLink(String webSiteLink) {
        this.webSiteLink = webSiteLink;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public FormationDTO getFormation() {
        return formation;
    }

    public void setFormation(FormationDTO formation) {
        this.formation = formation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmplacementDTO)) {
            return false;
        }

        EmplacementDTO emplacementDTO = (EmplacementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, emplacementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmplacementDTO{" +
            "id=" + getId() +
            ", identifiant=" + getIdentifiant() +
            ", type='" + getType() + "'" +
            ", centreDeFormation='" + getCentreDeFormation() + "'" +
            ", webSiteLink='" + getWebSiteLink() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", formation=" + getFormation() +
            "}";
    }
}
