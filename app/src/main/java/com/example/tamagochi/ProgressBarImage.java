package com.example.tamagochi;

import android.content.res.TypedArray;
import android.widget.ImageView;

public class ProgressBarImage {
    private ImageView imageView;
    private TypedArray progressImages; // Используем TypedArray для хранения изображений
    private int maxProgress;
    private int currentProgress;

    public ProgressBarImage(ImageView imageView, int imagesArrayId) {
        this.imageView = imageView;
        this.progressImages = imageView.getContext().getResources().obtainTypedArray(imagesArrayId);
        this.maxProgress = 100;
        this.currentProgress = maxProgress; // Начальное значение 100%
        updateImage();
    }

    public void setProgress(int progress) {
        this.currentProgress = progress;
        updateImage();
    }

    private void updateImage() {
        if (currentProgress == 0) {
            // Если прогресс равен 0, устанавливаем изображение sprite_10 (состояние равно 0)
            imageView.setImageResource(R.drawable.sprite_10);
        } else {
            // В противном случае вычисляем индекс изображения для текущего прогресса
            int progressIndex = currentProgress / (maxProgress / progressImages.length());
            if (progressIndex >= 0 && progressIndex < progressImages.length()) {
                imageView.setImageResource(progressImages.getResourceId(progressIndex, -1));
            }
        }
    }

    public void release() {
        progressImages.recycle(); // Освобождаем ресурсы после использования
    }
}
