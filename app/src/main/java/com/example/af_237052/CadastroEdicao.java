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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.icu.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        boolean checkTomado = checkBoxTomado.isChecked();
        boolean checkFinalizado = false;

        if (nome.isEmpty() || descricao.isEmpty() || horarioStr.isEmpty()) {
            Toast.makeText(this, R.string.campos_vazios, Toast.LENGTH_SHORT).show();
            return;
        }

        long tempoAlarmeMs = obterTempoAlarme(horarioStr);
        if (tempoAlarmeMs == -1) {
            Toast.makeText(this, "Formato de horário inválido. Use HH:mm (ex: 10:30)", Toast.LENGTH_LONG).show();
            return;
        }

        if (EditandoRemedio == null) {
            Remedio remedio = new Remedio(null, nome, descricao, horarioStr, checkTomado, checkFinalizado);
            db.collection("remedios")
                    .add(remedio)
                    .addOnSuccessListener(doc -> {
                        remedio.setId(doc.getId());
                        agendarAlarme(remedio, tempoAlarmeMs);
                        Toast.makeText(this, R.string.remedio_salvo, Toast.LENGTH_SHORT).show();
                        finish();
                    });
        } else {

            cancelarAlarme(EditandoRemedio);

            EditandoRemedio.setNome(nome);
            EditandoRemedio.setDescricao(descricao);
            EditandoRemedio.setHorario(horarioStr);
            EditandoRemedio.setCheck(checkTomado);
            EditandoRemedio.setFinalizado(checkFinalizado);

            db.collection("remedios").document(EditandoRemedio.getId())
                    .set(EditandoRemedio)
                    .addOnSuccessListener(aVoid -> {
                        agendarAlarme(EditandoRemedio, tempoAlarmeMs);
                        Toast.makeText(this, R.string.remedio_atualizado, Toast.LENGTH_SHORT).show();
                        EditandoRemedio = null;
                        finish();
                    });
        }


    }

    private long obterTempoAlarme(String horarioStr) {
        try {
            String[] partes = horarioStr.split(":");
            int hora = Integer.parseInt(partes[0]);
            int minuto = Integer.parseInt(partes[1]);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            calendar.set(Calendar.HOUR_OF_DAY, hora);
            calendar.set(Calendar.MINUTE, minuto);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            return calendar.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    private void agendarAlarme(Remedio remedio, long tempoAlarmeMs) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);

        intent.putExtra("nome_remedio", remedio.getNome());
        intent.putExtra("descricao_remedio", remedio.getDescricao());
        intent.putExtra("remedio_id", remedio.getId());

        int requestCode = remedio.getId().hashCode();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    tempoAlarmeMs,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
        }
    }

    private void cancelarAlarme(Remedio remedio) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);

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