package com.example.tamagochi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.IOException;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {

    private static final String FILE_NAME = "best_score.txt";
    private long bestScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        TextView lifeTimeTextView = findViewById(R.id.lifeTimeTextView);
        TextView bestScoreTextView = findViewById(R.id.bestScoreTextView);
        Button restartButton = findViewById(R.id.restartButton);

        // Получаем время жизни из Intent
        long lifeTime = getIntent().getLongExtra("LIFE_TIME", 0);

        // Загружаем лучший результат из файла
        bestScore = loadBestScore();

        lifeTimeTextView.setText("Время жизни: " + lifeTime + " сек");
        bestScoreTextView.setText("Лучший результат: " + bestScore + " сек");

        // Обработчик кнопки начать заново
        restartButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
            startActivity(intent);
            finish();  // Закрываем текущую активность
        });
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
}
