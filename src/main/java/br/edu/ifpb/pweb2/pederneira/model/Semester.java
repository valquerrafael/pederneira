package br.edu.ifpb.pweb2.pederneira.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
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

//    @NotBlank(message = "Campo obrigatório!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "Data deve ser futura")
    @Column(name = "start", nullable = false)
    private LocalDate start;

//    @NotBlank(message = "Campo obrigatório!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @Future(message = "Data deve ser futura")
    @Column(name = "end", nullable = false)
    private LocalDate end;

//    @NotBlank(message = "Campo obrigatório!")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "institution_id", nullable = false)
    private Institution institution;
//    @NotBlank(message = "Campo obrigatório!")
    @Column(name = "year", nullable = false)
    private Integer year;

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
