package com.example.diploma1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class IncomingFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String filename;
    @Column
    private long size;
    @Column
    private String key;
    @Column
    private LocalDate uploadDate;

    public IncomingFile(String filename, int size) {
        this.filename = filename;
        this.size = size;
    }
}

