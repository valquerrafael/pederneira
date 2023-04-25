package br.edu.ifpb.pweb2.pederneira.repository;

import br.edu.ifpb.pweb2.pederneira.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
}
