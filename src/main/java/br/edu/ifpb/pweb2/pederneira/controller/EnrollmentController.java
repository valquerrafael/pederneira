package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Enrollment;
import br.edu.ifpb.pweb2.pederneira.model.Semester;
import br.edu.ifpb.pweb2.pederneira.model.Student;
import br.edu.ifpb.pweb2.pederneira.repository.EnrollmentRepository;
import br.edu.ifpb.pweb2.pederneira.repository.SemesterRepository;
import br.edu.ifpb.pweb2.pederneira.repository.StudentRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/enrollment")
public class EnrollmentController {

    @Resource
    private EnrollmentRepository enrollmentRepository;
    @Resource
    private StudentRepository studentRepository;
    @Resource
    private SemesterRepository semesterRepository;

    @GetMapping("/create")
    public ModelAndView getCreatePage(ModelAndView mav) {
        mav.addObject("enrollment", new Enrollment());
        mav.addObject("students", this.studentRepository.findAll());
        mav.addObject("semesters", this.semesterRepository.findAll());
        mav.setViewName("layouts/enrollment/create");
        return mav;
    }

    @PostMapping("/create")
    public ModelAndView create(Enrollment enrollment, BindingResult bindingResult, ModelAndView mav, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao cadastrar declaração");
            mav.setViewName("redirect:/student");
            return mav;
        }

        if (enrollment.getId() != null && this.enrollmentRepository.findById(enrollment.getId()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Declaração já cadastrada");
            mav.setViewName("redirect:/student");
            return mav;
        }

        Optional<Semester> semesterOptional = this.semesterRepository.findById(enrollment.getSemester().getId());

        if (semesterOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Semestre não encontrado");
            mav.setViewName("redirect:/student");
            return mav;
        }

        Optional<Student> studentOptional = this.studentRepository.findById(enrollment.getStudent().getId());

        if (studentOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Estudante não encontrado");
            mav.setViewName("redirect:/student");
            return mav;
        }

        Student student = studentOptional.get();

        enrollment.setStudent(student);
        enrollment.setSemester(semesterOptional.get());
        enrollment.setReceiptDate(LocalDate.now());
        Enrollment savedEnrollment = this.enrollmentRepository.save(enrollment);

        student.setCurrentEnrollment(savedEnrollment);
        this.studentRepository.save(student);

        mav.setViewName("redirect:/student");
        return mav;
    }

}
