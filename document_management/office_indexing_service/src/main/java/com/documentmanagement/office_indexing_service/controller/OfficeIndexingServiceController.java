package com.documentmanagement.office_indexing_service.controller;
import com.documentmanagement.office_indexing_service.service.OfficeIndexingService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8094")
@RequestMapping("/api/office-indexer")
public class OfficeIndexingServiceController {
    private final OfficeIndexingService officeIndexingService;

    @PostMapping("extractFromDoc")
    @CrossOrigin(origins = "http://localhost:8094")
    public ResponseEntity<String> extractText(@RequestParam MultipartFile file) throws IOException, InvalidFormatException {
        String originalFileName = file.getOriginalFilename();
        assert originalFileName != null;
        File tempFile = new File(System.getProperty("java.io.tmpdir"), originalFileName);
        file.transferTo(tempFile);

        ResponseEntity<String> text=officeIndexingService.extractTextFromDocument(tempFile);

        boolean deleted = tempFile.delete();
        if (!deleted) {
            System.out.println("Failed to delete temporary file: " + tempFile.getAbsolutePath());
        }

        return text;
    }

}
