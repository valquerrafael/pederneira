package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Enrollment;
import br.edu.ifpb.pweb2.pederneira.model.Institution;
import br.edu.ifpb.pweb2.pederneira.model.Student;
import br.edu.ifpb.pweb2.pederneira.repository.EnrollmentRepository;
import br.edu.ifpb.pweb2.pederneira.repository.InstitutionRepository;
import br.edu.ifpb.pweb2.pederneira.repository.StudentRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Resource
    private StudentRepository studentRepository;
    @Resource
    private InstitutionRepository institutionRepository;
    @Resource
    private EnrollmentRepository enrollmentRepository;

    @GetMapping("/create")
    public ModelAndView getCreatePage(Student student, ModelAndView model) {
        model.addObject("student", student);
        model.setViewName("/student/create");
        return model;
    }

    @PostMapping("/create")
    public ModelAndView create(Student student, BindingResult bindingResult, ModelAndView model) {
        if (bindingResult.hasErrors()) {
            model.addObject("error", "Erro ao cadastrar estudante");
            model.setViewName("redirect:/");
            return model;
        }

        if (this.studentRepository.findById(student.getId()).isPresent()) {
            model.addObject("error", "Estudante já cadastrado");
            model.setViewName("redirect:/");
            return model;
        }

        this.studentRepository.save(student);

        model.setViewName("redirect:/");
        return model;
    }

    @GetMapping("/read/{id}")
    public ModelAndView readOne(@PathVariable(name = "id") Integer id, ModelAndView model) {
        Optional<Student> student = this.studentRepository.findById(id);

        if (student.isEmpty()) {
            model.addObject("error", "Estudante não encontrado");
            model.setViewName("redirect:/");
            return model;
        }

        model.addObject("student", student.get());
        model.setViewName("/student/read");
        return model;
    }

    @GetMapping("/read-all")
    public ModelAndView readAll(ModelAndView model) {
        model.addObject("students", this.studentRepository.findAll());
        model.setViewName("/student/read-all");
        return model;
    }

    @GetMapping("/update/{id}")
    public ModelAndView getUpdatePage(@PathVariable(name = "id") Integer id, ModelAndView model) {
        Optional<Student> student = this.studentRepository.findById(id);

        if (student.isEmpty()) {
            model.addObject("error", "Estudante não encontrado");
            model.setViewName("redirect:/");
            return model;
        }

        model.addObject("student", student.get());
        model.setViewName("/student/update");
        return model;
    }

    @PutMapping("/update")
    public ModelAndView update(Student student, BindingResult bindingResult, ModelAndView model) {
        if (bindingResult.hasErrors()) {
            model.addObject("error", "Erro ao atualizar estudante");
            model.setViewName("redirect:/");
            return model;
        }

        Optional<Student> studentOptional = this.studentRepository.findById(student.getId());

        if (studentOptional.isEmpty()) {
            model.addObject("error", "Estudante não encontrado");
            model.setViewName("redirect:/");
            return model;
        }

        Optional<Institution> institution = this.institutionRepository.findById(student.getCurrentInstitution().getId());

        if (institution.isEmpty()) {
            model.addObject("error", "Instituição não encontrada");
            model.setViewName("redirect:/");
            return model;
        }

        Optional<Enrollment> enrollment = this.enrollmentRepository.findById(student.getCurrentEnrollment().getId());

        if (enrollment.isEmpty()) {
            model.addObject("error", "Matrícula não encontrada");
            model.setViewName("redirect:/");
            return model;
        }

        Student studentToUpdate = studentOptional.get();

        studentToUpdate.setName(student.getName());
        studentToUpdate.setRegistration(student.getRegistration());
        studentToUpdate.setCurrentInstitution(institution.get());
        studentToUpdate.setCurrentEnrollment(enrollment.get());

        this.studentRepository.save(studentToUpdate);
        model.setViewName("redirect:/");
        return model;
    }

    @DeleteMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable(name = "id") Integer id, ModelAndView model) {
        this.studentRepository.deleteById(id);

        model.setViewName("redirect:/");
        return model;
    }

}
