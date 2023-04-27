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

import java.util.Optional;

@Controller
@RequestMapping("/institution")
public class InstitutionController {

    @Resource
    private InstitutionRepository institutionRepository;
    @Resource
    private SemesterRepository semesterRepository;

    @GetMapping("/create")
    public ModelAndView getCreatePage(Institution institution, ModelAndView model) {
        model.addObject("institution", institution);
        model.setViewName("layouts/institution/create");
        return model;
    }

    @PostMapping("/create")
    public ModelAndView create(Institution institution, BindingResult bindingResult, ModelAndView model) {
        if (bindingResult.hasErrors()) {
            model.addObject("error", "Erro ao cadastrar instituição");
            model.setViewName("redirect:/");
            return model;
        }

        if (this.institutionRepository.findById(institution.getId()).isPresent()) {
            model.addObject("error", "Instituição já cadastrada");
            model.setViewName("redirect:/");
            return model;
        }

        this.institutionRepository.save(institution);

        model.setViewName("redirect:/");
        return model;
    }

//    @GetMapping("/read/{id}")
//    public ModelAndView readOne(@PathVariable(name = "id") Integer id, ModelAndView model) {
//        Optional<Institution> institution = this.institutionRepository.findById(id);
//
//        if (institution.isEmpty()) {
//            model.addObject("error", "Instituição não encontrada");
//            model.setViewName("redirect:/");
//            return model;
//        }
//
//        model.addObject("institution", institution.get());
//        model.setViewName("/institution/read");
//        return model;
//    }
//
//    @GetMapping("/read-all")
//    public ModelAndView readAll(ModelAndView model) {
//        model.addObject("institutions", this.institutionRepository.findAll());
//        model.setViewName("/institution/read-all");
//        return model;
//    }

    @GetMapping("/update/{id}")
    public ModelAndView getUpdatePage(@PathVariable(name = "id") Integer id, ModelAndView model) {
        Optional<Institution> institution = this.institutionRepository.findById(id);

        if (institution.isEmpty()) {
            model.addObject("error", "Instituição não encontrada");
            model.setViewName("redirect:/");
            return model;
        }

        model.addObject("institution", institution.get());
        model.addObject("semesters", this.semesterRepository.findByInstitutionId(id));
        model.setViewName("layouts/institution/update");
        return model;
    }

    @PutMapping("/update")
    public ModelAndView update(Institution institution, BindingResult bindingResult, ModelAndView model) {
        if (bindingResult.hasErrors()) {
            model.addObject("error", "Erro ao atualizar instituição");
            model.setViewName("redirect:/");
            return model;
        }

        Optional<Institution> institutionOptional = this.institutionRepository.findById(institution.getId());

        if (institutionOptional.isEmpty()) {
            model.addObject("error", "Instituição não encontrada");
            model.setViewName("redirect:/");
            return model;
        }

        Optional<Semester> semester = this.semesterRepository.findById(institution.getCurrentSemester().getId());

        if (semester.isEmpty()) {
            model.addObject("error", "Semestre atual não encontrado para instituição");
            model.setViewName("redirect:/");
            return model;
        }

        Institution institutionToUpdate = institutionOptional.get();

        institutionToUpdate.setName(institution.getName());
        institutionToUpdate.setAcronym(institution.getAcronym());
        institutionToUpdate.setPhone(institution.getPhone());
        institutionToUpdate.setCurrentSemester(semester.get());

        this.institutionRepository.save(institutionToUpdate);

        model.setViewName("redirect:/");
        return model;
    }

//    @DeleteMapping("/delete/{id}")
//    public ModelAndView delete(@PathVariable(name = "id") Integer id, ModelAndView model) {
//        this.institutionRepository.deleteById(id);
//
//        model.setViewName("redirect:/");
//        return model;
//    }

}
