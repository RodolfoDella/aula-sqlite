package br.com.wedosoft.sqlite;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import br.com.wedosoft.sqlite.R;

public class PessoaNewActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = Application.class.getSimpleName();
    private Context context;

    private PessoaModel mPessoa;
    private PessoaRepositorio mRepositorio;

    public static final String EXTRA_PESSOA = "pessoa";

    RadioGroup rg_sexo;

    EditText ed_nome;
    EditText ed_dataNascimento;
    EditText ed_cpf;
    EditText ed_telefone;
    EditText ed_email;

    RadioButton radioMasculino;
    RadioButton radioFeminino;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessoa_new);

        Intent intent = getIntent();
        mPessoa = (PessoaModel) intent.getSerializableExtra("pessoa");

        context = this;
        mRepositorio = new PessoaRepositorio(this);

        ed_nome = (EditText) findViewById(R.id.editTextNome);
        ed_dataNascimento = (EditText) findViewById(R.id.editTextDataNascimento);
        ed_cpf = (EditText) findViewById(R.id.editTextCpf);
        ed_telefone = (EditText) findViewById(R.id.editTextTelefone);
        ed_email = (EditText) findViewById(R.id.editTextEmail);

        rg_sexo = (RadioGroup) findViewById(R.id.radioGroupSexo);
        radioMasculino = (RadioButton) findViewById(R.id.radioMasculino);
        radioFeminino = (RadioButton) findViewById(R.id.radioFeminino);

        Button salvar = (Button) findViewById(R.id.buttonSalvar);
        Button excluir = (Button) findViewById(R.id.buttonExcluir);

        salvar.setOnClickListener(this);

        if (mPessoa != null) {
            salvar.setText("Atualizar");
            excluir.setOnClickListener(this);
            carregarInformacoesDoObjetoNaTela();
        } else {
            excluir.setVisibility(View.GONE);
        }

        setMaskData();
        setMaskCpf();
        setMaskCelPhone();
    }

    protected void carregarInformacoesDoObjetoNaTela() {
        ed_nome.setText(mPessoa.nome);
        ed_dataNascimento.setText(mPessoa.dataNascimento);
        ed_cpf.setText(mPessoa.cpf);
        ed_telefone.setText(mPessoa.telefone);
        ed_email.setText(mPessoa.email);
        if (mPessoa.sexo.equals("M")) {
            radioMasculino.setChecked(true);
        } else {
            radioFeminino.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSalvar:
                salvarPessoa(view);
                break;
            case R.id.buttonExcluir:
                excluirPessoa(view);
                break;
            default:
                String str = "Não foi encontrado o botão \"" +
                        ((Button) view).getText().toString() + "\" na tela \"" +
                        String.valueOf(this.getTitle()) + "\"";
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                break;
        }

    }

    public void salvarPessoa(View view) {
        PessoaModel pessoa;
        String sexo;
        String msg;


        if (radioMasculino.isChecked()) {
            sexo = "M";
        } else {
            sexo = "F";
        }

        if (mPessoa == null) {
            msg = "salva";
            pessoa = new PessoaModel(ed_nome.getText().toString(),
                    sexo,
                    ed_dataNascimento.getText().toString(),
                    ed_cpf.getText().toString(),
                    ed_telefone.getText().toString(),
                    ed_email.getText().toString());
        } else {
            msg = "atualizada";
            pessoa = new PessoaModel(mPessoa.id, ed_nome.getText().toString(),
                    sexo,
                    ed_dataNascimento.getText().toString(),
                    ed_cpf.getText().toString(),
                    ed_telefone.getText().toString(),
                    ed_email.getText().toString());
        }

        mRepositorio.salvar(pessoa);
        Toast.makeText(this,
                "Pessoa "+msg+" com sucesso!",
                Toast.LENGTH_SHORT).show();

        finish();
    }

    public void excluirPessoa(View view) {
        mRepositorio.excluir(mPessoa);
        Toast.makeText(this,
                "Pessoa excluída com sucesso!",
                Toast.LENGTH_SHORT).show();

        finish();
    }

    private void setMaskData() {
        ed_dataNascimento.addTextChangedListener(new TextWatcher() {
            boolean isUpdating;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int after) {
                // Quando o texto é alterado o onTextChange é chamado
                // Essa flag evita a chamada infinita desse método
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }
                // Ao apagar o texto, a máscara é removida,
                // então o posicionamento do cursor precisa
                // saber se o texto atual tinha ou não, máscara
                boolean hasMask = s.toString().indexOf('/') > -1;

                // Remove o '.' e '-' da String
                String str = s.toString().replaceAll("[/]", "");

                // os parâmetros before e after dizem o tamanho
                // anterior e atual da String digitada, se after > before é
                // porque está digitando, caso contrário, está apagando
                if (after > before) {
                    // Se tem menos de 10 caracteres (sem máscara)
                    // coloca a '/'
                    if (str.length() < 10) {
                        if (str.length() > 8) {
                            str = str.substring(0, 2) + '/'
                                    + str.substring(2, 4) + '/'
                                    + str.substring(4, 8);

                        } else if (str.length() > 4) {
                            str = str.substring(0, 2) + '/'
                                    + str.substring(2, 4) + '/'
                                    + str.substring(4);

                        } else if (str.length() > 2) {
                            str = str.substring(0, 2) + '/'
                                    + str.substring(2);
                        }
                    }

                    // Seta a flag para evitar chamada infinita
                    isUpdating = true;
                    // seta o novo texto
                    ed_dataNascimento.setText(str);
                    // seta a posição do cursor
                    ed_dataNascimento.setSelection(ed_dataNascimento.getText().length());
                } else {
                    isUpdating = true;
                    ed_dataNascimento.setText(str);

                    // Se estiver apagando posiciona o cursor
                    // no local correto. Isso trata a deleção dos
                    // caracteres da máscara.
                    ed_dataNascimento.setSelection(Math.max(0, Math.min(hasMask ?
                            start - before : start, str.length() ) ) );
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void setMaskCpf() {
        ed_cpf.addTextChangedListener(new TextWatcher() {
            boolean isUpdating;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int after) {
                // Quando o texto é alterado o onTextChange é chamado
                // Essa flag evita a chamada infinita desse método
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }
                // Ao apagar o texto, a máscara é removida,
                // então o posicionamento do cursor precisa
                // saber se o texto atual tinha ou não, máscara
                boolean hasMask = s.toString().indexOf('.') > -1 ||
                                  s.toString().indexOf('-') > -1;

                // Remove o '.' e '-' da String
                String str = s.toString().replaceAll("[.]", "")
                                         .replaceAll("[-]", "");

                // os parâmetros before e after dizem o tamanho
                // anterior e atual da String digitada, se after > before é
                // porque está digitando, caso contrário, está apagando
                if (after > before) {
                    // Se tem menos de 13 caracteres (sem máscara)
                    // coloca o '.' e o '-'
                    if (str.length() < 13) {
                        if (str.length() > 11) {
                            str = str.substring(0, 3) + '.'
                                    + str.substring(3, 6) + '.'
                                    + str.substring(6, 9) + '-'
                                    + str.substring(9, 11);

                        } else if (str.length() > 9) {
                            str = str.substring(0, 3) + '.'
                                    + str.substring(3, 6) + '.'
                                    + str.substring(6, 9) + '-'
                                    + str.substring(9);

                        } else if (str.length() > 6) {
                            str = str.substring(0, 3) + '.'
                                    + str.substring(3, 6) + '.'
                                    + str.substring(6);

                        } else if (str.length() > 3) {
                            str = str.substring(0, 3) + '.'
                                    + str.substring(3);
                        }
                    }

                    // Seta a flag para evitar chamada infinita
                    isUpdating = true;
                    // seta o novo texto
                    ed_cpf.setText(str);
                    // seta a posição do cursor
                    ed_cpf.setSelection(ed_cpf.getText().length());
                } else {
                    isUpdating = true;
                    ed_cpf.setText(str);

                    // Se estiver apagando posiciona o cursor
                    // no local correto. Isso trata a deleção dos
                    // caracteres da máscara.
                    ed_cpf.setSelection(Math.max(0, Math.min(hasMask ?
                            start - before : start, str.length() ) ) );
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void setMaskCelPhone() {
        ed_telefone.addTextChangedListener(new TextWatcher() {
            boolean isUpdating;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int after) {
                // Quando o texto é alterado o onTextChange é chamado
                // Essa flag evita a chamada infinita desse método
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }
                // Ao apagar o texto, a máscara é removida,
                // então o posicionamento do cursor precisa
                // saber se o texto atual tinha ou não, máscara
                boolean hasMask = s.toString().indexOf('(') > -1 ||
                                  s.toString().indexOf(')') > -1 ||
                                  s.toString().indexOf(' ') > -1 ||
                                  s.toString().indexOf('-') > -1;

                // Remove o '(', ')', ' ' e '-' da String
                String str = s.toString() .replaceAll("[(]", "")
                                          .replaceAll("[)]", "")
                                          .replaceAll(" ", "")
                                          .replaceAll("[-]", "");

                // os parâmetros before e after dizem o tamanho
                // anterior e atual da String digitada, se after > before é
                // porque está digitando, caso contrário, está apagando
                if (after > before) {
                    // Se tem menos de 13 caracteres (sem máscara)
                    // coloca o '(', ')', ' ' e '-'
                    if (str.length() < 13) {
                        if (str.length() > 11) {
                            str = '(' + str.substring(0, 1)
                                    + str.substring(1, 2) + ") "
                                    + str.substring(2, 7) + '-'
                                    + str.substring(7, 11);

                        } else if (str.length() > 7) {
                            str = '(' + str.substring(0, 1)
                                    + str.substring(1, 2) + ") "
                                    + str.substring(2, 7) + '-'
                                    + str.substring(7);

                        } else if (str.length() > 2) {
                            str = '(' + str.substring(0, 1)
                                    + str.substring(1, 2) + ") "
                                    + str.substring(2);

                        } else if (str.length() > 1) {
                            str = '(' + str.substring(0, 1)
                                    + str.substring(1);
                        }
                    }

                    // Seta a flag para evitar chamada infinita
                    isUpdating = true;
                    // seta o novo texto
                    ed_telefone.setText(str);
                    // seta a posição do cursor
                    ed_telefone.setSelection(ed_telefone.getText().length());
                } else {
                    isUpdating = true;
                    ed_telefone.setText(str);

                    // Se estiver apagando posiciona o cursor
                    // no local correto. Isso trata a deleção dos
                    // caracteres da máscara.
                    ed_telefone.setSelection(Math.max(0, Math.min(hasMask ?
                            start - before : start, str.length() ) ) );
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}
