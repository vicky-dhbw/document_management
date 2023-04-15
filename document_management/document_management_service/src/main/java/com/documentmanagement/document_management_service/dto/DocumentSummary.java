package com.documentmanagement.document_management_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentSummary {
    private String title;
    private String filename;
    private double filesize;

}
