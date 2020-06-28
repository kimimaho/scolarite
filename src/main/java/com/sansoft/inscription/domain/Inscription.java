package com.sansoft.inscription.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Inscription.
 */
@Entity
@Table(name = "inscription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "inscription")
public class Inscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date_inscription", nullable = false)
    private LocalDate dateInscription;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "inscriptions", allowSetters = true)
    private Eleve eleve;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "inscriptions", allowSetters = true)
    private Niveau niveau;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "inscriptions", allowSetters = true)
    private AnneeScolaire anneeScolaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public Inscription dateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
        return this;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public Inscription eleve(Eleve eleve) {
        this.eleve = eleve;
        return this;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public Inscription niveau(Niveau niveau) {
        this.niveau = niveau;
        return this;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public AnneeScolaire getAnneeScolaire() {
        return anneeScolaire;
    }

    public Inscription anneeScolaire(AnneeScolaire anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
        return this;
    }

    public void setAnneeScolaire(AnneeScolaire anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
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
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inscription{" +
            "id=" + getId() +
            ", dateInscription='" + getDateInscription() + "'" +
            "}";
    }
}
