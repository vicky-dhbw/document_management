package com.documentmanagement.document_management_service.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String filename;
    private String creator;
    private Date dateOfCreation;
    private double filesize;
    @Lob
    @Column(length = 999999999)
    private String text;

    private String locationUrl;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "document_tag",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

}
