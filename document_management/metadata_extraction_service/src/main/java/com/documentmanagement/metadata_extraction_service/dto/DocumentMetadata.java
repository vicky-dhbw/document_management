package com.documentmanagement.metadata_extraction_service.dto;

import lombok.*;

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


}
