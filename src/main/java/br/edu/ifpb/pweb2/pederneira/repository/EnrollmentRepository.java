package br.edu.ifpb.pweb2.pederneira.repository;

import br.edu.ifpb.pweb2.pederneira.model.Enrollment;

import java.util.List;

import br.edu.ifpb.pweb2.pederneira.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    List<Enrollment> findBySemesterId(Integer id);

    List<Enrollment> findByStudent(Student student);
}
