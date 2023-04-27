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
    public ModelAndView getCreatePage(Enrollment enrollment, ModelAndView model) {
        model.addObject("enrollment", enrollment);
        model.addObject("students", this.studentRepository.findAll());
        model.addObject("semesters", this.semesterRepository.findAll());
        model.setViewName("layouts/enrollment/create");
        return model;
    }

    @PostMapping("/create")
    public ModelAndView create(Enrollment enrollment, BindingResult bindingResult, ModelAndView model) {
        if (bindingResult.hasErrors()) {
            model.addObject("error", "Erro ao cadastrar declaração");
            model.setViewName("redirect:/");
            return model;
        }

        if (this.enrollmentRepository.findById(enrollment.getId()).isPresent()) {
            model.addObject("error", "Declaração já cadastrada");
            model.setViewName("redirect:/");
            return model;
        }

        Optional<Semester> semesterOptional = this.semesterRepository.findById(enrollment.getSemester().getId());

        if (semesterOptional.isEmpty()) {
            model.addObject("error", "Semestre não encontrado");
            model.setViewName("redirect:/");
            return model;
        }

        Optional<Student> studentOptional = this.studentRepository.findById(enrollment.getStudent().getId());

        if (studentOptional.isEmpty()) {
            model.addObject("error", "Estudante não encontrado");
            model.setViewName("redirect:/");
            return model;
        }

        Student student = studentOptional.get();

        enrollment.setStudent(student);
        enrollment.setSemester(semesterOptional.get());
        Enrollment savedEnrollment = this.enrollmentRepository.save(enrollment);

        student.setCurrentEnrollment(savedEnrollment);
        this.studentRepository.save(student);

        model.setViewName("redirect:/");
        return model;
    }

//    @GetMapping("/read/{id}")
//    public ModelAndView readOne(@PathVariable(name = "id") Integer id, ModelAndView model) {
//        Optional<Enrollment> enrollmentOptional = this.enrollmentRepository.findById(id);
//
//        if (enrollmentOptional.isEmpty()) {
//            model.addObject("error", "Declaração não encontrada");
//            model.setViewName("redirect:/");
//            return model;
//        }
//
//        model.addObject("enrollment", enrollmentOptional.get());
//        model.setViewName("/enrollment/read");
//        return model;
//    }
//
//    @GetMapping("/read-all")
//    public ModelAndView readAll(ModelAndView model) {
//        model.addObject("enrollments", this.enrollmentRepository.findAll());
//        model.setViewName("/enrollment/read-all");
//        return model;
//    }

//    @GetMapping("/update/{id}")
//    public ModelAndView getUpdatePage(@PathVariable(name = "id") Integer id, ModelAndView model) {
//        Optional<Enrollment> enrollmentOptional = this.enrollmentRepository.findById(id);
//
//        if (enrollmentOptional.isEmpty()) {
//            model.addObject("error", "Declaração não encontrada");
//            model.setViewName("redirect:/");
//            return model;
//        }
//
//        model.addObject("enrollment", enrollmentOptional.get());
//        model.setViewName("layouts/enrollment/update");
//        return model;
//    }
//
//    @PutMapping("/update")
//    public ModelAndView update(Enrollment enrollment, BindingResult bindingResult, ModelAndView model) {
//        if (bindingResult.hasErrors()) {
//            model.addObject("error", "Erro ao atualizar declaração");
//            model.setViewName("redirect:/");
//            return model;
//        }
//
//        Optional<Enrollment> enrollmentOptional = this.enrollmentRepository.findById(enrollment.getId());
//
//        if (enrollmentOptional.isEmpty()) {
//            model.addObject("error", "Declaração não encontrada");
//            model.setViewName("redirect:/");
//            return model;
//        }
//
//        Enrollment enrollmentToUpdate = enrollmentOptional.get();
//
//        enrollmentToUpdate.setObservation(enrollment.getObservation());
//
//        this.enrollmentRepository.save(enrollmentToUpdate);
//
//        model.setViewName("redirect:/");
//        return model;
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ModelAndView delete(@PathVariable(name = "id") Integer id, ModelAndView model) {
//        this.enrollmentRepository.deleteById(id);
//
//        model.setViewName("redirect:/");
//        return model;
//    }

}
