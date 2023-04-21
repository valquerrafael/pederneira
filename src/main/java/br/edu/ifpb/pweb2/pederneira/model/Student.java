package br.edu.ifpb.pweb2.pederneira.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="student")
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "enrollment_id")
    private Enrollment currentEnrollment;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "institution_id")
    private Institution currentInstitution;
    private String name;
    private String registration;

    public Integer getId() {
        return this.id;
    }

    public List<Enrollment> getEnrollments() {
        return this.enrollments;
    }

    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
    }

    public void removeEnrollment(Enrollment enrollment) {
        this.enrollments.remove(enrollment);
    }

    public Enrollment getCurrentEnrollment() {
        return this.currentEnrollment;
    }

    public void setCurrentEnrollment(Enrollment enrollment) {
        this.currentEnrollment = enrollment;
    }

    public Institution getCurrentInstitution() {
        return this.currentInstitution;
    }

    public void setCurrentInstitution(Institution institution) {
        this.currentInstitution = institution;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistration() {
        return this.registration;
    }

    public void setRegistration(String acronym) {
        this.registration = acronym;
    }

}
