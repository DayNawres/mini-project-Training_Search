package com.wedevzone.digiparc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Inscription.
 */
@Entity
@Table(name = "inscription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Inscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "objet", nullable = false)
    private String objet;

    @NotNull
    @Column(name = "date_validite_debut", nullable = false)
    private LocalDate dateValiditeDebut;

    @NotNull
    @Column(name = "date_validite_fin", nullable = false)
    private LocalDate dateValiditeFin;

    @OneToMany(mappedBy = "inscription")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "inscription", "formations" }, allowSetters = true)
    private Set<Subscriber> subscribers = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "emplacaments", "inscriptions", "subscribers" }, allowSetters = true)
    private Formation formation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjet() {
        return this.objet;
    }

    public Inscription objet(String objet) {
        this.setObjet(objet);
        return this;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public LocalDate getDateValiditeDebut() {
        return this.dateValiditeDebut;
    }

    public Inscription dateValiditeDebut(LocalDate dateValiditeDebut) {
        this.setDateValiditeDebut(dateValiditeDebut);
        return this;
    }

    public void setDateValiditeDebut(LocalDate dateValiditeDebut) {
        this.dateValiditeDebut = dateValiditeDebut;
    }

    public LocalDate getDateValiditeFin() {
        return this.dateValiditeFin;
    }

    public Inscription dateValiditeFin(LocalDate dateValiditeFin) {
        this.setDateValiditeFin(dateValiditeFin);
        return this;
    }

    public void setDateValiditeFin(LocalDate dateValiditeFin) {
        this.dateValiditeFin = dateValiditeFin;
    }

    public Set<Subscriber> getSubscribers() {
        return this.subscribers;
    }

    public void setSubscribers(Set<Subscriber> subscribers) {
        if (this.subscribers != null) {
            this.subscribers.forEach(i -> i.setInscription(null));
        }
        if (subscribers != null) {
            subscribers.forEach(i -> i.setInscription(this));
        }
        this.subscribers = subscribers;
    }

    public Inscription subscribers(Set<Subscriber> subscribers) {
        this.setSubscribers(subscribers);
        return this;
    }

    public Inscription addSubscriber(Subscriber subscriber) {
        this.subscribers.add(subscriber);
        subscriber.setInscription(this);
        return this;
    }

    public Inscription removeSubscriber(Subscriber subscriber) {
        this.subscribers.remove(subscriber);
        subscriber.setInscription(null);
        return this;
    }

    public Formation getFormation() {
        return this.formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public Inscription formation(Formation formation) {
        this.setFormation(formation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inscription)) {
            return false;
        }
        return id != null && id.equals(((Inscription) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inscription{" +
            "id=" + getId() +
            ", objet='" + getObjet() + "'" +
            ", dateValiditeDebut='" + getDateValiditeDebut() + "'" +
            ", dateValiditeFin='" + getDateValiditeFin() + "'" +
            "}";
    }
}
