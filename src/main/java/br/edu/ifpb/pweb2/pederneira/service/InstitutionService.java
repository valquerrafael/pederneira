package br.edu.ifpb.pweb2.pederneira.service;

import br.edu.ifpb.pweb2.pederneira.model.Institution;
import br.edu.ifpb.pweb2.pederneira.repository.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstitutionService {

    @Autowired
    private InstitutionRepository repository;

    public void register(Institution institution) {
        this.repository.save(institution);
    }

    public Institution readOne(Integer id) {
        return (this.repository.findById(id)).orElseThrow();
    }

    public void update(Integer id, Institution institution) {
        Institution institutionToUpdate = (this.repository.findById(id)).orElseThrow();

        institutionToUpdate.setName(institution.getName());
        institutionToUpdate.setAcronym(institution.getAcronym());
        institutionToUpdate.setPhone(institution.getPhone());
        this.repository.save(institutionToUpdate);
    }

    public void delete(Integer id) {
        this.repository.deleteById(id);
    }

}
