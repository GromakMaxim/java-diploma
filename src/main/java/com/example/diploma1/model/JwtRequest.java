package com.example.diploma1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest implements Serializable {
    //private static final long serialVersionUID = 5926468583005150707L;
    private String login;
    private String password;

}