package com.example.af_237052;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;

public class CadastroEdicao extends AppCompatActivity {

    private FirebaseFirestore db;
    private Button buttonSalvar;
    private EditText edtNome, edtDescricao, edtTime;
    private CheckBox checkBoxTomado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro_edicao);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        edtNome = findViewById(R.id.edtNome);
        edtDescricao = findViewById(R.id.edtDescricao);
        edtTime = findViewById(R.id.edtTime);
        checkBoxTomado = findViewById((R.id.checkBoxTomado));

        //recyclerMedicamentos = findViewById(R.id.recyclerViewMedicamentos);
        //recyclerMedicamentos.setLayoutManager(new LinearLayoutManager(this));
       // adapter = new RemedioAdapter(listaRemedio);
        //.setAdapter(adapter);

        //carregarRemedios();
        //checkBoxTomado = findViewById(R.id.checkBoxTomado);
        //buttonNovo = findViewById(R.id.buttonNovo);

        //SalvarEVoltar();
        buttonSalvar = findViewById(R.id.buttonSalvar);
        buttonSalvar.setOnClickListener(v -> {
            SalvarEVoltar();
        });
    }

    private void SalvarEVoltar(){
        String nome = edtNome.getText().toString().trim();
        String descricao = edtDescricao.getText().toString().trim();
        String tempoRemedioStr = edtTime.getText().toString().trim();
        boolean check = checkBoxTomado.isChecked();

        if (nome.isEmpty() || descricao.isEmpty() || tempoRemedioStr.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        int tempoRemedio = Integer.parseInt(tempoRemedioStr);

        if (EditarRemedio(); == null) {
            Remedio remedio = new Remedio(null, nome, descricao, tempoRemedio, check);
            db.collection("remedios")
                    .add(remedio)
                    .addOnSuccessListener(doc -> {
                        remedio.setId(doc.getId());
                        Toast.makeText(this, "Remedio salvo!", Toast.LENGTH_SHORT).show();
                        carregarRemedios();
                    });
        } else {
            filmeEditando.setNome(nome);
            filmeEditando.setDescricao(descricao);
            filmeEditando.setHorario(tempoRemedio);
            filmeEditando.setCheck(check);

            db.collection("remedios").document(filmeEditando.getId())
                    .set(filmeEditando)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Remedio atualizado!", Toast.LENGTH_SHORT).show();
                        limparCampos();
                        carregarFilmes();
                        filmeEditando = null;
                    });
        }

        Intent intent = new Intent(CadastroEdicao.this, MainActivity.class);
        startActivity(intent);
    }
    private void EditarRemedio(){

    }
}