package br.edu.ifpb.pweb2.pederneira.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="institution")
@AllArgsConstructor
@NoArgsConstructor
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToMany(mappedBy = "institution")
    private List<Semester> semesters;
    @OneToOne
    @JoinColumn(name = "current_semester_id")
    private Semester currentSemester;
    private String name;
    private String acronym;
    private String phone;


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Semester> getSemesters() {
        return this.semesters;
    }

    public void addSemester(Semester semester) {
        this.semesters.add(semester);
    }

    public void removeSemester(Semester semester) {
        this.semesters.remove(semester);
    }

    public Semester getCurrentSemester() {
        return this.currentSemester;
    }

    public void setCurrentSemester(Semester currentSemester) {
        this.currentSemester = currentSemester;
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
