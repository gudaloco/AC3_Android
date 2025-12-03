package com.example.af_237052;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText edtNome, edtDescricao, edtTime;
    private CheckBox checkBoxTomado;
    private Button buttonNovo;
    private RemedioAdapter adapter;
    private List<Remedio> listaRemedio = new ArrayList<>();
    private RecyclerView recyclerMedicamentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        edtNome = findViewById(R.id.edtNome);
        edtDescricao = findViewById(R.id.edtDescricao);
        edtTime = findViewById(R.id.edtTime);

        recyclerMedicamentos = findViewById(R.id.recyclerViewMedicamentos);
        recyclerMedicamentos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RemedioAdapter(listaRemedio);
        recyclerMedicamentos.setAdapter(adapter);
//        findViewById(R.id.buttonSalvar).setOnClickListener(v -> salvarRemedio());

        //carregarRemedios();
        checkBoxTomado = findViewById(R.id.checkBoxTomado);
        buttonNovo = findViewById(R.id.buttonNovo);
        buttonNovo.setOnClickListener(v -> {
            novoMedicamento();
        });
    }

    private void carregarRemedios(){
    }

    private void novoMedicamento(){
        Intent intent = new Intent(MainActivity.this, CadastroEdicao.class);

        startActivity(intent);
    }


}