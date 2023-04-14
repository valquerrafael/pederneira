package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Declaration;
import br.edu.ifpb.pweb2.pederneira.repository.DeclarationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/declaration")
public class DeclarationController {

    @Autowired
    private DeclarationRepository repository;
    private final String templatesDirectory = "declaration";

    @GetMapping("/create")
    public String getCreatePage(Declaration declaration, Model model) {
        model.addAttribute("declaration", declaration);
        return this.templatesDirectory + "/create";
    }

    @PostMapping("/create")
    public String create(Declaration declaration) {
        this.repository.save(declaration);

        return "redirect:/";
    }

    @GetMapping("/read/{id}")
    public String readOne(@PathVariable(name = "id") Integer id, Model model) {
        Declaration declaration = this.repository.findById(id).orElseThrow();

        model.addAttribute("declaration", declaration);
        return this.templatesDirectory + "/read";
    }

    @GetMapping("/read-all")
    public String readAll(Model model) {
        List<Declaration> declarations = this.repository.findAll();

        model.addAttribute("declarations", declarations);
        return this.templatesDirectory + "/read-all";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(@PathVariable(name = "id") Integer id, Model model) {
        Declaration declaration = this.repository.findById(id).orElseThrow();

        model.addAttribute("declaration", declaration);
        return this.templatesDirectory + "/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Integer id, Declaration declaration) {
        Declaration declarationToUpdate = this.repository.findById(id).orElseThrow();

        declarationToUpdate.setReceptionDate(declaration.getReceptionDate());
        declarationToUpdate.setObservation(declaration.getObservation());

        this.repository.save(declarationToUpdate);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id) {
        this.repository.deleteById(id);

        return "redirect:/";
    }

}
