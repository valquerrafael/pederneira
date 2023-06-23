package br.edu.ifpb.pweb2.pederneira.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="institution")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    private Semester currentSemester;
    @OneToMany(mappedBy = "institution", orphanRemoval = true)
    private List<Semester> semesters;
    @OneToMany(mappedBy = "currentInstitution", cascade = {CascadeType.MERGE})
    private List<Student> students;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "acronym", nullable = false)
    private String acronym;
    @Column(name = "phone", nullable = false)
    private String phone;

    @Override
    public String toString() {
        return name + " (" + acronym + ")";
    }

}
