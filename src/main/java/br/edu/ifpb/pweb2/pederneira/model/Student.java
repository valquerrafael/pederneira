package br.edu.ifpb.pweb2.pederneira.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Campo obrigatório!")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Campo obrigatório!")
    @Size(max=8, message = "Matrícula deve ter no máximo 8 caracteres")
    private String registration;

    @Override
    public String toString() {
        return name + " - " + registration;
    }

}
