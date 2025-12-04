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
    private Remedio EditandoRemedio = null;

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

        buttonSalvar = findViewById(R.id.buttonSalvar);
        buttonSalvar.setOnClickListener(v -> {
            SalvarEVoltar();
        });
        Intent intent = getIntent();
        if (intent.hasExtra("remedio_para_edicao")) {
            EditandoRemedio = (Remedio) intent.getSerializableExtra("remedio_para_edicao");

            if (EditandoRemedio != null) {
                edtNome.setText(EditandoRemedio.getNome());
                edtDescricao.setText(EditandoRemedio.getDescricao());
                edtTime.setText(String.valueOf(EditandoRemedio.getHorario()));
                checkBoxTomado.setChecked(EditandoRemedio.getCheck());

                buttonSalvar.setText(R.string.button_salvar);
            }
        }

        buttonSalvar = findViewById(R.id.buttonSalvar);
        buttonSalvar.setOnClickListener(v -> {
            SalvarEVoltar();
        });
    }

    private void SalvarEVoltar(){
        String nome = edtNome.getText().toString().trim();
        String descricao = edtDescricao.getText().toString().trim();
        String horarioStr = edtTime.getText().toString().trim();
        //boolean check = checkBoxTomado.isChecked();
        boolean checkTomado = checkBoxTomado.isChecked();
        boolean checkFinalizado = false;

        if (nome.isEmpty() || descricao.isEmpty() || horarioStr.isEmpty()) {
            Toast.makeText(this, R.string.campos_vazios, Toast.LENGTH_SHORT).show();
            return;
        }

        //int tempoRemedio = Integer.parseInt(tempoRemedioStr);

        if (EditandoRemedio == null) {
            Remedio remedio = new Remedio(null, nome, descricao, horarioStr, checkTomado, checkFinalizado);
            db.collection("remedios")
                    .add(remedio)
                    .addOnSuccessListener(doc -> {
                        remedio.setId(doc.getId());
                        Toast.makeText(this, R.string.remedio_salvo, Toast.LENGTH_SHORT).show();
                    });
        } else {
            EditandoRemedio.setNome(nome);
            EditandoRemedio.setDescricao(descricao);
            EditandoRemedio.setHorario(horarioStr);
            //EditandoRemedio.setCheck(check);
            EditandoRemedio.setCheck(checkTomado);
            EditandoRemedio.setFinalizado(checkFinalizado);

            db.collection("remedios").document(EditandoRemedio.getId())
                    .set(EditandoRemedio)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, R.string.remedio_atualizado, Toast.LENGTH_SHORT).show();
                        EditandoRemedio = null;
                        finish();
                    });
        }

        finish();
    }
}