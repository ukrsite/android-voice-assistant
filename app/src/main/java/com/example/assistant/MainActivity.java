package com.example.assistant;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.Locale;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {

    protected Button sendButton;
    protected EditText userMessage;
    protected RecyclerView chatWindow;
    protected TextToSpeech textToSpeech;

    protected MessageController messageController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = findViewById(R.id.sendButton);
        userMessage = findViewById(R.id.userMessage);
        chatWindow = findViewById(R.id.chatWindow);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
//                получить_вопрос/вернуть_ответ
                onClickListener();
            }
        });

        messageController = new MessageController();
        chatWindow.setLayoutManager(new LinearLayoutManager(this));
        chatWindow.setAdapter(messageController);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(new Locale("en"));
                textToSpeech.setPitch(1.3f);
                textToSpeech.setSpeechRate(0.8f);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onClickListener() {
        String message = userMessage.getText().toString();
        userMessage.setText("");

        messageController.messageList
                .add(new Message(message, true));
        AI.getAnswer(message, new Consumer<String>() {
            @Override
            public void accept(String answer) {
            // метод будет вызван когда готов ответ
                messageController.messageList
                        .add(new Message(answer, false));
                textToSpeech.speak(
                        answer, TextToSpeech.QUEUE_FLUSH, null, null);
                messageController.notifyDataSetChanged();
                chatWindow.scrollToPosition(
                        messageController.messageList.size()-1);
            }
        });
    }
}
