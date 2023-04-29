package br.edu.ifpb.pweb2.pederneira.model;

import jakarta.persistence.*;
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start", nullable = false)
    private LocalDate start;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end", nullable = false)
    private LocalDate end;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "institution_id", nullable = false)
    private Institution institution;
    @Column(name = "year", nullable = false)
    private Integer year;
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
