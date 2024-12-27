package com.example.tamagochi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView creatureImage;
    private ProgressBarImage satietyBar, energyBar, cleanlinessBar, happinessBar;
    private TextView lifeTimeTextView;
    private Creature creature;
    private Handler handler;
    private Runnable stateUpdater;
    private long startTime;
    private boolean isGameRunning;
    private static final String FILE_NAME = "best_score.txt";
    private long bestScore = 0;
    private long delay = 500; // Изначальная задержка
    private final long minDelay = 100; // Минимальная задержка для ограничения скорости


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Загрузка лучшего результата из файла
        bestScore = loadBestScore();

        creatureImage = findViewById(R.id.creatureImage);
        satietyBar = new ProgressBarImage((ImageView) findViewById(R.id.satietyBar), R.array.satiety_images);
        energyBar = new ProgressBarImage((ImageView) findViewById(R.id.energyBar), R.array.energy_images);
        cleanlinessBar = new ProgressBarImage((ImageView) findViewById(R.id.cleanlinessBar), R.array.cleanliness_images);
        happinessBar = new ProgressBarImage((ImageView) findViewById(R.id.happinessBar), R.array.happiness_images);
        lifeTimeTextView = findViewById(R.id.lifeTimeTextView);

        // Создаем персонажа с прогресс-барами
        creature = new Creature(this, creatureImage, satietyBar, energyBar, cleanlinessBar, happinessBar);

        // Инициализация кнопок
        Button feedButton = findViewById(R.id.feedButton);
        Button sleepButton = findViewById(R.id.sleepButton);
        Button playButton = findViewById(R.id.playButton);
        Button cleanButton = findViewById(R.id.cleanButton);

        // Взаимодействие с персонажем
        feedButton.setOnClickListener(v -> creature.feed());
        sleepButton.setOnClickListener(v -> creature.sleep());
        playButton.setOnClickListener(v -> creature.play());
        cleanButton.setOnClickListener(v -> creature.clean());

        // Таймер и обновление состояния
        handler = new Handler();
        startTime = System.currentTimeMillis();
        isGameRunning = true;

        stateUpdater = new Runnable() {
            @Override
            public void run() {
                if (isGameRunning) {
                    creature.updateStates();
                    updateLifeTime();
                    if (!creature.isAlive()) {
                        isGameRunning = false;
                        // creature.die();
                        showGameOver();
                    } else {
                        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

                        // Ускоряем игру по мере увеличения времени жизни
                        delay = Math.max(minDelay, 1000 - elapsedTime * 10); // Уменьшаем задержку на 10 мс каждую секунду

                        handler.postDelayed(this, delay); // Обновление состояния с уменьшенной задержкой
                    }
                }
            }
        };
        handler.postDelayed(stateUpdater, delay);
    }

    @SuppressLint("SetTextI18n")
    private void updateLifeTime() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = (currentTime - startTime) / 1000; // Время в секундах
        lifeTimeTextView.setText("Время жизни: " + elapsedTime + " сек");
    }

    // Функция для сохранения лучшего результата в файл
    private void saveBestScore(long score) {
        try (FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE)) {
            String data = Long.toString(score);
            fos.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Функция для загрузки лучшего результата из файла
    private long loadBestScore() {
        long score = 0;
        try (FileInputStream fis = openFileInput(FILE_NAME)) {
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            String data = new String(buffer);
            score = Long.parseLong(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return score;
    }

    private void showGameOver() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = (currentTime - startTime) / 1000;

        // Обновляем лучший результат и сохраняем его
        if (elapsedTime > bestScore) {
            bestScore = elapsedTime;
            saveBestScore(bestScore); // Сохраняем лучший результат в файл
        }

        // Переход в GameOverActivity
        Intent intent = new Intent(MainActivity.this, GameOverActivity.class);
        intent.putExtra("LIFE_TIME", elapsedTime);
        intent.putExtra("BEST_SCORE", bestScore);
        startActivity(intent);
        finish();  // Закрываем текущую активность
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        satietyBar.release();
        energyBar.release();
        cleanlinessBar.release();
        happinessBar.release();

        SharedPreferences sharedPreferences = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        bestScore = sharedPreferences.getLong("BEST_SCORE", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("BEST_SCORE", bestScore);
        editor.apply();
    }
}
