package com.rss.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class ReadOutItem extends AppCompatActivity {
    String desc, title, url;
    TextToSpeech tts;
    Intent intent;
    TextView rss_title, rss_description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_out_item);

        rss_title = findViewById(R.id.title);
        rss_description = findViewById(R.id.desc);

        intent = getIntent();
        desc = intent.getStringExtra("desc");
        title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");

        rss_description.setText(desc);
        rss_title.setText(title);
        tts=new TextToSpeech(ReadOutItem.this, status -> {
            if(status == TextToSpeech.SUCCESS){
                int result=tts.setLanguage(Locale.US);
                if(result==TextToSpeech.LANG_MISSING_DATA ||
                        result==TextToSpeech.LANG_NOT_SUPPORTED){
                    Log.e("error", "This Language is not supported");
                }
                else{
                    ConvertTextToSpeech();
                }
            }
            else
                Log.e("error", "Initilization Failed!");
        });
    }
    @Override
    protected void onPause() {
        if(tts != null){
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }

    public void onClick(View v){
        ConvertTextToSpeech();
    }
    private void ConvertTextToSpeech() {
        if(desc ==null||"".equals(desc))
        {
            desc = "Unable to get Feed";
        }
        tts.speak(desc, TextToSpeech.QUEUE_FLUSH, null);
    }
}