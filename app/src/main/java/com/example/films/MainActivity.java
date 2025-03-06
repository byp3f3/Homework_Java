package com.example.films;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText filmInput;
    private Button searchButton;
    private TextView filmInfo;
    private ImageView filmPoster;

    private static final String BASE_URL = "https://kinopoiskapiunofficial.tech/api/v2.2/";
    private static final String API_KEY = "63724510-6df6-4f4a-a3f0-e74247042167";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Инициализация элементов интерфейса
        filmInput = findViewById(R.id.filmInput);
        filmInfo = findViewById(R.id.filmInfo);
        searchButton = findViewById(R.id.searchButton);
        filmPoster = findViewById(R.id.filmPoster);

        // Обработка нажатия на кнопку "Найти"
        searchButton.setOnClickListener(v -> {
            String input = filmInput.getText().toString();
            if (!input.isEmpty()) {
                getFilm(Integer.parseInt(input)); // Получение данных о фильме по ID
            } else {
                filmInfo.setText("Введите ID фильма"); // Сообщение об ошибке, если поле пустое
            }
        });
    }

    private void getFilm(int filmId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API filmApi = retrofit.create(API.class);
        Call<FilmResponce> call = filmApi.getCurrentFilm(filmId, API_KEY);

        call.enqueue(new Callback<FilmResponce>() {
            @Override
            public void onResponse(Call<FilmResponce> call, Response<FilmResponce> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FilmResponce filmResponce = response.body();

                    StringBuilder genresString = new StringBuilder();
                    for (Genre genre : filmResponce.getGenres()) {
                        genresString.append(genre.getGenre()).append(", ");
                    }

                    if (genresString.length() > 0) {
                        genresString.setLength(genresString.length() - 2);
                    }

                    String filmDetails = "Название: " + filmResponce.getNameRu() + "\n" +
                            "Год: " + filmResponce.getYear() + "\n" +
                            "Описание: " + filmResponce.getDescription() + "\n" +
                            "Рейтинг: " + filmResponce.getRatingKinopoisk() + "\n" +
                            "Жанры: " + genresString.toString();

                    // Отображение информации о фильме
                    filmInfo.setText(filmDetails);

                    // Загрузка и отображение постера с помощью Picasso
                    if (filmResponce.getPosterUrl() != null && !filmResponce.getPosterUrl().isEmpty()) {
                        Picasso.get()
                                .load(filmResponce.getPosterUrl())
                                .into(filmPoster);
                    } else {
                        filmPoster.setImageResource(R.drawable.placeholder); // Заглушка, если постер отсутствует
                    }
                } else {
                    // Обработка ошибок HTTP-запроса
                    String errorMessage;
                    switch (response.code()) {
                        case 404:
                            errorMessage = "Ошибка: Фильм не найден";
                            break;
                        case 500:
                            errorMessage = "Ошибка: Проблемы на сервере";
                            break;
                        default:
                            errorMessage = "Ошибка: Неизвестная ошибка";
                            break;
                    }
                    Log.e("MainActivity", "Ошибка получения данных: " + response.code() + " " + errorMessage);
                    filmInfo.setText(errorMessage);
                    filmPoster.setImageResource(0);
                    filmPoster.setImageDrawable(null);
                }
            }

            @Override
            public void onFailure(Call<FilmResponce> call, Throwable t) {
                Log.e("MainActivity", "Ошибка: Проверьте подключение к интернету " + t.getMessage());
                filmInfo.setText("Ошибка: Проверьте подключение к интернету");
            }
        });
    }
}