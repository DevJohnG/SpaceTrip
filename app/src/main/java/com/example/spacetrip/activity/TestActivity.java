package com.example.spacetrip.activity;

import android.media.MediaPlayer;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.spacetrip.R;
import com.example.spacetrip.entities.Preguntas;
import com.example.spacetrip.service.JugadorApiService;
import com.example.spacetrip.service.SpacetripdbApiAdapter;
import com.example.spacetrip.service.SpacetripdbApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private List<Preguntas> questions;
    private int currentQuestionIndex;
    private int correctAnswers;
    private TextView questionTextView;
    private ImageView questionImageView;
    private Button[] answerButtons;
    private MediaPlayer buttonClickSound;
    private MediaPlayer correctSound;
    private MediaPlayer incorrectSound;
    private SpacetripdbApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mediaPlayer = MediaPlayer.create(this, R.raw.test);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        ImageButton exitButton = findViewById(R.id.exit_button);

        questionTextView = findViewById(R.id.question_text);
        questionImageView = findViewById(R.id.question_image);
        answerButtons = new Button[]{
                findViewById(R.id.answer_button_1),
                findViewById(R.id.answer_button_2),
                findViewById(R.id.answer_button_3),
                findViewById(R.id.answer_button_4)
        };

        buttonClickSound = MediaPlayer.create(this, R.raw.presionar);
        correctSound = MediaPlayer.create(this, R.raw.correcto);
        incorrectSound = MediaPlayer.create(this, R.raw.incorrecto);

        apiService = SpacetripdbApiAdapter.getApiService();
        fetchQuestions();

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickSound.start();
                Intent backIntent = new Intent(TestActivity.this, TestSelectionActivity.class);
                startActivity(backIntent);
                finish();
            }
        });
    }

    private void fetchQuestions() {
        apiService.getPreguntas().enqueue(new Callback<ArrayList<Preguntas>>() {
            @Override
            public void onResponse(Call<ArrayList<Preguntas>> call, Response<ArrayList<Preguntas>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questions = response.body();
                    Collections.shuffle(questions);
                    currentQuestionIndex = 0;
                    correctAnswers = 0;
                    showQuestion();
                } else {
                    Toast.makeText(TestActivity.this, "Error fetching questions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Preguntas>> call, Throwable t) {
                Toast.makeText(TestActivity.this, "Failed to fetch questions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Preguntas currentQuestion = questions.get(currentQuestionIndex);
            questionTextView.setText(currentQuestion.getPregunta());

            String imageUrl = currentQuestion.getImageUrl();
            Log.d("TestActivity", "Image URL: " + imageUrl); // Añade esto para verificar la URL

            if (imageUrl == null || imageUrl.isEmpty()) {
                questionImageView.setVisibility(View.GONE);
            } else {
                questionImageView.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.jupiter)
                        .error(R.drawable.jupiter)
                        .into(questionImageView);
            }

            List<String> answers = new ArrayList<>();
            answers.add(currentQuestion.getRespuesta_correcta());
            answers.add(currentQuestion.getRespuesta_incorrecta_1());
            answers.add(currentQuestion.getRespuesta_incorrecta_2());
            answers.add(currentQuestion.getRespuesta_incorrecta_3());
            Collections.shuffle(answers);

            for (int i = 0; i < answerButtons.length; i++) {
                answerButtons[i].setText(answers.get(i));
                answerButtons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonClickSound.start();
                        checkAnswer(((Button) v).getText().toString());
                    }
                });
            }
        } else {
            finishQuiz();
        }
    }



    private void showCustomToast(String message, boolean isCorrect) {
        int layoutId = isCorrect ? R.layout.toast_correct : R.layout.toast_incorrect;
        View layout = getLayoutInflater().inflate(layoutId, findViewById(android.R.id.content), false);
        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER, 0, 100);
        toast.show();

        if (isCorrect) {
            correctSound.start();
        } else {
            incorrectSound.start();
        }
    }

    private void checkAnswer(String selectedAnswer) {
        Preguntas currentQuestion = questions.get(currentQuestionIndex);
        if (selectedAnswer.equals(currentQuestion.getRespuesta_correcta())) {
            correctAnswers++;
            showCustomToast("CORRECTO!", true);
        } else {
            showCustomToast("INCORRECTO!", false);
        }

        hideButtons();

        currentQuestionIndex++;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showQuestion();
                showButtons();
            }
        }, 2400); //debe ser 2400 para el dinamismo correcto
    }


    private void showButtons() {
        for (Button button : answerButtons) {
            button.setVisibility(View.VISIBLE);
        }
    }

    private void hideButtons() {
        for (Button button : answerButtons) {
            button.setVisibility(View.INVISIBLE);
        }
    }

    private void finishQuiz() {
        int totalQuestions = questions.size();

        Intent intent = new Intent(TestActivity.this, ResultActivity.class);
        intent.putExtra("CORRECT_ANSWERS", correctAnswers);
        intent.putExtra("TOTAL_QUESTIONS", totalQuestions);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (buttonClickSound != null) {
            buttonClickSound.release();
            buttonClickSound = null;
        }
        if (correctSound != null) {
            correctSound.release();
            correctSound = null;
        }
        if (incorrectSound != null) {
            incorrectSound.release();
            incorrectSound = null;
        }
    }
}
