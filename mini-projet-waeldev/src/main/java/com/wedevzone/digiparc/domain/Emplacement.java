package com.wedevzone.digiparc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Emplacement.
 */
@Entity
@Table(name = "emplacement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Emplacement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "identifiant", nullable = false)
    private Integer identifiant;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "centre_de_formation")
    private String centreDeFormation;

    @Column(name = "web_site_link")
    private String webSiteLink;

    @Column(name = "adresse")
    private String adresse;

    @ManyToOne
    @JsonIgnoreProperties(value = { "emplacaments", "inscriptions", "subscribers" }, allowSetters = true)
    private Formation formation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Emplacement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdentifiant() {
        return this.identifiant;
    }

    public Emplacement identifiant(Integer identifiant) {
        this.setIdentifiant(identifiant);
        return this;
    }

    public void setIdentifiant(Integer identifiant) {
        this.identifiant = identifiant;
    }

    public String getType() {
        return this.type;
    }

    public Emplacement type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCentreDeFormation() {
        return this.centreDeFormation;
    }

    public Emplacement centreDeFormation(String centreDeFormation) {
        this.setCentreDeFormation(centreDeFormation);
        return this;
    }

    public void setCentreDeFormation(String centreDeFormation) {
        this.centreDeFormation = centreDeFormation;
    }

    public String getWebSiteLink() {
        return this.webSiteLink;
    }

    public Emplacement webSiteLink(String webSiteLink) {
        this.setWebSiteLink(webSiteLink);
        return this;
    }

    public void setWebSiteLink(String webSiteLink) {
        this.webSiteLink = webSiteLink;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Emplacement adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Formation getFormation() {
        return this.formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public Emplacement formation(Formation formation) {
        this.setFormation(formation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Emplacement)) {
            return false;
        }
        return id != null && id.equals(((Emplacement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Emplacement{" +
            "id=" + getId() +
            ", identifiant=" + getIdentifiant() +
            ", type='" + getType() + "'" +
            ", centreDeFormation='" + getCentreDeFormation() + "'" +
            ", webSiteLink='" + getWebSiteLink() + "'" +
            ", adresse='" + getAdresse() + "'" +
            "}";
    }
}
