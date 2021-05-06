package com.example.diploma1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String username;
    @Column
    private String password;
    @OneToMany(cascade= CascadeType.ALL)
    private List<IncomingFile> files;
}
