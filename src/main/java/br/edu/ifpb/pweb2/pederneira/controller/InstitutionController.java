package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Enrollment;
import br.edu.ifpb.pweb2.pederneira.model.Institution;
import br.edu.ifpb.pweb2.pederneira.model.Semester;
import br.edu.ifpb.pweb2.pederneira.model.Student;
import br.edu.ifpb.pweb2.pederneira.repository.EnrollmentRepository;
import br.edu.ifpb.pweb2.pederneira.repository.InstitutionRepository;
import br.edu.ifpb.pweb2.pederneira.repository.SemesterRepository;
import br.edu.ifpb.pweb2.pederneira.repository.StudentRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/institution")
public class InstitutionController {

    @Resource
    private InstitutionRepository institutionRepository;
    @Resource
    private SemesterRepository semesterRepository;
    @Resource
    private StudentRepository studentRepository;
    @Resource
    private EnrollmentRepository enrollmentRepository;

    @GetMapping
    public ModelAndView getHome(ModelAndView mav) {
        mav.addObject("institutions", this.institutionRepository.findAll());
        mav.setViewName("layouts/institution/home");
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView getCreatePage(ModelAndView mav) {
        mav.addObject("institution", new Institution());
        mav.setViewName("layouts/institution/create");
        return mav;
    }

    @PostMapping("/create")
    public ModelAndView create(Institution institution, BindingResult bindingResult, ModelAndView mav, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao cadastrar instituição");
            mav.setViewName("redirect:/institution");
            return mav;
        }

        if (institution.getId() != null && this.institutionRepository.findById(institution.getId()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Instituição já cadastrada");
            mav.setViewName("redirect:/institution");
            return mav;
        }

        this.institutionRepository.save(institution);

        mav.setViewName("redirect:/institution");
        return mav;
    }

    @GetMapping("/update/{id}")
    public ModelAndView getUpdatePage(@PathVariable(name = "id") Integer id, ModelAndView mav, RedirectAttributes redirectAttributes) {
        Optional<Institution> institution = this.institutionRepository.findById(id);

        if (institution.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Instituição não encontrada");
            mav.setViewName("redirect:/institution");
            return mav;
        }

        mav.addObject("institution", institution.get());
        mav.setViewName("layouts/institution/update");
        return mav;
    }

    @PutMapping("/update")
    public ModelAndView update(Institution institution, BindingResult bindingResult, ModelAndView mav, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar instituição");
            mav.setViewName("redirect:/institution");
            return mav;
        }

        Optional<Institution> institutionOptional = this.institutionRepository.findById(institution.getId());

        if (institutionOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Instituição não encontrada");
            mav.setViewName("redirect:/institution");
            return mav;
        }

        Institution institutionToUpdate = institutionOptional.get();

        if (institution.getCurrentSemester() != null) {
            Optional<Semester> semester = this.semesterRepository.findById(institution.getCurrentSemester().getId());

            if (semester.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Semestre atual não encontrado para instituição");
                mav.setViewName("redirect:/institution");
                return mav;
            }

            institutionToUpdate.setCurrentSemester(semester.get());
        }

        institutionToUpdate.setName(institution.getName());
        institutionToUpdate.setAcronym(institution.getAcronym());
        institutionToUpdate.setPhone(institution.getPhone());

        this.institutionRepository.save(institutionToUpdate);

        mav.setViewName("redirect:/institution");
        return mav;
    }
    
    @GetMapping("/delete/{id}")
    private ModelAndView delete(@PathVariable(name = "id") Integer id, ModelAndView mav) {
        Optional<Institution> institutionOptional = this.institutionRepository.findById(id);

        if (institutionOptional.isEmpty()) {
            mav.setViewName("redirect:/institution");
            return mav;
        }

        Institution institution = institutionOptional.get();

        for (Student student : institution.getStudents()) {
            student.setCurrentInstitution(null);
            student.setCurrentEnrollment(null);
        }

        for (Semester semester : institution.getSemesters()) {
            List<Enrollment> enrollments = this.enrollmentRepository.findBySemesterId(semester.getId());

            for (Enrollment enrollment : enrollments) {
                if (enrollment.equals(enrollment.getStudent().getCurrentEnrollment())) {
                    enrollment.getStudent().setCurrentEnrollment(null);
                    this.studentRepository.saveAndFlush(enrollment.getStudent());
                }
            }

            this.enrollmentRepository.deleteAll(enrollments);
        }

        this.studentRepository.saveAll(institution.getStudents());

        this.institutionRepository.deleteById(id);

        mav.setViewName("redirect:/institution");
        return mav;
    }

}
