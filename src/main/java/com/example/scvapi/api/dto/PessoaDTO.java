package com.example.scvapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class PessoaDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private int cep;
    private int numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
}