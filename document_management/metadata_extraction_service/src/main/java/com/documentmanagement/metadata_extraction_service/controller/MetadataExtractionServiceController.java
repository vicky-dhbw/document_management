package com.documentmanagement.metadata_extraction_service.controller;
import com.documentmanagement.metadata_extraction_service.dto.DocumentMetadata;
import com.documentmanagement.metadata_extraction_service.service.MetadataExtractionService;
import lombok.RequiredArgsConstructor;
import org.apache.tika.exception.TikaException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="http://localhost:64203")
@RequestMapping("/api/metadata-service")
public class MetadataExtractionServiceController {
    private final MetadataExtractionService metadataExtractionService;

    @CrossOrigin(origins="http://localhost:64203")
    @PostMapping("getMetadata")
    public ResponseEntity<DocumentMetadata> getDocumentMetadata(@RequestParam("file") MultipartFile file) throws TikaException, IOException, SAXException, ParseException {
        String originalFileName = file.getOriginalFilename();
        assert originalFileName != null;
        File tempFile = new File(System.getProperty("java.io.tmpdir"), originalFileName);
        file.transferTo(tempFile);
        DocumentMetadata metadata = metadataExtractionService.extractMetadata(tempFile);
        boolean deleted = tempFile.delete();
        if (!deleted) {
            System.out.println("Failed to delete temporary file: " + tempFile.getAbsolutePath());
        }

        return ResponseEntity.ok(metadata);
    }

    @GetMapping("/testing")
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a text to test the connection between the two servers.");
        return ResponseEntity.ok(response);
    }
}
