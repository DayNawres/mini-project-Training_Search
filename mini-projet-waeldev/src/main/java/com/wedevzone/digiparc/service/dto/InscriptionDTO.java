package com.wedevzone.digiparc.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wedevzone.digiparc.domain.Inscription} entity.
 */
public class InscriptionDTO implements Serializable {

    private Long id;

    @NotNull
    private String objet;

    @NotNull
    private LocalDate dateValiditeDebut;

    @NotNull
    private LocalDate dateValiditeFin;

    private FormationDTO formation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public LocalDate getDateValiditeDebut() {
        return dateValiditeDebut;
    }

    public void setDateValiditeDebut(LocalDate dateValiditeDebut) {
        this.dateValiditeDebut = dateValiditeDebut;
    }

    public LocalDate getDateValiditeFin() {
        return dateValiditeFin;
    }

    public void setDateValiditeFin(LocalDate dateValiditeFin) {
        this.dateValiditeFin = dateValiditeFin;
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
        if (!(o instanceof InscriptionDTO)) {
            return false;
        }

        InscriptionDTO inscriptionDTO = (InscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscriptionDTO{" +
            "id=" + getId() +
            ", objet='" + getObjet() + "'" +
            ", dateValiditeDebut='" + getDateValiditeDebut() + "'" +
            ", dateValiditeFin='" + getDateValiditeFin() + "'" +
            ", formation=" + getFormation() +
            "}";
    }
}
