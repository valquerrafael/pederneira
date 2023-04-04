package br.edu.ifpb.pweb2.pederneira.model;

import jakarta.persistence.*;

@Entity
@Table(name="institution")
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String acronym;
    private String phone;

    public Institution() {}

    public Institution(
        Integer id,
        String name,
        String acronym,
        String phone
    ) {
        super();
        this.id = id;
        this.name = name;
        this.acronym = acronym;
        this.phone = phone;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
