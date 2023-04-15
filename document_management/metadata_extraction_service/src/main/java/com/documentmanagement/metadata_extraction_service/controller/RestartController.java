package com.documentmanagement.metadata_extraction_service.controller;

import com.documentmanagement.metadata_extraction_service.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:64203")
@RequestMapping("/metadata-service")
public class RestartController {

    @PostMapping("/restart")
    @CrossOrigin(origins = "http://localhost:64203")
    public void restart() {
        MetadataExtractionServiceApplication.restart();
    }

}
