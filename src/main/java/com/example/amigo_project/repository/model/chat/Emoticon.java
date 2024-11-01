package com.example.amigo_project.repository.model.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Emoticon {

    private Integer id;
    private String url;
    private String name;

}
