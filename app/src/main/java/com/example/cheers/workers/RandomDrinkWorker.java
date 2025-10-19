package com.example.cheers.workers;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.cheers.R;
import com.example.cheers.model.Drink;
import com.example.cheers.model.DrinksResponse;
import com.example.cheers.network.CocktailRepository;

import java.io.IOException;

import retrofit2.Response;

public class RandomDrinkWorker extends Worker {

    private static final String CHANNEL_ID = "DRINK_SUGGESTION_CHANNEL";
    private final Context context;

    public RandomDrinkWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        // Reutilizando o seu repositório já existente
        CocktailRepository repository = new CocktailRepository();

        try {
            // A chamada de rede precisa ser síncrona dentro de um Worker
            Response<DrinksResponse> response = repository.getRandomDrink().execute();

            if (response.isSuccessful() && response.body() != null && !response.body().getDrinks().isEmpty()) {
                Drink randomDrink = response.body().getDrinks().get(0);
                String drinkName = randomDrink.getName();

                // Dispara a notificação com o nome do drink
                sendNotification(drinkName);

                // Retorna sucesso para o WorkManager
                return Result.success();
            } else {
                // Se a API falhou, indica falha para o WorkManager tentar novamente depois
                return Result.retry();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Se ocorreu um erro de rede, indica falha para tentar novamente
            return Result.retry();
        }
    }

    private void sendNotification(String drinkName) {
        // 1. Criar o canal de notificação (obrigatório para Android 8.0+)
        createNotificationChannel();

        // 2. Construir a notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_cheers_notification) // CRIE UM ÍCONE PARA NOTIFICAÇÃO!
                .setContentTitle("Sugestão do Dia!")
                .setContentText("Que tal experimentar um " + drinkName + " hoje?")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // A notificação some ao ser tocada

        // 3. Exibir a notificação
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // O ID da notificação (101) pode ser qualquer número.
        // É importante pedir permissão em apps mais novos, mas o WorkManager lida bem com isso.
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(101, builder.build());
    }

    private void createNotificationChannel() {
        // O canal só precisa ser criado uma vez, então verificamos a versão do Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Sugestões de Drinks";
            String description = "Canal para receber sugestões diárias de drinks";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Registra o canal com o sistema
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
