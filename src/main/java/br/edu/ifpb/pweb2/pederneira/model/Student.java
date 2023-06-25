package br.edu.ifpb.pweb2.pederneira.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="student")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    private Enrollment currentEnrollment;
    @OneToMany(mappedBy = "student", orphanRemoval = true)
    private List<Enrollment> enrollments;
    @ManyToOne
    @JoinColumn(name = "institution_id")
    private Institution currentInstitution;
    @Column(name = "name", nullable = false)
    private String name;
    private String registration;

    @Override
    public String toString() {
        return name + " - " + registration;
    }

}
