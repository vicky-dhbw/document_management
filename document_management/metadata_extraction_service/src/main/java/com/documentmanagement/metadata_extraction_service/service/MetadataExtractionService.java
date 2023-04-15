package com.documentmanagement.metadata_extraction_service.service;
import com.documentmanagement.metadata_extraction_service.dto.DocumentMetadata;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class MetadataExtractionService {

    public DocumentMetadata extractMetadata(File file) throws IOException, TikaException, SAXException, ParseException {
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();

        try (InputStream stream =new FileInputStream(file)) {
            parser.parse(stream, handler, metadata);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date createdDate = metadata.get("dcterms:created") != null ? sdf.parse(metadata.get("dcterms:created")) : null;
        SimpleDateFormat sqlSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sqlDateStr = createdDate != null ? sqlSdf.format(createdDate) : "";

        return DocumentMetadata.builder()
                .title(metadata.get("dc:title")!=null ? metadata.get("dc:title") : "")
                .filename(file.getName())
                .creator(metadata.get("dc:creator")!=null ? metadata.get("dc:creator") :"" )
                .dateOfCreation(sqlDateStr)
                .filesize(Math.round(((double)file.length() / 1024) * 100.0) / 100.0)
                .build();
    }

}
