package br.edu.ifpb.pweb2.pederneira.service;

import br.edu.ifpb.pweb2.pederneira.model.Document;
import br.edu.ifpb.pweb2.pederneira.model.Enrollment;
import br.edu.ifpb.pweb2.pederneira.repository.DocumentRepository;
import br.edu.ifpb.pweb2.pederneira.repository.EnrollmentRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class DocumentService {

    @Resource
    private DocumentRepository documentRepository;
    @Resource
    private EnrollmentRepository enrollmentRepository;

    public Document save(Enrollment enrollment, String filename, byte[] bytes) throws IOException {
        Document document = new Document(filename, bytes);
        enrollment.setDocument(document);
        documentRepository.save(document);
        return document;
    }

    public Document getDocument(Integer id) {
        return documentRepository.findById(id).get();
    }

    public Optional<Document> getDocumentOf(Integer idEnrollment) {
        return Optional.ofNullable(enrollmentRepository.findDocumentById(idEnrollment));
    }
}
