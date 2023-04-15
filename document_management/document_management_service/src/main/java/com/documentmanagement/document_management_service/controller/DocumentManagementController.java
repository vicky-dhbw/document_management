package com.documentmanagement.document_management_service.controller;

import com.documentmanagement.document_management_service.downloadResource.FileDownloadUtil;
import com.documentmanagement.document_management_service.dto.DocumentMetadata;
import com.documentmanagement.document_management_service.dto.DocumentSummary;
import com.documentmanagement.document_management_service.dto.SearchRequest;
import com.documentmanagement.document_management_service.dto.UploadResponse;
import com.documentmanagement.document_management_service.service.DocumentManagementService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/document-management")
@CrossOrigin(origins="http://localhost:64203")
public class DocumentManagementController {

    private final DocumentManagementService documentManagementService;

    @CrossOrigin(origins="http://localhost:64203")
    @PostMapping(value = "/saveDocument",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UploadResponse> saveDocumentWithMetadata(@RequestPart("file") MultipartFile file,
                                                                   @RequestPart("metadata") String documentMetadata) throws IOException {

        return new ResponseEntity<>(documentManagementService.saveDocumentAndDocumentMetadata(file,documentMetadata), HttpStatus.OK);
    }
    @GetMapping("/test")
    public ResponseEntity<String> testing(@RequestBody DocumentMetadata documentMetadata){

        return null;
    }


    @GetMapping("/downloadFile/{filePath}")
    public ResponseEntity<?> downloadFile(@PathVariable("filePath") String filePath) throws IOException {
        FileDownloadUtil fileDownloadUtil=new FileDownloadUtil();
        Path path;
        Resource resource=null;
        try{
            resource=fileDownloadUtil.getFileAsResource(filePath);
            path=resource.getFile().toPath();
        } catch(IOException e){
            return ResponseEntity.internalServerError().build();
        }

        String contentType="application/octet-stream";
        String headerValue="attachment;filename=\""+resource.getFilename()+"\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }

    @GetMapping("/getDocumentSummaries")
    public ResponseEntity<List<DocumentSummary>> getDocumentSummaryList(){
        List<DocumentSummary> documentSummaryList=documentManagementService.getDocumentSummaries();
        return new ResponseEntity<>(documentSummaryList,HttpStatus.OK);
    }

    @PostMapping("/searchForKeyword")
    public ResponseEntity<List<DocumentSummary>> searchInDatabase(@RequestBody SearchRequest searchRequest){
        List<DocumentSummary> documentSummaryList=documentManagementService.searchForKeyword(searchRequest);
        return new ResponseEntity<>(documentSummaryList,HttpStatus.OK);
    }
}
