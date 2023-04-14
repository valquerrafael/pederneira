package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Institution;
import br.edu.ifpb.pweb2.pederneira.repository.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/institution")
public class InstitutionController {

    @Autowired
    private InstitutionRepository repository;

    @GetMapping("/create")
    public String getCreatePage(Institution institution, Model model) {
        model.addAttribute("institution", institution);
        return "institution/create";
    }

    @PostMapping("/create")
    public String create(Institution institution) {
        this.repository.save(institution);

        return "redirect:/";
    }

    @GetMapping("/read/{id}")
    public String readOne(@PathVariable(name = "id") Integer id, Model model) {
        Institution institution = this.repository.findById(id).orElseThrow();

        model.addAttribute("institution", institution);
        return "institution/read";
    }

    @GetMapping("/read-all")
    public String readAll(Model model) {
        List<Institution> institutions = this.repository.findAll();

        model.addAttribute("institutions", institutions);
        return "institution/read-all";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(@PathVariable(name = "id") Integer id, Model model) {
        Institution institution = this.repository.findById(id).orElseThrow();

        model.addAttribute("institution", institution);
        return "institution/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Integer id, Institution institution) {
        Institution institutionToUpdate = this.repository.findById(id).orElseThrow();

        institutionToUpdate.setName(institution.getName());
        institutionToUpdate.setAcronym(institution.getAcronym());
        institutionToUpdate.setPhone(institution.getPhone());

        this.repository.save(institutionToUpdate);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id) {
        this.repository.deleteById(id);

        return "redirect:/";
    }

}
