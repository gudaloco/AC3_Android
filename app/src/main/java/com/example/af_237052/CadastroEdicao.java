package com.example.af_237052;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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
        Intent intent = new Intent(CadastroEdicao.this, MainActivity.class);
        startActivity(intent);
    }
    private void EditarRemedio(){

    }
}