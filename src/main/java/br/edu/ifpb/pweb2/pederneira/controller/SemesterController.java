package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Institution;
import br.edu.ifpb.pweb2.pederneira.model.Semester;
import br.edu.ifpb.pweb2.pederneira.repository.InstitutionRepository;
import br.edu.ifpb.pweb2.pederneira.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/semester")
public class SemesterController {

    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private InstitutionRepository institutionRepository;
    private final String templatesDirectory = "semester";

    @GetMapping("/create")
    public String getCreatePage(Model model) {
        Institution institution = new Institution();
        Semester semester = new Semester();
        semester.setInstitution(institution);
        model.addAttribute("semester", semester);
        return this.templatesDirectory + "/create";
    }

    // TODO: verify error institution identifier is replaced from X to Y
    @PostMapping("/create")
    public String create(Semester semester) {
        Institution institutionToBeUpdated = this.institutionRepository.findById(semester.getInstitution().getId()).orElseThrow();
        Semester semesterToBeCreated = new Semester(
            null, semester.getStart(), semester.getEnd(), institutionToBeUpdated, semester.getYear(), semester.getSemester()
        );
        Semester createdSemester = this.semesterRepository.save(semesterToBeCreated);

        institutionToBeUpdated.setCurrentSemester(createdSemester);
        this.institutionRepository.save(institutionToBeUpdated);

        return "redirect:/";
    }

    @GetMapping("/read/{id}")
    public String readOne(@PathVariable(name = "id") Integer id, Model model) {
        Semester semester = this.semesterRepository.findById(id).orElseThrow();

        model.addAttribute("semester", semester);
        return this.templatesDirectory + "/read";
    }

    @GetMapping("/read-all")
    public String readAll(Model model) {
        List<Semester> semesters = this.semesterRepository.findAll();

        model.addAttribute("semesters", semesters);
        return this.templatesDirectory + "/read-all";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(@PathVariable(name = "id") Integer id, Model model) {
        Semester semester = this.semesterRepository.findById(id).orElseThrow();

        model.addAttribute("semester", semester);
        return this.templatesDirectory + "/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Integer id, Semester semester) {
        Semester semesterToUpdate = this.semesterRepository.findById(id).orElseThrow();

        semesterToUpdate.setYear(semester.getYear());
        semesterToUpdate.setSemester(semester.getSemester());
        semesterToUpdate.setStart(semester.getStart());
        semesterToUpdate.setEnd(semester.getEnd());

        this.semesterRepository.save(semesterToUpdate);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id) {
        this.semesterRepository.deleteById(id);

        return "redirect:/";
    }

}
