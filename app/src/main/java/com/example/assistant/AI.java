package com.example.assistant;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AI {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void getAnswer(String user_question, final Consumer<String> callback) {
        Map<String, String> database = new HashMap<String, String>(){{
            put("hello", "Good day!");
            put("how are you", "I'm fine. thanks");
            put("you doing", "I answer stupid questions:)");
            put("your name", "I am voice assistance Stepan");
            put("you write", "I developed by Stas");
        }};

        user_question = user_question.toLowerCase();
        final ArrayList<String> answers = new ArrayList<>();

        int max_score = 0;
        String max_score_answer = "Ok";
        String[] split_user = user_question.split("\\s+");

        for (String database_question : database.keySet()) {
            String[] split_db = database_question.split("\\s+");

            int score = 0;

            for (String word_user : split_user) {
                for (String word_db : split_db) {
                    if (word_user.equals(word_db)) {
                        score++;
                    }
                }
            }

            if (score > max_score) {
                max_score = score;
                max_score_answer = database.get(database_question);
            }

            if(max_score > 0) {
                answers.add(max_score_answer);
            }

//            if (user_question.contains(database_question)) {
//                answers.add(database.get(database_question));
//            }
        }

        Pattern cityPattern = Pattern.compile(
                "what is the weather in (\\p{L}+)",
                Pattern.CASE_INSENSITIVE);
        Pattern queryPattern = Pattern.compile(
                "tell me about (\\p{L}+)",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = cityPattern.matcher(user_question);
        Matcher matcherQuery = queryPattern.matcher(user_question);
        if (matcher.find()) {
            String cityName = matcher.group(1);
            Weathar.get(cityName, new Consumer<String>() {
                @Override
                public void accept(String s) {
                    answers.add(s);
                    callback.accept(String.join(", ", answers));
                }
            });
        } else if (matcherQuery.find())  {
            String query = matcherQuery.group(1);
            Chucknorris.get(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    answers.add(s);
                    callback.accept(String.join(", ", answers));
                }
            });
        } else {
            if (answers.isEmpty()) {
                callback.accept("OK");
                return;
            }
            callback.accept(String.join(", ", answers));
        }
    }
}
