package com.documentmanagement.pdf_indexing_service.controller;

import com.documentmanagement.pdf_indexing_service.*;
import com.documentmanagement.pdf_indexing_service.service.PdfIndexingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pdf-indexer")
@CrossOrigin(origins = "http://localhost:8094")
public class PdfIndexingServiceController {

    private final PdfIndexingService pdfIndexingService;

    @PostMapping("/extractFromDoc")
    @CrossOrigin(origins = "http://localhost:8094")
    public ResponseEntity<String> extractText(@RequestParam MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        assert originalFileName != null;
        File tempFile = new File(System.getProperty("java.io.tmpdir"), originalFileName);
        file.transferTo(tempFile);
        ResponseEntity<String> text=pdfIndexingService.extractTextFromPdf(tempFile);

        boolean deleted = tempFile.delete();
        if (!deleted) {
            System.out.println("Failed to delete temporary file: " + tempFile.getAbsolutePath());
        }

        return text;
    }

}
