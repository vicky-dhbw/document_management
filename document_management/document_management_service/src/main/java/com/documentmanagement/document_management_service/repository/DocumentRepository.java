package com.documentmanagement.document_management_service.repository;

import com.documentmanagement.document_management_service.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document,Long> {
    interface DocumentSummary {
        String getTitle();
        String getFilename();
        double getFilesize();
    }
    List<DocumentSummary> findAllBy();

    @Query("SELECT d.filename, d.filesize, d.title FROM Document d WHERE d.text LIKE %:keyword%")
    List<Object[]> searchByText(String keyword);

    @Query("SELECT d.filename as filename, d.filesize as filesize, d.title as title FROM Document d JOIN d.tags t WHERE t.tagName = :tagName")
    List<Object[]> findByTagName(@Param("tagName") String tagName);

}
