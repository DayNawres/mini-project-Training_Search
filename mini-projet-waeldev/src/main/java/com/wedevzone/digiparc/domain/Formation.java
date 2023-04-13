package com.wedevzone.digiparc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wedevzone.digiparc.domain.enumeration.Type;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Formation.
 */
@Entity
@Table(name = "formation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Formation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "identifiant", nullable = false)
    private String identifiant;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "domaine")
    private String domaine;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @NotNull
    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @NotNull
    @Column(name = "les_heures_de_la_formation", nullable = false)
    private String lesHeuresDeLaFormation;

    @Column(name = "le_prix")
    private String lePrix;

    @Column(name = "nom_instructeur")
    private String nomInstructeur;

    @OneToMany(mappedBy = "formation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "formation" }, allowSetters = true)
    private Set<Emplacement> emplacaments = new HashSet<>();

    @OneToMany(mappedBy = "formation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subscribers", "formation" }, allowSetters = true)
    private Set<Inscription> inscriptions = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_formation__subscriber",
        joinColumns = @JoinColumn(name = "formation_id"),
        inverseJoinColumns = @JoinColumn(name = "subscriber_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "inscription", "formations" }, allowSetters = true)
    private Set<Subscriber> subscribers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Formation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifiant() {
        return this.identifiant;
    }

    public Formation identifiant(String identifiant) {
        this.setIdentifiant(identifiant);
        return this;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getDescription() {
        return this.description;
    }

    public Formation description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomaine() {
        return this.domaine;
    }

    public Formation domaine(String domaine) {
        this.setDomaine(domaine);
        return this;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public Type getType() {
        return this.type;
    }

    public Formation type(Type type) {
        this.setType(type);
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public Formation dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public Formation dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getLesHeuresDeLaFormation() {
        return this.lesHeuresDeLaFormation;
    }

    public Formation lesHeuresDeLaFormation(String lesHeuresDeLaFormation) {
        this.setLesHeuresDeLaFormation(lesHeuresDeLaFormation);
        return this;
    }

    public void setLesHeuresDeLaFormation(String lesHeuresDeLaFormation) {
        this.lesHeuresDeLaFormation = lesHeuresDeLaFormation;
    }

    public String getLePrix() {
        return this.lePrix;
    }

    public Formation lePrix(String lePrix) {
        this.setLePrix(lePrix);
        return this;
    }

    public void setLePrix(String lePrix) {
        this.lePrix = lePrix;
    }

    public String getNomInstructeur() {
        return this.nomInstructeur;
    }

    public Formation nomInstructeur(String nomInstructeur) {
        this.setNomInstructeur(nomInstructeur);
        return this;
    }

    public void setNomInstructeur(String nomInstructeur) {
        this.nomInstructeur = nomInstructeur;
    }

    public Set<Emplacement> getEmplacaments() {
        return this.emplacaments;
    }

    public void setEmplacaments(Set<Emplacement> emplacements) {
        if (this.emplacaments != null) {
            this.emplacaments.forEach(i -> i.setFormation(null));
        }
        if (emplacements != null) {
            emplacements.forEach(i -> i.setFormation(this));
        }
        this.emplacaments = emplacements;
    }

    public Formation emplacaments(Set<Emplacement> emplacements) {
        this.setEmplacaments(emplacements);
        return this;
    }

    public Formation addEmplacament(Emplacement emplacement) {
        this.emplacaments.add(emplacement);
        emplacement.setFormation(this);
        return this;
    }

    public Formation removeEmplacament(Emplacement emplacement) {
        this.emplacaments.remove(emplacement);
        emplacement.setFormation(null);
        return this;
    }

    public Set<Inscription> getInscriptions() {
        return this.inscriptions;
    }

    public void setInscriptions(Set<Inscription> inscriptions) {
        if (this.inscriptions != null) {
            this.inscriptions.forEach(i -> i.setFormation(null));
        }
        if (inscriptions != null) {
            inscriptions.forEach(i -> i.setFormation(this));
        }
        this.inscriptions = inscriptions;
    }

    public Formation inscriptions(Set<Inscription> inscriptions) {
        this.setInscriptions(inscriptions);
        return this;
    }

    public Formation addInscription(Inscription inscription) {
        this.inscriptions.add(inscription);
        inscription.setFormation(this);
        return this;
    }

    public Formation removeInscription(Inscription inscription) {
        this.inscriptions.remove(inscription);
        inscription.setFormation(null);
        return this;
    }

    public Set<Subscriber> getSubscribers() {
        return this.subscribers;
    }

    public void setSubscribers(Set<Subscriber> subscribers) {
        this.subscribers = subscribers;
    }

    public Formation subscribers(Set<Subscriber> subscribers) {
        this.setSubscribers(subscribers);
        return this;
    }

    public Formation addSubscriber(Subscriber subscriber) {
        this.subscribers.add(subscriber);
        subscriber.getFormations().add(this);
        return this;
    }

    public Formation removeSubscriber(Subscriber subscriber) {
        this.subscribers.remove(subscriber);
        subscriber.getFormations().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Formation)) {
            return false;
        }
        return id != null && id.equals(((Formation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Formation{" +
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
            "}";
    }
}
