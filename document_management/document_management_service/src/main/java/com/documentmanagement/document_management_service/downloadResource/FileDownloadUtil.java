package com.documentmanagement.document_management_service.downloadResource;
import com.documentmanagement.document_management_service.Configuration.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDownloadUtil {
    private Path foundFile;

    public Resource getFileAsResource(String filePath) throws IOException {
        Path uploadDirectory = Paths.get(Configuration.INSTANCE.pathToAssetsDir);

        Files.list(uploadDirectory).forEach(file->{
            if(file.getFileName().toString().startsWith(filePath)){
                foundFile=file;
            }
        });

        if(foundFile!=null){
            return new UrlResource(foundFile.toUri());
        }
        return null;
    }
}
