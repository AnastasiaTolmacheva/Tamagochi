package com.example.tamagochi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Creature {
    private static final int MAX_STATE = 100;
    private static final int MIN_STATE = 0;

    private int satiety, energy, cleanliness, happiness;
    private ImageView creatureImage;
    private ProgressBarImage satietyBar, energyBar, cleanlinessBar, happinessBar;
    private Context context;

    public Creature(Context context, ImageView creatureImage, ProgressBarImage satietyBar, ProgressBarImage energyBar, ProgressBarImage cleanlinessBar, ProgressBarImage happinessBar) {
        this.context = context;
        this.creatureImage = creatureImage;
        this.satietyBar = satietyBar;
        this.energyBar = energyBar;
        this.cleanlinessBar = cleanlinessBar;
        this.happinessBar = happinessBar;

        satiety = cleanliness = happiness = energy = 100;  // Начальные состояния
    }

    public void feed() {
        satiety = Math.min(satiety + 20, MAX_STATE); // Ограничиваем значение от 0 до MAX_STATE
        updateBars();
    }

    public void sleep() {
        energy = Math.min(energy + 20, MAX_STATE); // Ограничиваем значение от 0 до MAX_STATE
        updateBars();
    }

    public void play() {
        happiness = Math.min(happiness + 20, MAX_STATE); // Ограничиваем значение от 0 до MAX_STATE
        updateBars();
    }

    public void clean() {
        cleanliness = Math.min(cleanliness + 20, MAX_STATE); // Ограничиваем значение от 0 до MAX_STATE
        updateBars();
    }

    public void updateStates() {
        satiety = Math.max(satiety - 2, MIN_STATE); // Ограничиваем значение от MIN_STATE до MAX_STATE
        energy = Math.max(energy - 2, MIN_STATE); // Ограничиваем значение от 0 до MAX_STATE
        cleanliness = Math.max(cleanliness - 2, MIN_STATE); // Ограничиваем значение от MIN_STATE до MAX_STATE
        happiness = Math.max(happiness - 2, MIN_STATE); // Ограничиваем значение от MIN_STATE до MAX_STATE

        updateBars();
        checkStates();
    }

    private void updateBars() {
        satietyBar.setProgress(satiety);
        energyBar.setProgress(energy);
        cleanlinessBar.setProgress(cleanliness);
        happinessBar.setProgress(happiness);
    }

    private void checkStates() {
        /*
        Состояния существа:
            + happiness - счастье (грустное)
            + satiety - сытость (голодное)
            + energy - энергия (усталое)
            + cleanliness - чистота (грязное)

        1. Веселое (happiness, satiety, cleanliness, energy >= 75), sprite_cat00
        2. Нормальное (50 <= happiness, satiety, cleanliness, energy < 75), sprite_cat01
        3. Грустное (satiety, cleanliness, energy >= 50; 0 < happiness < 50), sprite_cat07
        4. Голодное (happiness, cleanliness, energy >= 50; 0 < satiety < 50), sprite_cat02
        5. Усталое (happiness, satiety, cleanliness >= 50; 0 < energy < 50), sprite_cat12
        6. Грязное (happiness, satiety, energy >= 50; 0 < cleanliness < 50), sprite_cat14
        7. Грустное + голодное (cleanliness, energy >= 50; 0 < happiness, satiety < 50), sprite_cat27
        8. Грустное + усталое (satiety, cleanliness >= 50; 0 < happiness, energy < 50), sprite_cat34
        9. Грустное + грязное (satiety, energy >= 50; 0 < happiness, cleanliness < 50), sprite_cat35
        10. Грустное + голодное + усталое (cleanliness >= 50; 0 < happiness, satiety, energy < 50), sprite_cat30
        11. Грустное + голодное + грязное (energy >= 50; 0 < happiness, satiety, cleanliness < 50), sprite_cat31
        12. Грустное + усталое + грязное (satiety >= 50; 0 < happiness, energy, cleanliness < 50), sprite_cat36
        13. Грустное + голодное + усталое + грязное (0 < happiness, energy, cleanliness, satiety < 50), sprite_cat33
        14. Голодное + усталое (cleanliness, happiness >= 50; 0 < energy, satiety < 50), sprite_cat28
        15. Голодное + грязное (energy, happiness >= 50; 0 < cleanliness, satiety < 50), sprite_cat29
        16. Усталое + грязное (satiety, happiness >= 50; 0 < cleanliness, energy < 50), sprite_cat37
        17. Голодное + усталое + грязное (happiness >= 50; 0 < satiety, energy, cleanliness < 50), sprite_cat32
         */

        if (happiness >= MAX_STATE || satiety >= MAX_STATE || energy >= MAX_STATE|| cleanliness <= MIN_STATE) {
            die();

        } else if (happiness >= 75 && satiety >= 75 && energy >= 75 && cleanliness >= 75) {
            creatureImage.setImageResource(R.drawable.sprite_cat00);  // 1. Веселое

        } else if (happiness >= 50 && happiness < 75 && satiety >= 50 && satiety < 75 && energy >= 50 && energy < 75 && cleanliness >= 50 && cleanliness < 75) {
            creatureImage.setImageResource(R.drawable.sprite_cat01);  // 2. Нормальное

        } else if (happiness < 50 && happiness > 0 && satiety >= 50 && energy >= 50 && cleanliness >= 50) {
            creatureImage.setImageResource(R.drawable.sprite_cat07); // 3. Грустное

        } else if (happiness >= 50 && satiety < 50 && satiety > 0 && energy >= 50 && cleanliness >= 50) {
            creatureImage.setImageResource(R.drawable.sprite_cat02); // 4. Голодное

        } else if (energy < 50 && energy > 0 && satiety >= 50 && happiness >= 50 && cleanliness >= 50) {
            creatureImage.setImageResource(R.drawable.sprite_cat12); // 5. Усталое

        } else if (cleanliness < 50 && cleanliness > 0 && satiety >= 50 && happiness >= 50 && energy >= 50) {
            creatureImage.setImageResource(R.drawable.sprite_cat14); // 6. Грязное

        } else if (happiness < 50 && happiness > 0 && satiety < 50 && satiety > 0 && cleanliness >= 50 && energy >= 50) {
            creatureImage.setImageResource(R.drawable.sprite_cat27); // 7. Грустное + голодное

        } else if (happiness < 50 && happiness > 0 && energy < 50 && energy > 0 && cleanliness >= 50 && satiety >= 50) {
            creatureImage.setImageResource(R.drawable.sprite_cat34); // 8. Грустное + усталое

        } else if (happiness < 50 && happiness > 0 && cleanliness < 50 && cleanliness > 0 && energy >= 50 && satiety >= 50) {
            creatureImage.setImageResource(R.drawable.sprite_cat35); // 9. Грустное + грязное

        } else if (happiness < 50 && happiness > 0 && satiety < 50 && satiety > 0 && energy < 50 && energy > 0 && cleanliness >= 50) {
            creatureImage.setImageResource(R.drawable.sprite_cat30); // 10. Грустное + голодное + усталое

        } else if (happiness < 50 && happiness > 0 && satiety < 50 && satiety > 0 && cleanliness < 50 && cleanliness > 0 && energy >= 50) {
            creatureImage.setImageResource(R.drawable.sprite_cat31); // 11. Грустное + голодное + грязное

        } else if (happiness < 50 && happiness > 0 && energy < 50 && energy > 0 && cleanliness < 50 && cleanliness > 0 && satiety >= 50) {
            creatureImage.setImageResource(R.drawable.sprite_cat36); // 12. Грустное + усталое + грязное

        } else if (happiness < 50 && happiness > 0 && energy < 50 && energy > 0 && cleanliness < 50 && cleanliness > 0 && satiety < 50 && satiety > 0 ) {
            creatureImage.setImageResource(R.drawable.sprite_cat33); // 13. Грустное + голодное + усталое + грязное

        } else if (satiety < 50 && satiety > 0 && energy < 50 && energy > 0 && cleanliness >= 50 && happiness >= 50) {
            creatureImage.setImageResource(R.drawable.sprite_cat28); // 14. Голодное + усталое

        } else if (satiety < 50 && satiety > 0 && cleanliness < 50 && cleanliness > 0 && energy >= 50 && happiness >= 50) {
            creatureImage.setImageResource(R.drawable.sprite_cat29); // 15. Голодное + грязное

        } else if (energy < 50 && energy > 0 && cleanliness < 50 && cleanliness > 0 && satiety >= 50 && happiness >= 50) {
            creatureImage.setImageResource(R.drawable.sprite_cat37); // 16. Усталое + грязное

        } else if (satiety < 50 && satiety > 0 && energy < 50 && energy > 0 && cleanliness < 50 && cleanliness > 0 && happiness >= 50) {
            creatureImage.setImageResource(R.drawable.sprite_cat32); // 17. Голодное + усталое + грязное
        }
    }

    public boolean isAlive() {
        return satiety > MIN_STATE && energy > MIN_STATE && cleanliness > MIN_STATE && happiness > MIN_STATE;
    }

    public void die() {
        // Устанавливаем изображение умершего существа
        // creatureImage.setImageResource(R.drawable.sprite_cat26);

        // Создаем кастомный Toast
        LayoutInflater inflater = LayoutInflater.from(context); // Получаем LayoutInflater
        View layout = inflater.inflate(R.layout.custom_dead, null); // Инфлейтим кастомный макет тоста

        // Настраиваем изображение и текст внутри тоста
        // ImageView toastImage = layout.findViewById(R.id.toastImage);
        // TextView toastText = layout.findViewById(R.id.toastText);

        // toastImage.setImageResource(R.drawable.sprite_cat26); // Устанавливаем иконку
        // toastText.setText("Я умер!"); // Устанавливаем текст

        // Создаем и показываем кастомный Toast
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT); // Длительность
        toast.setView(layout); // Устанавливаем кастомный макет
        toast.show(); // Показываем Toast
    }
}
