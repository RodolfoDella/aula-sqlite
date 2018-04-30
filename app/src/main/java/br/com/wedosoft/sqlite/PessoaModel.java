package br.com.wedosoft.sqlite;

import java.io.Serializable;

/**
 * Created by rodolfodellagiustina on 22/04/2018.
 */

public class PessoaModel implements Serializable {
    public long id;
    public String nome;
    public String sexo;
    public String dataNascimento;
    public String cpf;
    public String telefone;
    public String email;

    public PessoaModel(long id, String nome, String sexo, String dataNascimento, String cpf, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
    }

    public PessoaModel(String nome, String sexo, String dataNascimento, String cpf, String telefone, String email) {
        this(0, nome, sexo, dataNascimento, cpf, telefone, email);
    }

    @Override
    public String toString() {
        return nome;
    }
}
