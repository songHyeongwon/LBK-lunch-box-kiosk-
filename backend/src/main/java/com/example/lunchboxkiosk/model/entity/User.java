package com.example.lunchboxkiosk.model.entity;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String username;
    private String phoneNumber;
}
