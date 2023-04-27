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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public ModelAndView getCreatePage(ModelAndView model) {
        model.addObject("student", new Student());
        model.addObject("institutions", this.institutionRepository.findAll());
        model.setViewName("layouts/student/create");
        return model;
    }

    @PostMapping("/create")
    public ModelAndView create(Student student, BindingResult bindingResult, ModelAndView model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao cadastrar estudante");
            model.setViewName("redirect:/home");
            return model;
        }

        if (student.getId() != null && this.studentRepository.findById(student.getId()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Estudante já cadastrado");
            model.setViewName("redirect:/home");
            return model;
        }

        if (student.getCurrentInstitution() == null) {
            redirectAttributes.addFlashAttribute("error", "Instituição não informada");
            model.setViewName("redirect:/home");
            return model;
        }

        Optional<Institution> institution = this.institutionRepository.findById(student.getCurrentInstitution().getId());

        if (institution.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Instituição não encontrada");
            model.setViewName("redirect:/home");
            return model;
        }

        this.studentRepository.save(student);

        model.setViewName("redirect:/home");
        return model;
    }

    @GetMapping("/read/{id}")
    public ModelAndView readOne(@PathVariable(name = "id") Integer id, ModelAndView model, RedirectAttributes redirectAttributes) {
        Optional<Student> student = this.studentRepository.findById(id);

        if (student.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Estudante não encontrado");
            model.setViewName("redirect:/home");
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
    public ModelAndView getUpdatePage(@PathVariable(name = "id") Integer id, ModelAndView model, RedirectAttributes redirectAttributes) {
        Optional<Student> student = this.studentRepository.findById(id);

        if (student.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Estudante não encontrado");
            model.setViewName("redirect:/home");
            return model;
        }

        model.addObject("student", student.get());
        model.addObject("institutions", this.institutionRepository.findAll());
        model.setViewName("layouts/student/update");
        return model;
    }

    @PutMapping("/update")
    public ModelAndView update(Student student, BindingResult bindingResult, ModelAndView model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar estudante");
            model.setViewName("redirect:/home");
            return model;
        }

        Optional<Student> studentOptional = this.studentRepository.findById(student.getId());

        if (studentOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Estudante não encontrado");
            model.setViewName("redirect:/home");
            return model;
        }

        if (student.getCurrentInstitution() == null) {
            redirectAttributes.addFlashAttribute("error", "Instituição não informada");
            model.setViewName("redirect:/home");
            return model;
        }

        Optional<Institution> institution = this.institutionRepository.findById(student.getCurrentInstitution().getId());

        if (institution.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Instituição não encontrada");
            model.setViewName("redirect:/home");
            return model;
        }

        Student studentToUpdate = studentOptional.get();

        studentToUpdate.setName(student.getName());
        studentToUpdate.setRegistration(student.getRegistration());
        studentToUpdate.setCurrentInstitution(institution.get());

        this.studentRepository.save(studentToUpdate);
        model.setViewName("redirect:/home");
        return model;
    }

    @DeleteMapping("/delete")
    public ModelAndView delete(Student student, ModelAndView model) {
        this.studentRepository.delete(student);

        model.setViewName("redirect:/home");
        return model;
    }

}
