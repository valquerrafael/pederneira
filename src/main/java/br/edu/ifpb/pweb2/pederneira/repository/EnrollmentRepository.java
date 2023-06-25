package br.edu.ifpb.pweb2.pederneira.repository;

import br.edu.ifpb.pweb2.pederneira.model.Document;
import br.edu.ifpb.pweb2.pederneira.model.Enrollment;

import java.util.List;

import br.edu.ifpb.pweb2.pederneira.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    List<Enrollment> findBySemesterId(Integer id);

    List<Enrollment> findByStudent(Student student);

    @Query(value = "select e.document from Enrollment e where e.id = :idEnrollment")
    Document findDocumentById(@Param("idEnrollment") Integer idEnrollment);

}
