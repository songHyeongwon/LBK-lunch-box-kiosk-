package com.example.lunchboxkiosk.model.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

    private String id;
    private String name;
    private String parentId;
}
