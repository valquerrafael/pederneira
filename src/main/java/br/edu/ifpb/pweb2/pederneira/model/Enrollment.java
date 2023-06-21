package br.edu.ifpb.pweb2.pederneira.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    @Column(name = "receipt_date", nullable = false)
    private LocalDate receiptDate;
    @ManyToOne
    @JoinColumn(name = "semester_id")
    @NotNull(message = "Campo obrigatório!")
    private Semester semester;

    @NotNull(message = "Campo obrigatório!")
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    private String observation;

    public String getReceiptDateFormatted() {
        return receiptDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Override
    public String toString() {
        return "Data de recebimento: " + getReceiptDateFormatted() + " - Semestre: " + semester + " - Observação: " + observation;
    }

}
