package br.edu.ifpb.pweb2.pederneira.repository;

import br.edu.ifpb.pweb2.pederneira.model.Enrollment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    List<Enrollment> findBySemesterId(Integer id);

}
