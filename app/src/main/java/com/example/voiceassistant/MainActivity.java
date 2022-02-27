package com.example.voiceassistant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView textViewTest;
    private ImageView imageViewMain;
    private SoundPool soundPool;
    private int sound_siren;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });

        textViewTest = findViewById(R.id.textViewTest);
        imageViewMain = findViewById(R.id.imageViewMain);
        createSoundPool();
        loadSounds();
    }

    public void onClickMicrophone(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 10) {
                ArrayList<String> textList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                textCommand(textList.get(0));
            }
        }
    }

    private void textCommand(String text) {
        switch (text) {
            case "что надо сказать":
                textToSpeech.speak("Конечно же СПАСИБО", TextToSpeech.QUEUE_FLUSH, null);
                break;
            case "протокол протоколов":
                protocolProtocolov();
                break;
            case "корова":
                imageViewMain.setImageResource(R.drawable.cow);
                break;
            case "яблоко":
                imageViewMain.setImageResource(R.drawable.apple);
                break;
            case "арбуз":
                imageViewMain.setImageResource(R.drawable.watermelon);
                break;
            case "дыня":
                imageViewMain.setImageResource(R.drawable.melon);
                break;
        }
    }

    private void protocolProtocolov() {
        soundPool.play(sound_siren, 1, 1, 1, 0, 1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 6; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int finalI = 5 - i;
                    textViewTest.post(new Runnable() {
                        @Override
                        public void run() {
                            textViewTest.setText(String.valueOf(finalI));
                        }
                    });
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageViewMain.setImageResource(R.drawable.skull);
                    }
                });
            }
        }).start();
    }

    protected void createSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    private void loadSounds() {
        sound_siren = soundPool.load(this, R.raw.siren, 1);
    }
}