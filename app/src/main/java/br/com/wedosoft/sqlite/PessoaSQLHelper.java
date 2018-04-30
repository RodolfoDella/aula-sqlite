package br.com.wedosoft.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class PessoaSQLHelper extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "dbPessoa";
    private static final int VERSAO_BANCO = 1;

    public static final String TABELA_PESSOA = "pessoa";
    public static final String COLUNA_PESSOA_ID = "_id";
    public static final String COLUNA_PESSOA_NOME = "nm_nome";
    public static final String COLUNA_PESSOA_SEXO = "ds_sexo";
    public static final String COLUNA_PESSOA_DATA_NASCIMENTO = "dt_nascimento";
    public static final String COLUNA_PESSOA_CPF = "ds_cpf";
    public static final String COLUNA_PESSOA_TELEFONE = "nr_telefone";
    public static final String COLUNA_PESSOA_EMAIL = "ds_email";

    public PessoaSQLHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE "+ TABELA_PESSOA +" (" +
                        COLUNA_PESSOA_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        COLUNA_PESSOA_NOME     +" TEXT NOT NULL, "+
                        COLUNA_PESSOA_SEXO +" TEXT, " +
                        COLUNA_PESSOA_DATA_NASCIMENTO +" NUMERIC, " +
                        COLUNA_PESSOA_CPF +" TEXT, " +
                        COLUNA_PESSOA_TELEFONE +" TEXT, " +
                        COLUNA_PESSOA_EMAIL +" TEXT );"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // para as próximas versões
    }
}
