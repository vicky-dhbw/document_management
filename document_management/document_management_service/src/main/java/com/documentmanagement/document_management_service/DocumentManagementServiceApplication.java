package com.documentmanagement.document_management_service;

import com.documentmanagement.document_management_service.service.DocumentManagementService;
import jakarta.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class DocumentManagementServiceApplication implements CommandLineRunner {
    @Resource
    DocumentManagementService documentManagementService;
    public static void main(String[] args) {
        SpringApplication.run(DocumentManagementServiceApplication.class,args);
    }

    @Override
    public void run(String[] args){
        documentManagementService.init();
    }

}
