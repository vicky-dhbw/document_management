package com.documentmanagement.document_management_service.service;

import com.documentmanagement.document_management_service.*;
import com.documentmanagement.document_management_service.Configuration.Configuration;
import com.documentmanagement.document_management_service.dto.DocumentMetadata;
import com.documentmanagement.document_management_service.dto.DocumentSummary;
import com.documentmanagement.document_management_service.dto.SearchRequest;
import com.documentmanagement.document_management_service.dto.UploadResponse;
import com.documentmanagement.document_management_service.model.Document;
import com.documentmanagement.document_management_service.model.Tag;
import com.documentmanagement.document_management_service.repository.DocumentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentManagementService {
    private final Path root = Paths.get(Configuration.INSTANCE.pathToAssetsDir);
    private final DocumentRepository documentRepository;
    public void init() {
        System.out.println(root);
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    public DocumentMetadata getDocumentMetadata(String metadata){
        DocumentMetadata documentMetadata=new DocumentMetadata();
        try{
            ObjectMapper objectMapper=new ObjectMapper();
            documentMetadata=objectMapper.readValue(metadata, DocumentMetadata.class);
        }
        catch (IOException err){
            System.out.println("Error"+ err.toString());
        }
        return documentMetadata;
    }

    public UploadResponse saveDocumentAndDocumentMetadata(MultipartFile file, String metadata) {

        File directory = new File(root.toUri());
        File[] files = directory.listFiles();
        ResponseEntity<String> extractedText = null;

        boolean fileExists = false;
        assert files != null;
        for (File file_ : files) {
            if (file_.getName().equals(file.getOriginalFilename())) {
                System.out.println(file_.getName());
                fileExists = true;
                break;
            }
        }

        UploadResponse uploadResponse = null;
        if (!fileExists) {
            try {
                Path destinationPath = this.root.resolve(file.getOriginalFilename());
                long bytesCopied = Files.copy(file.getInputStream(), destinationPath);
                File copiedFile = destinationPath.toFile();

                extractedText = requestText(copiedFile);

            } catch (Exception e) {
                if (e instanceof FileAlreadyExistsException) {
                    throw new RuntimeException("A file of that name already exists.");
                }
            }

            DocumentMetadata documentMetadata = new DocumentMetadata();

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                documentMetadata = objectMapper.readValue(metadata, DocumentMetadata.class);
                SimpleDateFormat sqlDatetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date javaDate = new Date();
                try {
                    javaDate = sqlDatetimeFormat.parse(documentMetadata.getDateOfCreation());
                    System.out.println("Date of creation: " + javaDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List<Tag> documentTags = new ArrayList<>();

                assert extractedText != null;
                Document document = Document.builder()
                        .title(documentMetadata.getTitle())
                        .filename(documentMetadata.getFilename())
                        .creator(documentMetadata.getCreator())
                        .dateOfCreation(javaDate)
                        .filesize(documentMetadata.getFilesize())
                        .locationUrl(Configuration.INSTANCE.pathToAssetsDir + Configuration.INSTANCE.fileSeparator + file.getOriginalFilename())
                        .text(extractedText.getBody())
                        .build();

                for (String tagName : documentMetadata.getTags()) {
                    Tag documentTag = new Tag();
                    documentTag.setTagName(tagName);
                    documentTag.setDocument(document);
                    documentTags.add(documentTag);
                }

                document.setTags(documentTags);

                uploadResponse = new UploadResponse(document.getFilename(), document.getFilesize());

                documentRepository.save(document);

            } catch (IOException err) {
                System.out.println("Error" + err);
            }

        }else{
            return new UploadResponse("error",0);
        }
        return uploadResponse;

    }


    public ResponseEntity<String> requestText(File file){

       int index= file.getName().lastIndexOf(".");
       String fileExtension="";

       if(index>0){
           fileExtension=file.getName().substring(index+1);
       }

       String serverUrlForTextExtraction=fileExtension.equals("docx")? Configuration.INSTANCE.urlForOfficeIndexingService:
               Configuration.INSTANCE.urlForPdfIndexingService;

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", new FileSystemResource(file));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

        return restTemplate.exchange(
                serverUrlForTextExtraction,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

    }

    public List<DocumentSummary> getDocumentSummaries(){
        List<DocumentRepository.DocumentSummary> documentSummaries=documentRepository.findAllBy();

        return documentSummaries.stream()
                .map(documentSummary->
                        DocumentSummary.builder()
                                .title(documentSummary.getTitle())
                                .filesize(documentSummary.getFilesize())
                                .filename(documentSummary.getFilename())
                                .build()
                ).toList();


    }

    public List<DocumentSummary> searchForKeyword(SearchRequest searchRequest){

        String searchKeyword= searchRequest.getSearchKeyword();
        List<DocumentSummary> documentSummaries = new ArrayList<>();

        if((searchRequest.getTextOrTag().equals("text"))){
            List<Object[]> result = documentRepository.searchByText(searchKeyword);


            for (Object[] obj : result) {
                DocumentSummary documentSummary = DocumentSummary.builder()
                        .filename((String) obj[0])
                        .filesize((Double) obj[1])
                        .title((String) obj[2])
                        .build();
                documentSummaries.add(documentSummary);
            }
        }
        if((searchRequest.getTextOrTag().equals("tag"))){
            List<Object[]> result = documentRepository.findByTagName(searchKeyword);

            for (Object[] obj : result) {
                DocumentSummary documentSummary = DocumentSummary.builder()
                        .filename((String) obj[0])
                        .filesize((Double) obj[1])
                        .title((String) obj[2])
                        .build();
                documentSummaries.add(documentSummary);
            }
        }



        return documentSummaries;
    }

}
