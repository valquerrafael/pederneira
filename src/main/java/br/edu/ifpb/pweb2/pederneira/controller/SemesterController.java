package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Institution;
import br.edu.ifpb.pweb2.pederneira.model.Semester;
import br.edu.ifpb.pweb2.pederneira.repository.InstitutionRepository;
import br.edu.ifpb.pweb2.pederneira.repository.SemesterRepository;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping
    public ModelAndView getHome(
        ModelAndView mav,
        @RequestParam(defaultValue = "1") int page
    ) {
        int size = 3;
        Pageable paging = PageRequest.of(page - 1, size);
        mav.addObject("semesters", this.semesterRepository.findAll(paging));
        mav.setViewName("layouts/semester/home");
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView getCreatePage(ModelAndView mav) {
        mav.addObject("semester", new Semester());
        mav.addObject("institutions", this.institutionRepository.findAll());
        mav.setViewName("layouts/semester/create");
        return mav;
    }

    @PostMapping("/create")
    public ModelAndView create(@Valid Semester semester, BindingResult bindingResult, ModelAndView mav, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            mav.setViewName("layouts/semester/create");
            return mav;
        }

        if (semester.getId() != null && this.semesterRepository.findById(semester.getId()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Semestre já cadastrado");
            mav.setViewName("redirect:/semester");
            return mav;
        }

        if (semester.getInstitution() == null) {
            redirectAttributes.addFlashAttribute("error", "É necessário uma instituição");
            mav.setViewName("redirect:/semester");
            return mav;
        }

        Optional<Institution> institutionOptional = this.institutionRepository.findById(semester.getInstitution().getId());

        if (institutionOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Instituição não encontrada");
            mav.setViewName("redirect:/semester");
            return mav;
        }

        semester.getInstitution().setCurrentSemester(semester);
        this.semesterRepository.save(semester);

        redirectAttributes.addFlashAttribute("success", "Semestre cadastrado com sucesso");
        mav.setViewName("redirect:/semester");
        return mav;
    }

    @GetMapping("/update/{id}")
    public ModelAndView getUpdatePage(@PathVariable(name = "id") Integer id, ModelAndView mav, RedirectAttributes redirectAttributes) {
        Optional<Semester> semester = this.semesterRepository.findById(id);

        if (semester.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Semestre não encontrado");
            mav.setViewName("redirect:/semester");
            return mav;
        }

        mav.addObject("semester", semester.get());
        mav.setViewName("layouts/semester/update");
        return mav;
    }

    @PutMapping("/update")
    public ModelAndView update(@Valid Semester semester, BindingResult bindingResult, ModelAndView mav, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar semestre");
            mav.setViewName("redirect:/semester");
            return mav;
        }

        Optional<Semester> semesterOptional = this.semesterRepository.findById(semester.getId());

        if (semesterOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Semestre não encontrado");
            mav.setViewName("redirect:/semester");
            return mav;
        }

        Semester semesterToUpdate = semesterOptional.get();

        semesterToUpdate.setStart(semester.getStart());
        semesterToUpdate.setEnd(semester.getEnd());

        this.semesterRepository.save(semesterToUpdate);

        mav.setViewName("redirect:/semester");
        return mav;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable(name = "id") Integer id, ModelAndView mav) {
        Optional<Semester> semesterOptional = this.semesterRepository.findById(id);

        if (semesterOptional.isEmpty()) {
            mav.setViewName("redirect:/semester");
            return mav;
        }

        Semester semester = semesterOptional.get();

        Optional<Institution> institution = this.institutionRepository.findById(semester.getInstitution().getId());
        if (institution.isPresent()
            && institution.get().getCurrentSemester() != null
            && institution.get().getCurrentSemester().getId().equals(id)) {
            Institution institutionToUpdate = institution.get();
            institutionToUpdate.setCurrentSemester(null);
            this.institutionRepository.saveAndFlush(institutionToUpdate);
        }

        this.semesterRepository.deleteById(id);

        mav.setViewName("redirect:/semester");
        return mav;
    }

}
