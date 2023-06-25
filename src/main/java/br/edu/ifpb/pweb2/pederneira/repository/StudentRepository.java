package br.edu.ifpb.pweb2.pederneira.repository;

import br.edu.ifpb.pweb2.pederneira.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("SELECT s FROM Student s LEFT JOIN s.currentEnrollment e WHERE e IS NULL")
    List<Student> findStudentsWithoutEnrollment();
}
