package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Institution;
import br.edu.ifpb.pweb2.pederneira.model.Semester;
import br.edu.ifpb.pweb2.pederneira.repository.InstitutionRepository;
import br.edu.ifpb.pweb2.pederneira.repository.SemesterRepository;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/semester")
public class SemesterController {

    @Resource
    private SemesterRepository semesterRepository;
    @Resource
    private InstitutionRepository institutionRepository;

    @GetMapping("/create")
    public ModelAndView getCreatePage(Semester semester, ModelAndView model) {
        model.addObject("semester", semester);
        model.setViewName("layouts/semester/create");
        return model;
    }

    @PostMapping("/create")
    public ModelAndView create(Semester semester, BindingResult bindingResult, ModelAndView model) {
        if (bindingResult.hasErrors()) {
            model.addObject("error", "Erro ao cadastrar semestre");
            model.setViewName("redirect:/");
            return model;
        }

        if (this.semesterRepository.findById(semester.getId()).isPresent()) {
            model.addObject("error", "Semestre já cadastrado");
            model.setViewName("redirect:/");
            return model;
        }

        Optional<Institution> institutionOptional = this.institutionRepository.findById(semester.getInstitution().getId());

        if (institutionOptional.isEmpty()) {
            model.addObject("error", "Instituição não encontrada");
            model.setViewName("redirect:/");
            return model;
        }

        Semester createdSemester = this.semesterRepository.save(semester);
        Institution institution = institutionOptional.get();

        institution.setCurrentSemester(createdSemester);
        this.institutionRepository.save(institution);

        model.setViewName("redirect:/");
        return model;
    }

//    @GetMapping("/read/{id}")
//    public ModelAndView readOne(@PathVariable(name = "id") Integer id, ModelAndView model) {
//        Optional<Semester> semester = this.semesterRepository.findById(id);
//
//        if (semester.isEmpty()) {
//            model.addObject("error", "Semestre não encontrado");
//            model.setViewName("redirect:/");
//            return model;
//        }
//
//        model.addObject("semester", semester.get());
//        model.setViewName("/semester/read");
//        return model;
//    }
//
//    @GetMapping("/read-all")
//    public ModelAndView readAll(ModelAndView model) {
//        model.addObject("semesters", this.semesterRepository.findAll());
//        model.setViewName("/semester/read-all");
//        return model;
//    }

    @GetMapping("/update/{id}")
    public ModelAndView getUpdatePage(@PathVariable(name = "id") Integer id, ModelAndView model) {
        Optional<Semester> semester = this.semesterRepository.findById(id);

        if (semester.isEmpty()) {
            model.addObject("error", "Semestre não encontrado");
            model.setViewName("redirect:/");
            return model;
        }

        model.addObject("semester", semester.get());
        model.setViewName("layouts/semester/update");
        return model;
    }

    @PutMapping("/update")
    public ModelAndView update(Semester semester, BindingResult bindingResult, ModelAndView model) {
        if (bindingResult.hasErrors()) {
            model.addObject("error", "Erro ao atualizar semestre");
            model.setViewName("redirect:/");
            return model;
        }

        Optional<Semester> semesterOptional = this.semesterRepository.findById(semester.getId());

        if (semesterOptional.isEmpty()) {
            model.addObject("error", "Semestre não encontrado");
            model.setViewName("redirect:/");
            return model;
        }

        Semester semesterToUpdate = semesterOptional.get();

        semesterToUpdate.setStart(semester.getStart());
        semesterToUpdate.setEnd(semester.getEnd());

        this.semesterRepository.save(semesterToUpdate);

        model.setViewName("redirect:/");
        return model;
    }

//    @DeleteMapping("/delete/{id}")
//    @Transactional
//    public ModelAndView delete(@PathVariable(name = "id") Integer id, ModelAndView model) {
//        this.semesterRepository.deleteById(id);
//
//        model.setViewName("redirect:/");
//        return model;
//    }

}
