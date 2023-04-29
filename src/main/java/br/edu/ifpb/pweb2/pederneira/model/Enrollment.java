package br.edu.ifpb.pweb2.pederneira.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name="enrollment")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate receiptDate;
    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "student_id")
    private Student student;
    private String observation;

    @Override
    public String toString() {
        return "Data de recebimento: " + receiptDate + " - Semestre: " + semester + " - Observação: " + observation;
    }

}
