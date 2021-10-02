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

public class MainActivity extends AppCompatActivity {

    // Описание тэга для логов debug
    private static final String TAG = "myLogs";

    // Объявим переменные компонентов
    ListView userList;

    // Переменная для работы с БД
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase(); // Открываем подключение

        // Найдем компонент в XML разметке
        userList = (ListView) findViewById(R.id.list);

        List<String> nameEpisodes = new ArrayList<>();

        // Выполним запрос для дальнейшего получения имени торжества
        Cursor cursor = db.rawQuery("SELECT * FROM episodes", null);
        cursor.moveToFirst();

        // Присваиваем List<String> значения
        while (!cursor.isAfterLast()) {
            nameEpisodes.add(cursor.getString(2));
            cursor.moveToNext();
        }

        cursor.close();

        // Используем адаптер данных
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, nameEpisodes);

        // Присваиваем listView значения
        userList.setAdapter(adapter);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // проверяем отсылаемое значение
                Log.d(TAG, "Отправляем во второе активити");
                Log.d(TAG, nameEpisodes.get((int)id));

                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                intent.putExtra("nameEp", nameEpisodes.get((int)id));
                startActivity(intent);
            }
        });

    }
}