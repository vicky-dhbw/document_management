package com.documentmanagement.pdf_indexing_service.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PdfIndexingService {

    public ResponseEntity<String> extractTextFromPdf(File file) throws IOException {

        String pdfFileInText= "";
        HttpHeaders headers = new HttpHeaders();

        try (PDDocument document = PDDocument.load(file)) {

            if (!document.isEncrypted()) {
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);

                PDFTextStripper tStripper = new PDFTextStripper();

                try {
                    pdfFileInText = tStripper.getText(document);
                } catch (IOException e) {
                    // handle the exception here
                }

                headers.setContentType(MediaType.TEXT_PLAIN);

            }
        }
        return new ResponseEntity<>(pdfFileInText, headers, HttpStatus.OK);
    }
}
