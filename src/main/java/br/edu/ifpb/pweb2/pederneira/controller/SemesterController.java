package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Semester;
import br.edu.ifpb.pweb2.pederneira.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/semester")
public class SemesterController {

    @Autowired
    private SemesterRepository repository;
    private final String templatesDirectory = "semester";

    @GetMapping("/create")
    public String getCreatePage(Semester semester, Model model) {
        model.addAttribute("semester", semester);
        return this.templatesDirectory + "/create";
    }

    @PostMapping("/create")
    public String create(Semester semester) {
        this.repository.save(semester);

        return "redirect:/";
    }

    @GetMapping("/read/{id}")
    public String readOne(@PathVariable(name = "id") Integer id, Model model) {
        Semester semester = this.repository.findById(id).orElseThrow();

        model.addAttribute("semester", semester);
        return this.templatesDirectory + "/read";
    }

    @GetMapping("/read-all")
    public String readAll(Model model) {
        List<Semester> semesters = this.repository.findAll();

        model.addAttribute("semesters", semesters);
        return this.templatesDirectory + "/read-all";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(@PathVariable(name = "id") Integer id, Model model) {
        Semester semester = this.repository.findById(id).orElseThrow();

        model.addAttribute("semester", semester);
        return this.templatesDirectory + "/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Integer id, Semester semester) {
        Semester semesterToUpdate = this.repository.findById(id).orElseThrow();

        semesterToUpdate.setYear(semester.getYear());
        semesterToUpdate.setSemester(semester.getSemester());
        semesterToUpdate.setStart(semester.getStart());
        semesterToUpdate.setEnd(semester.getEnd());

        this.repository.save(semesterToUpdate);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id) {
        this.repository.deleteById(id);

        return "redirect:/";
    }

}
