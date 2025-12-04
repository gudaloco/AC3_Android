package com.example.af_237052;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText tNome, tDescricao, tTime;
    //private CheckBox checkBoxTomado;
    private Button buttonNovo;
    private RemedioAdapter adapter;
    private List<Remedio> listaRemedio = new ArrayList<>();
    private RecyclerView recyclerMedicamentos;
    private static final int PERMISSION_REQUEST_CODE = 1001;

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
        
        solicitarPermissaoNotificacao();

        db = FirebaseFirestore.getInstance();
        //tNome = findViewById(R.id.edtNome);
        //tDescricao = findViewById(R.id.edtDescricao);
       // tTime = findViewById(R.id.edtTime);

        recyclerMedicamentos = findViewById(R.id.recyclerViewMedicamentos);
        recyclerMedicamentos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RemedioAdapter(listaRemedio);
        recyclerMedicamentos.setAdapter(adapter);

        //checkBoxTomado = findViewById(R.id.checkBoxTomado);
        buttonNovo = findViewById(R.id.buttonNovo);
        buttonNovo.setOnClickListener(v -> {
            novoMedicamento();
        });
        adapter.setOnItemLongClickListener((remedio, position) -> {
            excluirRemedio(remedio, position);

        });
    }
    private void solicitarPermissaoNotificacao() {
        // Verifica se a versão do Android é 13 (Tiramisu) ou superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                // Se a permissão não foi concedida, solicita ao usuário
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida!
                Toast.makeText(this, "Permissão de notificação concedida.", Toast.LENGTH_SHORT).show();
            } else {
                // Permissão negada. O usuário não receberá notificações.
                Toast.makeText(this, "Permissão de notificação negada. Lembretes não funcionarão.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarRemedios();
    }

    private void carregarRemedios() {
        db.collection("remedios")
                .get()
                .addOnSuccessListener(query -> {
                    listaRemedio.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        Remedio f = doc.toObject(Remedio.class);
                        f.setId(doc.getId());
                        listaRemedio.add(f);
                    }
                    adapter.notifyDataSetChanged();
                });
        adapter.setOnItemClickListener(remedio -> {
            Intent intent = new Intent(this, CadastroEdicao.class);
            intent.putExtra("remedio_para_edicao", remedio);
            startActivity(intent);
        });

    }

    private void novoMedicamento(){
        Intent intent = new Intent(MainActivity.this, CadastroEdicao.class);
        startActivity(intent);
    }

    private void excluirRemedio(Remedio remedio, int position) {
        cancelarAlarme(remedio);
        db.collection("remedios").document(remedio.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    listaRemedio.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(this, R.string.excluir_remedio, Toast.LENGTH_SHORT).show();
                });

    }
    /**
     * Cancela o alarme de um remédio existente.
     */
    private void cancelarAlarme(Remedio remedio) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);

        // O requestCode deve ser o mesmo usado no agendamento
        int requestCode = remedio.getId().hashCode();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
    }



}