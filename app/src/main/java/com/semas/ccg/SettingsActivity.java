package com.semas.ccg;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.os.Vibrator;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private SeekBar volumeSeekBar;
    private Switch vibrationSwitch;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private Button backButton;
    private SharedPreferences sharedPreferences;
    private Vibrator vibrator; // Объявляем переменную для вибратора

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Инициализация компонентов
        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        vibrationSwitch = findViewById(R.id.vibrationSwitch);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        backButton = findViewById(R.id.backButton);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE); // Инициализация вибратора

        // Установка начальных значений
        int currentVolume = sharedPreferences.getInt("volume", 50);
        boolean isVibrationEnabled = sharedPreferences.getBoolean("vibration", false);

        volumeSeekBar.setProgress(currentVolume);
        vibrationSwitch.setChecked(isVibrationEnabled);

        // Настройка SeekBar
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Установка громкости
                float volume = progress / 100f;
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress / 10, 0);
                // Сохранение громкости
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("volume", progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Настройка переключателя вибрации
        vibrationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Сохранение состояния вибрации
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("vibration", isChecked);
            editor.apply();

            if (isChecked) {
                // Включение вибрации
                if (vibrator != null) {
                    vibrator.vibrate(500); // Вибрация на 500 миллисекунд
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Закрытие текущей активности и возврат к предыдущей
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Освобождение ресурсов MediaPlayer, если используется
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}