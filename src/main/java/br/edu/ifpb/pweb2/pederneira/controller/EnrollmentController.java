package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Document;
import br.edu.ifpb.pweb2.pederneira.model.Enrollment;
import br.edu.ifpb.pweb2.pederneira.model.Semester;
import br.edu.ifpb.pweb2.pederneira.model.Student;
import br.edu.ifpb.pweb2.pederneira.repository.EnrollmentRepository;
import br.edu.ifpb.pweb2.pederneira.repository.SemesterRepository;
import br.edu.ifpb.pweb2.pederneira.repository.StudentRepository;
import br.edu.ifpb.pweb2.pederneira.service.DocumentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    @Resource
    private DocumentService documentService;

    @GetMapping("/create")
    public ModelAndView getCreatePage(Enrollment enrollment, ModelAndView mav) {
        mav.addObject("enrollment", new Enrollment());
        mav.addObject("students", this.studentRepository.findAll());
        mav.addObject("semesters", this.semesterRepository.findAll());
        mav.setViewName("layouts/enrollment/create");
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

    @PostMapping("/{id}/upload")
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("id") Integer id, ModelAndView mav) {
        try {
            Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(id);
            if (optionalEnrollment.isPresent()) {
                Enrollment enrollment = optionalEnrollment.get();
                String filename = StringUtils.cleanPath(file.getOriginalFilename());
                Document document = documentService.save(enrollment, filename, file.getBytes());
                document.setUrl(ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/enrollment/")
                        .path(String.valueOf(enrollment.getId()))
                        .path("/document/")
                        .path(String.valueOf(document.getId()))
                        .toUriString());
                enrollmentRepository.save(enrollment);
            }
        } catch (Exception e) {}
        return mav;
    }
}
