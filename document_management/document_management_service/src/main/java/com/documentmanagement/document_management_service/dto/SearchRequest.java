package com.documentmanagement.document_management_service.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    private String searchKeyword;
    private String textOrTag;
}
