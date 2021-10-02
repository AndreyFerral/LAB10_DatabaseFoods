package com.example.lab10_databasefoods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    // Описание тэга для логов debug
    private static final String TAG = "myLogs";

    // Переменные для получения данных с активити
    String nameEp;

    // Переменная компонента
    ListView userListTypeFoods;

    // Переменная для работы с БД
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase(); // открываем подключение

        // Найдем компонент в XML разметке
        userListTypeFoods = (ListView) findViewById(R.id.list);

        // Получаем данные с первого активити
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nameEp = extras.getString("nameEp");
        }

        List<String> nameTypeFoods = new ArrayList<>();

        // Используем запросы на выборку типов еды
        Cursor cursor = db.rawQuery(        "SELECT " +
                "foods.id AS id, " +
                "episodes.name AS nameEp, " +
                "food_types.name AS nameType, " +
                "foods.name AS name " +
                "FROM episodes " +
                "INNER JOIN foods_episodes ON foods_episodes.episode_id = episodes.id " +
                "INNER JOIN foods ON foods.id = foods_episodes.food_id " +
                "INNER JOIN food_types ON food_types.id = foods.type_id " +
                "WHERE nameEp=?", new String[]{String.valueOf(nameEp)});
        cursor.moveToFirst();

        // Присваиваем List<String> значения
        while (!cursor.isAfterLast()) {
            //Log.d(TAG, cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3));

            // Не добавляем дублирующиеся элементы
            if (!nameTypeFoods.contains(cursor.getString(2))) nameTypeFoods.add(cursor.getString(2));

            cursor.moveToNext();
        }

        cursor.close();

        // Используем адаптер данных
        ArrayAdapter<String> adapterTypeFoods = new ArrayAdapter<>(UserActivity.this,
                android.R.layout.simple_list_item_1, nameTypeFoods);

        // Присваиваем listView значения
        userListTypeFoods.setAdapter(adapterTypeFoods);

        // При нажатии на элемент listView отправляет третьему активити параметры
        userListTypeFoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // проверяем отсылаемое значение
                Log.d(TAG, "Отправляем в третье активити");
                Log.d(TAG, nameEp);
                Log.d(TAG, nameTypeFoods.get((int)id));

                Intent intent = new Intent(UserActivity.this, SubUserActivity.class);
                intent.putExtra("nameEp", nameEp);
                intent.putExtra("nameTypeFood", nameTypeFoods.get((int)id));
                startActivity(intent);
            }
        });

    }
}