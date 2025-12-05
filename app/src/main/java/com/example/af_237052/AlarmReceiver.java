package com.example.af_237052;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "MEDICATION_CHANNEL";

    @Override
    public void onReceive(Context context, Intent intent) {
        String nomeRemedio = intent.getStringExtra("nome_remedio");
        String descricaoRemedio = intent.getStringExtra("descricao_remedio");
        String remedioId = intent.getStringExtra("remedio_id");

        if (nomeRemedio == null) nomeRemedio = "Medicamento";
        if (descricaoRemedio == null) descricaoRemedio = "Hora de tomar seu remédio!";


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.nomeapp);
            String description = "Canal para lembretes de medicamentos.";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("⏰ Lembrete de Medicamento")
                .setContentText("Hora de tomar: " + nomeRemedio)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(descricaoRemedio))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        int notificationId = remedioId.hashCode();

        notificationManager.notify(notificationId, builder.build());
    }
}
