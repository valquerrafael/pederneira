package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Enrollment;
import br.edu.ifpb.pweb2.pederneira.model.Student;
import br.edu.ifpb.pweb2.pederneira.repository.EnrollmentRepository;
import br.edu.ifpb.pweb2.pederneira.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/enrollment")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private StudentRepository studentRepository;
    private final String templatesDirectory = "enrollment";

    @GetMapping("/create")
    public String getCreatePage(Enrollment enrollment, Model model) {
        model.addAttribute("enrollment", enrollment);
        return this.templatesDirectory + "/create";
    }

    @PostMapping("/create")
    public String create(Enrollment enrollment) {
        Student student = this.studentRepository.findById(enrollment.getStudent().getId()).orElseThrow();

        enrollment.setStudent(student);
        Enrollment savedEnrollment = this.enrollmentRepository.save(enrollment);

        student.setCurrentEnrollment(savedEnrollment);
        this.studentRepository.save(student);

        return "redirect:/";
    }

    @GetMapping("/read/{id}")
    public String readOne(@PathVariable(name = "id") Integer id, Model model) {
        Enrollment enrollment = this.enrollmentRepository.findById(id).orElseThrow();

        model.addAttribute("enrollment", enrollment);
        return this.templatesDirectory + "/read";
    }

    @GetMapping("/read-all")
    public String readAll(Model model) {
        List<Enrollment> enrollments = this.enrollmentRepository.findAll();

        model.addAttribute("enrollments", enrollments);
        return this.templatesDirectory + "/read-all";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(@PathVariable(name = "id") Integer id, Model model) {
        Enrollment enrollment = this.enrollmentRepository.findById(id).orElseThrow();

        model.addAttribute("enrollment", enrollment);
        return this.templatesDirectory + "/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Integer id, Enrollment enrollment) {
        Enrollment enrollmentToUpdate = this.enrollmentRepository.findById(id).orElseThrow();

        enrollmentToUpdate.setReceptionDate(enrollment.getReceptionDate());
        enrollmentToUpdate.setObservation(enrollment.getObservation());

        this.enrollmentRepository.save(enrollmentToUpdate);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id) {
        this.enrollmentRepository.deleteById(id);

        return "redirect:/";
    }

}
