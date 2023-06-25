package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Document;
import br.edu.ifpb.pweb2.pederneira.model.Enrollment;
import br.edu.ifpb.pweb2.pederneira.model.Semester;
import br.edu.ifpb.pweb2.pederneira.model.Student;
import br.edu.ifpb.pweb2.pederneira.repository.EnrollmentRepository;
import br.edu.ifpb.pweb2.pederneira.repository.SemesterRepository;
import br.edu.ifpb.pweb2.pederneira.repository.StudentRepository;
import jakarta.annotation.Resource;
import br.edu.ifpb.pweb2.pederneira.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.List;
import java.util.Collections;
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
    @Resource
    private DocumentService documentService;

    @GetMapping
    public ModelAndView getHome(ModelAndView mav) {
        mav.addObject("enrollments", this.enrollmentRepository.findAll());
        mav.setViewName("layouts/enrollment/home");
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView getCreatePage(Enrollment enrollment, ModelAndView mav) {
        mav.addObject("enrollment", new Enrollment());
        mav.addObject("students", this.studentRepository.findAll());
        mav.addObject("semesters", this.semesterRepository.findAll());
        mav.setViewName("layouts/enrollment/create");
        return mav;
    }

    @GetMapping("/expired")
    public ModelAndView getExpiredEnrollments(ModelAndView mav) {
        List<Enrollment> expiredEnrollments = this.enrollmentRepository.findExpiredEnrollments();
        mav.addObject("enrollments", expiredEnrollments);
        mav.setViewName("layouts/enrollment/home");
        return mav;
    }


    @GetMapping("/ending-soon")
    public ModelAndView getEnrollmentsEndingSoon(@RequestParam(value = "days", required = false) Integer days, ModelAndView mav) {
        if (days == null) {
            mav.addObject("enrollments", Collections.emptyList());
        } else {
            LocalDate endDate = LocalDate.now().plusDays(days);
            List<Enrollment> enrollmentsEndingSoon = this.enrollmentRepository.findEnrollmentsEndingSoon(endDate);
            mav.addObject("enrollments", enrollmentsEndingSoon);
        }
        mav.setViewName("layouts/enrollment/home");
        return mav;
    }

    @PostMapping("/create")
    public ModelAndView create(Enrollment enrollment, BindingResult bindingResult, ModelAndView mav, RedirectAttributes redirectAttributes) {
        if (enrollment.getStudent() != null && enrollment.getSemester() == null) {
            mav.addObject("enrollment", enrollment);
            mav.addObject("selectedStudent", enrollment.getStudent());
            mav.addObject("students", this.studentRepository.findAll());
            mav.addObject("semesters", this.semesterRepository.findByInstitutionId(enrollment.getStudent().getCurrentInstitution().getId()));
            mav.setViewName("layouts/enrollment/create");
            return mav;
        }

        if (bindingResult.hasErrors()) {
            mav.setViewName("layouts/enrollment/create");
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

        mav.addObject("enrollment", savedEnrollment);
        mav.setViewName("redirect:/enrollment/create/upload");
        return mav;
    }

    @GetMapping("/create/upload")
    private ModelAndView getUploadPage(Enrollment enrollment, ModelAndView mav) {
        mav.addObject("enrollment", enrollment);
        mav.setViewName("/layouts/enrollment/upload");
        return mav;
    }

    @PostMapping("/create/upload/{id}")
    public ModelAndView upload(
            @PathVariable(name = "id") Integer id,
            @RequestParam("file") MultipartFile file,
            ModelAndView mav,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(id);
        if (optionalEnrollment.isPresent()) {
            Enrollment enrollment = optionalEnrollment.get();
            mav.addObject("enrollment", enrollment);
            try {
                String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                Document document = documentService.save(enrollment, filename, file.getBytes());
                document.setUrl(this.buildUrl(enrollment.getId(), document.getId()));
                enrollmentRepository.save(enrollment);
            } catch (NullPointerException npe) {
                redirectAttributes.addFlashAttribute("error", "Nenhum arquivo selecionado!");
                mav.setViewName("redirect:/enrollment/create/upload");
            } catch (IOException ioe) {
                redirectAttributes.addFlashAttribute("error", "Não foi possível carregar o documento!");
                mav.setViewName("redirect:/enrollment/create/upload");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Não foi possível cadastrar a declaração!");
            mav.setViewName("redirect:/enrollment/create/upload");
        }

        mav.setViewName("redirect:/student");
        return mav;
    }

    private String buildUrl(Integer idEnrollment, Integer idDocument) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/enrollment/")
                .path(String.valueOf(idEnrollment))
                .path("/document")
                .path(String.valueOf(idDocument))
                .toUriString();
    }

    @GetMapping("/student/{id}/enrollments")
    public ModelAndView viewEnrollments(@PathVariable("id")Integer studentId, ModelAndView mav){
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);

            mav.addObject("student", student);
            mav.addObject("enrollments", enrollments);
            mav.setViewName("layouts/student/update");
        } else {
            mav.setViewName("redirect:/student");
        }
        return mav;
    }
}
