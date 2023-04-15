package com.documentmanagement.document_management_service.Configuration;

public enum Configuration {
    INSTANCE;

    public final String fileSeparator=System.getProperty("file.separator");
    public final String pathToAssetsDir="document_management_service"+fileSeparator+"src"+fileSeparator+"main"+fileSeparator+"assets";
    public final String urlForOfficeIndexingService="http://localhost:8091/api/office-indexer/extractFromDoc";
    public final String urlForPdfIndexingService="http://localhost:8092/api/pdf-indexer/extractFromDoc";
}
