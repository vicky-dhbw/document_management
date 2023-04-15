package com.documentmanagement.office_indexing_service.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class OfficeIndexingService {
    public ResponseEntity<String> extractTextFromDocument(File file) throws IOException, InvalidFormatException {
        FileInputStream fis = new FileInputStream(file);
        // open file
        XWPFDocument file_  = new XWPFDocument(OPCPackage.open(fis));
        // read text
        XWPFWordExtractor ext = new XWPFWordExtractor(file_);

        String largeText = ext.getText();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>(largeText, headers, HttpStatus.OK);
    }

}
