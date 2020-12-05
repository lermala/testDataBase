package com.example.testdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnRead, btnClear;
    EditText etName;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.etName);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {

        String name, type; // имя и тип одежды
        String imageUri; // путь до картинки с одеждой

        name = "name1";
        type = "type1";
        imageUri = "URi";

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        switch (v.getId()) {

            case R.id.btnAdd:
                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_TYPE, type);
                contentValues.put(DBHelper.KEY_NAME, imageUri.toString());

                Log.d( "mLog", "name = " + name +
                        ", type = " + type +
                        ", picture = " + imageUri.toString());

                // вставляем запись
                database.insert(DBHelper.TABLE_CLOTHES, null, contentValues);

                Log.d("mLog", "ROW INSERTED " +
                        "name = " + DBHelper.KEY_NAME +
                        ", type = " + DBHelper.KEY_TYPE +
                        ", picture = " + DBHelper.KEY_PICTURE
                );
                break;

            case R.id.btnRead:
                // делаем запрос всех данных из таблицы table_clothes, получаем Cursor
                Cursor cursor = database.query(DBHelper.TABLE_CLOTHES, null, null,
                        null, null, null, null);

                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (cursor.moveToFirst()){
                    // определяем номера столбцов по имени в выборке
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                    int typeIndex = cursor.getColumnIndex(DBHelper.KEY_TYPE);
                    int pictureIndex = cursor.getColumnIndex(DBHelper.KEY_PICTURE);

                    do {
                        Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                                ", name = " + cursor.getString(nameIndex) +
                                ", type = " + cursor.getString(typeIndex) +
                                ", picture = " + cursor.getString(pictureIndex));
                    } while (cursor.moveToNext());
                } else
                    Log.d("mLog","0 rows");
                break;

            case R.id.btnClear:
                database.delete(DBHelper.TABLE_CLOTHES, null, null);
                break;
        }
        dbHelper.close();
    }
}