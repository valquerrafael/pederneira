package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Institution;
import br.edu.ifpb.pweb2.pederneira.model.Semester;
import br.edu.ifpb.pweb2.pederneira.repository.InstitutionRepository;
import br.edu.ifpb.pweb2.pederneira.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/institution")
public class InstitutionController {

    @Autowired
    private InstitutionRepository institutionRepository;
    @Autowired
    private SemesterRepository semesterRepository;
    private final String templatesDirectory = "institution";

    @GetMapping("/create")
    public String getCreatePage(Institution institution, Model model) {
        model.addAttribute("institution", institution);
        return this.templatesDirectory + "/create";
    }

    @PostMapping("/create")
    public String create(
        Institution institution,
        BindingResult result,
        RedirectAttributes redirectAttributes
    ) {
        this.institutionRepository.save(institution);

        return "redirect:/";
    }

    @GetMapping("/read/{id}")
    public String readOne(@PathVariable(name = "id") Integer id, Model model) {
        Institution institution = this.institutionRepository.findById(id).orElseThrow();

        model.addAttribute("institution", institution);
        return this.templatesDirectory + "/read";
    }

    @GetMapping("/read-all")
    public String readAll(Model model) {
        List<Institution> institutions = this.institutionRepository.findAll();

        model.addAttribute("institutions", institutions);
        return this.templatesDirectory + "/read-all";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(@PathVariable(name = "id") Integer id, Model model) {
        Institution institution = this.institutionRepository.findById(id).orElseThrow();

        model.addAttribute("institution", institution);
        return this.templatesDirectory + "/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Integer id, Institution institution) {
        Semester semester = this.semesterRepository.findById(institution.getCurrentSemester().getId()).orElseThrow();
        Institution institutionToUpdate = this.institutionRepository.findById(id).orElseThrow();

        if (!Objects.equals(semester.getInstitution().getId(), institutionToUpdate.getId())) {
            throw new RuntimeException("Semestre não pertence a instituição");
        }

        institutionToUpdate.setName(institution.getName());
        institutionToUpdate.setAcronym(institution.getAcronym());
        institutionToUpdate.setPhone(institution.getPhone());
        institutionToUpdate.setCurrentSemester(semester);

        this.institutionRepository.save(institutionToUpdate);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id) {
        this.institutionRepository.deleteById(id);

        return "redirect:/";
    }

}
