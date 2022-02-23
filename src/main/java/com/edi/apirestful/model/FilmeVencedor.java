package com.edi.apirestful.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class FilmeVencedor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Filme filme;

    @Column(nullable = false)
    private String winner;

}
