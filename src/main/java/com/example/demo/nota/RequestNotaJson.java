package com.example.demo.nota;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
public class RequestNotaJson {
    private String id;
    private Double nota;
}
