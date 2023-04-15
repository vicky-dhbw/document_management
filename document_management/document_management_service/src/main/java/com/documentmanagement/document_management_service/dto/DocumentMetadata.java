package com.documentmanagement.document_management_service.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class DocumentMetadata {
    private String title;
    private String filename;
    private String creator;
    private String dateOfCreation;
    private double filesize;
    private List<String> tags;
    ///


}
