package com.example.lab10_databasefoods;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SubUserActivity extends AppCompatActivity {

    // Описание тэга для логов debug
    private static final String TAG = "myLogs";

    // Переменные для получения данных с активити
    String nameEp;
    String nameTypeFood;

    // Переменная компонента
    ListView userListFoods;

    // Переменные для работы с БД
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_user);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();  // открываем подключение

        // Найдем компонент в XML разметке
        userListFoods = (ListView) findViewById(R.id.list);

        // Получаем значения со второго активити
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nameEp = extras.getString("nameEp");
            nameTypeFood = extras.getString("nameTypeFood");
        }

        List<String> nameFoods = new ArrayList<>();

        // Выполяеем запрос на выборку еды
        Cursor cursor = db.rawQuery(        "SELECT " +
                "foods.id AS id, " +
                "episodes.name AS nameEp, " +
                "food_types.name AS nameType, " +
                "foods.name AS name " +
                "FROM episodes " +
                "INNER JOIN foods_episodes ON foods_episodes.episode_id = episodes.id " +
                "INNER JOIN foods ON foods.id = foods_episodes.food_id " +
                "INNER JOIN food_types ON food_types.id = foods.type_id " +
                "WHERE nameEp=? and nameType=?", new String[]{nameEp, nameTypeFood});
        cursor.moveToFirst();

        Log.d(TAG, "Получаем значения по запросу");

        // Заполняем nameFoods значениями
        while (!cursor.isAfterLast()) {

            Log.d(TAG, cursor.getString(3)); // проверяем получаемые значение

            nameFoods.add(cursor.getString(3));
            cursor.moveToNext();
        }

        cursor.close();

        // Используем адаптер данных
        ArrayAdapter<String> adapterTypeFoods = new ArrayAdapter<>(SubUserActivity.this,
                android.R.layout.simple_list_item_1, nameFoods);

        // Заполяем userListFoods значениями
        userListFoods.setAdapter(adapterTypeFoods);
    }
}