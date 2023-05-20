package com.ngtu.api.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("template")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Template {
    @Id
    private String id;
    private String test;
}
