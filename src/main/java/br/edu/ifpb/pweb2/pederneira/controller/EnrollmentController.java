package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Enrollment;
import br.edu.ifpb.pweb2.pederneira.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/enrollment")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository repository;
    private final String templatesDirectory = "enrollment";

    @GetMapping("/create")
    public String getCreatePage(Enrollment enrollment, Model model) {
        model.addAttribute("enrollment", enrollment);
        return this.templatesDirectory + "/create";
    }

    @PostMapping("/create")
    public String create(Enrollment enrollment) {
        this.repository.save(enrollment);

        return "redirect:/";
    }

    @GetMapping("/read/{id}")
    public String readOne(@PathVariable(name = "id") Integer id, Model model) {
        Enrollment enrollment = this.repository.findById(id).orElseThrow();

        model.addAttribute("enrollment", enrollment);
        return this.templatesDirectory + "/read";
    }

    @GetMapping("/read-all")
    public String readAll(Model model) {
        List<Enrollment> enrollments = this.repository.findAll();

        model.addAttribute("enrollments", enrollments);
        return this.templatesDirectory + "/read-all";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(@PathVariable(name = "id") Integer id, Model model) {
        Enrollment enrollment = this.repository.findById(id).orElseThrow();

        model.addAttribute("enrollment", enrollment);
        return this.templatesDirectory + "/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Integer id, Enrollment enrollment) {
        Enrollment enrollmentToUpdate = this.repository.findById(id).orElseThrow();

        enrollmentToUpdate.setReceptionDate(enrollment.getReceptionDate());
        enrollmentToUpdate.setObservation(enrollment.getObservation());

        this.repository.save(enrollmentToUpdate);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id) {
        this.repository.deleteById(id);

        return "redirect:/";
    }

}
