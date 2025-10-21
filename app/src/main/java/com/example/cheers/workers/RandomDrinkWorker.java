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
        CocktailRepository repository = new CocktailRepository();

        try {
            Response<DrinksResponse> response = repository.getRandomDrink().execute();

            if (response.isSuccessful() && response.body() != null && !response.body().getDrinks().isEmpty()) {
                Drink randomDrink = response.body().getDrinks().get(0);
                String drinkName = randomDrink.getName();
                sendNotification(drinkName);
                return Result.success();
            } else {
                return Result.retry();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.retry();
        }
    }

    private void sendNotification(String drinkName) {
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_cheers_notification)
                .setContentTitle("Sugestão do Dia!")
                .setContentText("Que tal experimentar um " + drinkName + " hoje?")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(101, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Sugestões de Drinks";
            String description = "Canal para receber sugestões diárias de drinks";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
