package com.wedevzone.digiparc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Subscriber.
 */
@Entity
@Table(name = "subscriber")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Subscriber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "c_in", nullable = false)
    private String cIN;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "age")
    private Integer age;

    @NotNull
    @Column(name = "statut", nullable = false)
    private String statut;

    @ManyToOne
    @JsonIgnoreProperties(value = { "subscribers", "formation" }, allowSetters = true)
    private Inscription inscription;

    @ManyToMany(mappedBy = "subscribers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "emplacaments", "inscriptions", "subscribers" }, allowSetters = true)
    private Set<Formation> formations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Subscriber id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getcIN() {
        return this.cIN;
    }

    public Subscriber cIN(String cIN) {
        this.setcIN(cIN);
        return this;
    }

    public void setcIN(String cIN) {
        this.cIN = cIN;
    }

    public String getNom() {
        return this.nom;
    }

    public Subscriber nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Subscriber prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Integer getAge() {
        return this.age;
    }

    public Subscriber age(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getStatut() {
        return this.statut;
    }

    public Subscriber statut(String statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Inscription getInscription() {
        return this.inscription;
    }

    public void setInscription(Inscription inscription) {
        this.inscription = inscription;
    }

    public Subscriber inscription(Inscription inscription) {
        this.setInscription(inscription);
        return this;
    }

    public Set<Formation> getFormations() {
        return this.formations;
    }

    public void setFormations(Set<Formation> formations) {
        if (this.formations != null) {
            this.formations.forEach(i -> i.removeSubscriber(this));
        }
        if (formations != null) {
            formations.forEach(i -> i.addSubscriber(this));
        }
        this.formations = formations;
    }

    public Subscriber formations(Set<Formation> formations) {
        this.setFormations(formations);
        return this;
    }

    public Subscriber addFormation(Formation formation) {
        this.formations.add(formation);
        formation.getSubscribers().add(this);
        return this;
    }

    public Subscriber removeFormation(Formation formation) {
        this.formations.remove(formation);
        formation.getSubscribers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subscriber)) {
            return false;
        }
        return id != null && id.equals(((Subscriber) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Subscriber{" +
            "id=" + getId() +
            ", cIN='" + getcIN() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", age=" + getAge() +
            ", statut='" + getStatut() + "'" +
            "}";
    }
}
