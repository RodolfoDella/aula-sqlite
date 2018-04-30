package br.com.wedosoft.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodolfodellagiustina on 22/04/2018.
 */

public class PessoaRepositorio {
    private PessoaSQLHelper helper;

    public PessoaRepositorio(Context ctx) {
        helper = new PessoaSQLHelper(ctx);
    }

    private long inserir(PessoaModel pessoa) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PessoaSQLHelper.COLUNA_PESSOA_NOME, pessoa.nome);
        cv.put(PessoaSQLHelper.COLUNA_PESSOA_SEXO, pessoa.sexo);
        cv.put(PessoaSQLHelper.COLUNA_PESSOA_DATA_NASCIMENTO, pessoa.dataNascimento);
        cv.put(PessoaSQLHelper.COLUNA_PESSOA_CPF, pessoa.cpf);
        cv.put(PessoaSQLHelper.COLUNA_PESSOA_TELEFONE, pessoa.telefone);
        cv.put(PessoaSQLHelper.COLUNA_PESSOA_EMAIL, pessoa.email);

        long id = db.insert(PessoaSQLHelper.TABELA_PESSOA, null, cv);
        if (id != -1) {
            pessoa.id = id;
        }
        db.close();
        return id;
    }

    private int atualizar(PessoaModel pessoa) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PessoaSQLHelper.COLUNA_PESSOA_NOME, pessoa.nome);
        cv.put(PessoaSQLHelper.COLUNA_PESSOA_SEXO, pessoa.sexo);
        cv.put(PessoaSQLHelper.COLUNA_PESSOA_DATA_NASCIMENTO, pessoa.dataNascimento);
        cv.put(PessoaSQLHelper.COLUNA_PESSOA_CPF, pessoa.cpf);
        cv.put(PessoaSQLHelper.COLUNA_PESSOA_TELEFONE, pessoa.telefone);
        cv.put(PessoaSQLHelper.COLUNA_PESSOA_EMAIL, pessoa.email);

        int linhasAfetadas = db.update(
                PessoaSQLHelper.TABELA_PESSOA,
                cv,
                PessoaSQLHelper.COLUNA_PESSOA_ID +" = ?",
                new String[]{ String.valueOf(pessoa.id)});
        db.close();
        return linhasAfetadas;
    }

    public void salvar(PessoaModel pessoa) {
        if (pessoa.id == 0) {
            inserir(pessoa);
        } else {
            atualizar(pessoa);
        }
    }

    public int excluir(PessoaModel pessoa) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int linhasAfetadas = db.delete(
                PessoaSQLHelper.TABELA_PESSOA,
                PessoaSQLHelper.COLUNA_PESSOA_ID +" = ?",
                new String[]{ String.valueOf(pessoa.id)});
        db.close();
        return linhasAfetadas;
    }

    public List<PessoaModel> buscarPessoa(String filtro) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM "+ PessoaSQLHelper.TABELA_PESSOA;
        String[] argumentos = null;
        if (filtro != null) {
            sql += " WHERE "+ PessoaSQLHelper.COLUNA_PESSOA_NOME +" LIKE ?";
            argumentos = new String[]{ filtro };
        }
        sql += " ORDER BY "+ PessoaSQLHelper.COLUNA_PESSOA_NOME;
        Cursor cursor = db.rawQuery(sql, argumentos);
        List<PessoaModel> pessoas = new ArrayList<PessoaModel>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(
                    cursor.getColumnIndex(
                            PessoaSQLHelper.COLUNA_PESSOA_ID));
            String nome = cursor.getString(
                    cursor.getColumnIndex(
                            PessoaSQLHelper.COLUNA_PESSOA_NOME));
            String sexo = cursor.getString(
                    cursor.getColumnIndex(
                            PessoaSQLHelper.COLUNA_PESSOA_SEXO));
            String dataNascimento = cursor.getString(
                    cursor.getColumnIndex(
                            PessoaSQLHelper.COLUNA_PESSOA_DATA_NASCIMENTO));
            String cpf = cursor.getString(
                    cursor.getColumnIndex(
                            PessoaSQLHelper.COLUNA_PESSOA_CPF));
            String telefone = cursor.getString(
                    cursor.getColumnIndex(
                            PessoaSQLHelper.COLUNA_PESSOA_TELEFONE));
            String email = cursor.getString(
                    cursor.getColumnIndex(
                            PessoaSQLHelper.COLUNA_PESSOA_EMAIL));

            PessoaModel pessoa = new PessoaModel(id, nome, sexo, dataNascimento, cpf, telefone, email);
            pessoas.add(pessoa);
        }
        cursor.close();
        db.close();
        return pessoas;
    }
}
