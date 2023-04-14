package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Student;
import br.edu.ifpb.pweb2.pederneira.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository repository;
    private final String templatesDirectory = "student";

    @GetMapping("/create")
    public String getCreatePage(Student student, Model model) {
        model.addAttribute("student", student);
        return this.templatesDirectory + "/create";
    }

    @PostMapping("/create")
    public String create(Student student) {
        this.repository.save(student);

        return "redirect:/";
    }

    @GetMapping("/read/{id}")
    public String readOne(@PathVariable(name = "id") Integer id, Model model) {
        Student student = this.repository.findById(id).orElseThrow();

        model.addAttribute("student", student);
        return this.templatesDirectory + "/read";
    }

    @GetMapping("/read-all")
    public String readAll(Model model) {
        List<Student> students = this.repository.findAll();

        model.addAttribute("students", students);
        return this.templatesDirectory + "/read-all";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(@PathVariable(name = "id") Integer id, Model model) {
        Student student = this.repository.findById(id).orElseThrow();

        model.addAttribute("student", student);
        return this.templatesDirectory + "/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Integer id, Student student) {
        Student studentToUpdate = this.repository.findById(id).orElseThrow();

        studentToUpdate.setName(student.getName());
        studentToUpdate.setRegistration(student.getRegistration());

        this.repository.save(studentToUpdate);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id) {
        this.repository.deleteById(id);

        return "redirect:/";
    }

}
