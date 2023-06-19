package br.edu.ifpb.pweb2.pederneira.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "Campo obrigatório!")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Campo obrigatório!")
    @Column(name = "acronym", nullable = false)
    private String acronym;

    @Pattern(regexp = "\\(\\d{2}\\)\\s?\\d{4,5}-\\d{4}",
            message = "O telefone deve estar no formato (99) 9999-9999 ou (99) 99999-9999")
    @Column(name = "phone", nullable = false)
    private String phone;

    @Override
    public String toString() {
        return name + " (" + acronym + ")";
    }

}
