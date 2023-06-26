package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Enrollment;
import br.edu.ifpb.pweb2.pederneira.model.Institution;
import br.edu.ifpb.pweb2.pederneira.model.Student;
import br.edu.ifpb.pweb2.pederneira.repository.InstitutionRepository;
import br.edu.ifpb.pweb2.pederneira.repository.StudentRepository;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping
    public ModelAndView getHome(
        ModelAndView mav,
        @RequestParam(defaultValue = "1") int page
    ) {
        int size = 3;
        Pageable paging = PageRequest.of(page - 1, size);
        mav.addObject("students", this.studentRepository.findAll(paging));
        mav.setViewName("layouts/student/home");
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView getCreatePage(ModelAndView mav) {
        mav.addObject("student", new Student());
        mav.addObject("institutions", this.institutionRepository.findAll());
        mav.setViewName("layouts/student/create");
        return mav;
    }

    @GetMapping("/expired")
    public ModelAndView getexpiredEnrollment(ModelAndView mav) {
        mav.addObject("students", this.studentRepository.findStudentsWithoutEnrollment());
        mav.setViewName("layouts/student/home");
        return mav;
    }

    @PostMapping("/create")
    public ModelAndView create(Student student, BindingResult bindingResult, ModelAndView mav, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao cadastrar estudante");
            mav.setViewName("redirect:/student");
            return mav;
        }

        if (student.getId() != null && this.studentRepository.findById(student.getId()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Estudante já cadastrado");
            mav.setViewName("redirect:/student");
            return mav;
        }

        if (student.getCurrentInstitution() == null) {
            redirectAttributes.addFlashAttribute("error", "Instituição não informada");
            mav.setViewName("redirect:/student");
            return mav;
        }

        Optional<Institution> institution = this.institutionRepository.findById(student.getCurrentInstitution().getId());

        if (institution.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Instituição não encontrada");
            mav.setViewName("redirect:/student");
            return mav;
        }

        this.studentRepository.save(student);

        mav.setViewName("redirect:/student");
        return mav;
    }

    @GetMapping("/update/{id}")
    public ModelAndView getUpdatePage(@PathVariable(name = "id") Integer id, ModelAndView mav, RedirectAttributes redirectAttributes) {
        Optional<Student> student = this.studentRepository.findById(id);

        if (student.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Estudante não encontrado");
            mav.setViewName("redirect:/student");
            return mav;
        }

        mav.addObject("student", student.get());
        mav.addObject("institutions", this.institutionRepository.findAll());
        mav.setViewName("layouts/student/update");
        return mav;
    }

    @PutMapping("/update")
    public ModelAndView update(Student student, BindingResult bindingResult, ModelAndView mav, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar estudante");
            mav.setViewName("redirect:/student");
            return mav;
        }

        Optional<Student> studentOptional = this.studentRepository.findById(student.getId());

        if (studentOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Estudante não encontrado");
            mav.setViewName("redirect:/student");
            return mav;
        }

        if (student.getCurrentInstitution() == null) {
            redirectAttributes.addFlashAttribute("error", "Instituição não informada");
            mav.setViewName("redirect:/student");
            return mav;
        }

        Optional<Institution> institution = this.institutionRepository.findById(student.getCurrentInstitution().getId());

        if (institution.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Instituição não encontrada");
            mav.setViewName("redirect:/student");
            return mav;
        }

        Student studentToUpdate = studentOptional.get();

        studentToUpdate.setName(student.getName());
        studentToUpdate.setRegistration(student.getRegistration());
        studentToUpdate.setCurrentInstitution(institution.get());

        this.studentRepository.save(studentToUpdate);
        mav.setViewName("redirect:/student");
        return mav;
    }

    @GetMapping("/delete/{id}")
    private ModelAndView delete(@PathVariable(name = "id") Integer id, ModelAndView mav) {
        this.studentRepository.deleteById(id);

        mav.setViewName("redirect:/student");
        return mav;
    }

}
