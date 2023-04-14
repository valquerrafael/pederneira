package br.edu.ifpb.pweb2.pederneira.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name="declaration")
@AllArgsConstructor
@NoArgsConstructor
public class Declaration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date receptionDate;
    private String observation;

    public Integer getId() {
        return this.id;
    }

    public Date getReceptionDate() {
        return this.receptionDate;
    }

    public void setReceptionDate(Date name) {
        this.receptionDate = name;
    }

    public String getObservation() {
        return this.observation;
    }

    public void setObservation(String acronym) {
        this.observation = acronym;
    }

}
