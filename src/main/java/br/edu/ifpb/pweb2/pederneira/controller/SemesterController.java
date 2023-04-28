package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Institution;
import br.edu.ifpb.pweb2.pederneira.model.Semester;
import br.edu.ifpb.pweb2.pederneira.repository.InstitutionRepository;
import br.edu.ifpb.pweb2.pederneira.repository.SemesterRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/semester")
public class SemesterController {

    @Resource
    private SemesterRepository semesterRepository;
    @Resource
    private InstitutionRepository institutionRepository;

    @GetMapping("/create")
    public ModelAndView getCreatePage(ModelAndView model) {
        model.addObject("semester", new Semester());
        model.addObject("institutions", this.institutionRepository.findAll());
        model.setViewName("layouts/semester/create");
        return model;
    }

    @PostMapping("/create")
    public ModelAndView create(Semester semester, BindingResult bindingResult, ModelAndView model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao cadastrar semestre");
            model.setViewName("redirect:/home");
            return model;
        }

        if (semester.getId() != null && this.semesterRepository.findById(semester.getId()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Semestre já cadastrado");
            model.setViewName("redirect:/home");
            return model;
        }

        if (semester.getInstitution() == null) {
            redirectAttributes.addFlashAttribute("error", "É necessário uma instituição");
            model.setViewName("redirect:/home");
            return model;
        }

        Optional<Institution> institutionOptional = this.institutionRepository.findById(semester.getInstitution().getId());

        if (institutionOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Instituição não encontrada");
            model.setViewName("redirect:/home");
            return model;
        }

        semester.getInstitution().setCurrentSemester(semester);
        this.semesterRepository.save(semester);

        redirectAttributes.addFlashAttribute("success", "Semestre cadastrado com sucesso");
        model.setViewName("redirect:/home");
        return model;
    }

    @GetMapping("/read/{id}")
    public ModelAndView readOne(@PathVariable(name = "id") Integer id, ModelAndView model, RedirectAttributes redirectAttributes) {
        Optional<Semester> semester = this.semesterRepository.findById(id);

        if (semester.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Semestre não encontrado");
            model.setViewName("redirect:/home");
            return model;
        }

        model.addObject("semester", semester.get());
        model.addObject("institutions", this.institutionRepository.findAll());
        model.setViewName("layouts/semester/read");
        return model;
    }

    @GetMapping("/read-all")
    public ModelAndView readAll(ModelAndView model) {
        model.addObject("semesters", this.semesterRepository.findAll());
        model.setViewName("layouts/semester/read-all");
        return model;
    }

    @GetMapping("/update/{id}")
    public ModelAndView getUpdatePage(@PathVariable(name = "id") Integer id, ModelAndView model, RedirectAttributes redirectAttributes) {
        Optional<Semester> semester = this.semesterRepository.findById(id);

        if (semester.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Semestre não encontrado");
            model.setViewName("redirect:/home");
            return model;
        }

        model.addObject("semester", semester.get());
        model.setViewName("layouts/semester/update");
        return model;
    }

    @PutMapping("/update")
    public ModelAndView update(Semester semester, BindingResult bindingResult, ModelAndView model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar semestre");
            model.setViewName("redirect:/home");
            return model;
        }

        Optional<Semester> semesterOptional = this.semesterRepository.findById(semester.getId());

        if (semesterOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Semestre não encontrado");
            model.setViewName("redirect:/home");
            return model;
        }

        Semester semesterToUpdate = semesterOptional.get();

        semesterToUpdate.setStart(semester.getStart());
        semesterToUpdate.setEnd(semester.getEnd());

        this.semesterRepository.save(semesterToUpdate);

        model.setViewName("redirect:/home");
        return model;
    }

    @DeleteMapping("/delete")
    public ModelAndView delete(Semester semester, ModelAndView model) {
        Optional<Institution> institution = this.institutionRepository.findById(semester.getInstitution().getId());
        if (institution.isPresent()
            && institution.get().getCurrentSemester() != null
            && institution.get().getCurrentSemester().getId().equals(semester.getId())) {
            Institution institutionToUpdate = institution.get();
            institutionToUpdate.setCurrentSemester(null);
            this.institutionRepository.save(institutionToUpdate);
        }

        this.semesterRepository.delete(semester);

        model.setViewName("redirect:/home");
        return model;
    }

}
