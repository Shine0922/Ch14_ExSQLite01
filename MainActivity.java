package com.example.win7.exsqlite01;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity
{
    //  宣告全域資料庫類別物件 db
    private SQLiteDatabase db = null;

    //  建立資料表的欄位
    private final static String CREATE_TABLE = "CREATE TABLE table01(_id INTEGER PRIMARY KEY ,num INTERGER ,data TEXT)";

    ListView listwiew01;
    Button btnDo;
    EditText edtSQL;
    String str,itemdata;
    int n=1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listwiew01 = (ListView)findViewById(R.id.listview01);
        btnDo = (Button)findViewById(R.id.btnDo);
        edtSQL = (EditText)findViewById(R.id.edtSQL);

        btnDo.setOnClickListener(btnDoClick);

        //  預設 SQL 指令為新增資料
        itemdata = " 資料項目 " + n;
        str = "INSERT INTO table01 (num,data) " +
                "values (" + n + ",'" + itemdata + "') ";
        edtSQL.setText(str);

        //  建立資料庫， 若資料庫已存在則將之開啟
        db = openOrCreateDatabase("db1.db",MODE_PRIVATE,null);
        try
        {
            db.execSQL(CREATE_TABLE);   //  建立資料表
        }
        catch(Exception e)
        {
            UpdataAdapter();    //  載入資料表到 ListView 中
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //  故意刪除原有的資料表 讓每次執行時資料表都是空的
        db.execSQL("DROP TABLE table01");
        db.close(); //  關閉資料庫
    }

    private Button.OnClickListener btnDoClick = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            try
            {
                db.execSQL(edtSQL.getText().toString());    //  執行SQL
                UpdataAdapter();    //  更新 ListView
                n++;
                itemdata = " 資料項目" + n ;
                str="INSERT INTO table01 (num,data) values (" + n + ", '" + itemdata + "')";
                edtSQL.setText(str);

            }
            catch(Exception err )
            {
                setTitle("SQL 語法錯誤 ! ");
            }
        }
    };

    public void UpdataAdapter()
    {
        Cursor cursor = db.rawQuery("SELECT * FROM table01",null);
        if (cursor != null && cursor.getCount() >= 0)
        {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2, // 包含兩個資料項
                     cursor,                                                                                        // 資料庫的cursor 物件
                    new String[] {"num","data"},                                                                   //  num , data 欄位
                    new int[]{android.R.id.text1,android.R.id.text2},                                               //  與 num , data 對應的元件
                    0);                                                                                       //  adapter行為最佳化
                    listwiew01.setAdapter(adapter);                                                                 // 將adapter增加到 listview01 中
        }
    }
}


