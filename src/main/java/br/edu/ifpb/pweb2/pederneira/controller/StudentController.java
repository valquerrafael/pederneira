package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Enrollment;
import br.edu.ifpb.pweb2.pederneira.model.Institution;
import br.edu.ifpb.pweb2.pederneira.model.Student;
import br.edu.ifpb.pweb2.pederneira.repository.EnrollmentRepository;
import br.edu.ifpb.pweb2.pederneira.repository.InstitutionRepository;
import br.edu.ifpb.pweb2.pederneira.repository.StudentRepository;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
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

    @GetMapping
    public ModelAndView getHome(ModelAndView mav) {
        mav.addObject("students", this.studentRepository.findAll());
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

    @PostMapping("/create")
    public ModelAndView create(@Valid Student student, BindingResult bindingResult,
                               ModelAndView mav, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            mav.addObject("institutions", this.institutionRepository.findAll());
            mav.setViewName("layouts/student/create");
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
    public ModelAndView getUpdatePage(@PathVariable(name = "id") Integer id,
                                      ModelAndView mav, RedirectAttributes redirectAttributes) {
        Optional<Student> student = this.studentRepository.findById(id);

        if (student.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Estudante não encontrado");
            mav.setViewName("redirect:/student");
            return mav;
        }
        if (student.isPresent()) {
            Student students = student.get();
            List<Enrollment> enrollments = enrollmentRepository.findByStudent(students);

            mav.addObject("student", students);
            mav.addObject("enrollments", enrollments);
            mav.addObject("institutions", this.institutionRepository.findAll());
            mav.setViewName("layouts/student/update");
        } else {
            mav.setViewName("redirect:/student");
        }
        return mav;
    }

    @PutMapping("/update")
    public ModelAndView update(@Valid Student student, BindingResult bindingResult, Enrollment enrollment,
                               ModelAndView mav, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            mav.addObject("enrollments", this.enrollmentRepository.findAll());
            mav.addObject("institutions", this.institutionRepository.findAll());
            mav.setViewName("layouts/student/update");
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

        if (student.getCurrentEnrollment() == null) {
            redirectAttributes.addFlashAttribute("error", "Declaração não informada");
            mav.setViewName("redirect:/student");
            return mav;
        }

        Optional<Enrollment> enrollmentOptional = enrollmentRepository.findById(student.getCurrentEnrollment().getId());

        if (enrollmentOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Declaração não encontrada");
            mav.setViewName("redirect:/student");
            return mav;
        }

        Student studentToUpdate = studentOptional.get();

        studentToUpdate.setName(student.getName());
        studentToUpdate.setRegistration(student.getRegistration());
        studentToUpdate.setCurrentInstitution(institution.get());
        studentToUpdate.setCurrentEnrollment(enrollmentOptional.get());

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
