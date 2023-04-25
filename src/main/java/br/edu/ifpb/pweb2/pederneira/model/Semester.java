package br.edu.ifpb.pweb2.pederneira.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name="semester")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date start;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date end;
    @ManyToOne
    @JoinColumn(name = "institution_id")
    private Institution institution;
    private Integer year;
    private Integer semester;
}
