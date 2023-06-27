package br.edu.ifpb.pweb2.pederneira.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name="semester")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Campo obrigatório!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "Data deve ser futura!")
    @Column(name = "start", nullable = false)
    private LocalDate start;

    @NotNull(message = "Campo obrigatório!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "Data deve ser futura!")
    @Column(name = "end", nullable = false)
    private LocalDate end;

    @NotNull(message = "Campo obrigatório!")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "institution_id", nullable = false)
    private Institution institution;

    @NotNull(message = "Campo obrigatório!")
    @Min(value = 2023, message = "O número deve ser maior ou igual a 2023.")
    @Column(name = "year", nullable = false)
    private Integer year;

    @Positive(message = "Número deve ser acima de zero.")
    @Column(name = "school_semester", nullable = false)
    private Integer schoolSemester;

    public String getStartFormatted() {
        return start.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getEndFormatted() {
        return end.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Override
    public String toString() {
        return year + "." + schoolSemester;
    }

}
