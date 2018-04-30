package br.com.wedosoft.sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import br.com.wedosoft.sqlite.R;

public class PessoaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView mListView;

    List<PessoaModel> mPessoas;
    ArrayAdapter<PessoaModel> mAdapter;
    PessoaRepositorio mRepositorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessoa);

        mRepositorio = new PessoaRepositorio(this);

        mListView = findViewById(R.id.listPessoa);
        mListView.setOnItemClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionarPessoaClick();
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        buscar("");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        PessoaModel pessoa = (PessoaModel) adapterView.getItemAtPosition(i);
        editarPessoa(pessoa);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void adicionarPessoaClick(){
        Intent intent = new Intent(PessoaActivity.this, PessoaNewActivity.class);
        startActivity(intent);
    }

    public void editarPessoa(PessoaModel pessoa) {
        Intent it = new Intent(this, PessoaNewActivity.class);
        it.putExtra(PessoaNewActivity.EXTRA_PESSOA, pessoa);
        startActivity(it);
    }

    public void buscar(String s) {
        List<PessoaModel> pessoasEncontradas = mRepositorio.buscarPessoa(null);

        mAdapter = new ArrayAdapter<PessoaModel>(
                this,
                android.R.layout.simple_list_item_1,
                pessoasEncontradas);
        mListView.setAdapter(mAdapter);
    }

}
