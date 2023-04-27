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
@RequestMapping("/institution")
public class InstitutionController {

    @Resource
    private InstitutionRepository institutionRepository;
    @Resource
    private SemesterRepository semesterRepository;

    @GetMapping("/create")
    public ModelAndView getCreatePage(ModelAndView model) {
        model.addObject("institution", new Institution());
        model.setViewName("layouts/institution/create");
        return model;
    }

    @PostMapping("/create")
    public ModelAndView create(Institution institution, BindingResult bindingResult, ModelAndView model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao cadastrar instituição");
            model.setViewName("redirect:/home");
            return model;
        }

        if (institution.getId() != null && this.institutionRepository.findById(institution.getId()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Instituição já cadastrada");
            model.setViewName("redirect:/home");
            return model;
        }

        this.institutionRepository.save(institution);

        model.setViewName("redirect:/home");
        return model;
    }

    @GetMapping("/read/{id}")
    public ModelAndView readOne(@PathVariable(name = "id") Integer id, ModelAndView model, RedirectAttributes redirectAttributes) {
        Optional<Institution> institution = this.institutionRepository.findById(id);

        if (institution.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Instituição não encontrada");
            model.setViewName("redirect:/home");
            return model;
        }

        model.addObject("institution", institution.get());
        model.setViewName("/institution/read");
        return model;
    }

    @GetMapping("/read-all")
    public ModelAndView readAll(ModelAndView model) {
        model.addObject("institutions", this.institutionRepository.findAll());
        model.setViewName("/institution/read-all");
        return model;
    }

    @GetMapping("/update/{id}")
    public ModelAndView getUpdatePage(@PathVariable(name = "id") Integer id, ModelAndView model, RedirectAttributes redirectAttributes) {
        Optional<Institution> institution = this.institutionRepository.findById(id);

        if (institution.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Instituição não encontrada");
            model.setViewName("redirect:/home");
            return model;
        }

        model.addObject("institution", institution.get());
        model.addObject("semesters", this.semesterRepository.findByInstitutionId(id));
        model.setViewName("layouts/institution/update");
        return model;
    }

    @PutMapping("/update")
    public ModelAndView update(Institution institution, BindingResult bindingResult, ModelAndView model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar instituição");
            model.setViewName("redirect:/home");
            return model;
        }

        Optional<Institution> institutionOptional = this.institutionRepository.findById(institution.getId());

        if (institutionOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Instituição não encontrada");
            model.setViewName("redirect:/home");
            return model;
        }

        Optional<Semester> semester = this.semesterRepository.findById(institution.getCurrentSemester().getId());

        if (semester.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Semestre atual não encontrado para instituição");
            model.setViewName("redirect:/home");
            return model;
        }

        Institution institutionToUpdate = institutionOptional.get();

        institutionToUpdate.setName(institution.getName());
        institutionToUpdate.setAcronym(institution.getAcronym());
        institutionToUpdate.setPhone(institution.getPhone());
        institutionToUpdate.setCurrentSemester(semester.get());

        this.institutionRepository.save(institutionToUpdate);

        model.setViewName("redirect:/home");
        return model;
    }

    @DeleteMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable(name = "id") Integer id, ModelAndView model) {
        this.institutionRepository.deleteById(id);

        model.setViewName("redirect:/home");
        return model;
    }

}
