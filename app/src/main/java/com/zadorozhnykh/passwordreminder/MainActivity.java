package com.zadorozhnykh.passwordreminder;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

//Class for work with the DB
class MyDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 2;

    //Table and column names
    public static final String TABLE_NAME = "passwords";
    public static final String COL_NAME = "pName";
    public static final String COL_LOGIN = "pLogin";
    public static final String COL_PASS = "pPass";
    private static final String STRING_CREATE = "CREATE TABLE "+TABLE_NAME+" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COL_NAME+" TEXT, "+COL_LOGIN+" TEXT, " +COL_PASS+ " TEXT);";

    public MyDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(STRING_CREATE);
        ContentValues cv = new ContentValues(2);
        //cv.put(COL_NAME, "Source");
        //cv.put(COL_LOGIN, "Login");
        //cv.put(COL_PASS, "Password");
        db.insert(TABLE_NAME, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}



public class MainActivity extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    EditText nameText, loginText, passText;
    Button mAdd;
    ListView mList;

    MyDbHelper mHelper;
    SQLiteDatabase mDb;
    Cursor mCursor;
    SimpleCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameText = (EditText)findViewById(R.id.name);
        loginText = (EditText)findViewById(R.id.login);
        passText = (EditText)findViewById(R.id.pass);

        mAdd = (Button)findViewById(R.id.add);
        mAdd.setOnClickListener(this);

        mList = (ListView)findViewById(R.id.list);
        mList.setOnItemClickListener(this);

        mHelper = new MyDbHelper(this);

        registerForContextMenu (mList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
//Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.action_main:
               return true;
            case R.id.action_settings:
                MainActivity.this.startActivity(new Intent(MainActivity.this, MainActivity3.class));
                finish();
                return true;
            case R.id.action_about:
                MainActivity.this.startActivity(new Intent(MainActivity.this,About.class));
                finish();
                return true;
            case R.id.action_exit:
                finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    //Context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }


    //Adding values to DB and show them at ListView by clicking button "ADD"
    @Override
    public void onClick(View v) {
        ContentValues cv = new ContentValues(3);

        //
        cv.put(MyDbHelper.COL_NAME, nameText.getText().toString());
        cv.put(MyDbHelper.COL_LOGIN, loginText.getText().toString() + " / " + passText.getText().toString());
        //Two strings (may be fixed later)

        mDb.insert(MyDbHelper.TABLE_NAME, null, cv);

        //Update
        mCursor.requery();
        mAdapter.notifyDataSetChanged();

        //Delete text from EditText fields
        nameText.setText(null);
        loginText.setText(null);
        passText.setText(null);

        //Toast
        Toast toast = Toast.makeText(getApplicationContext(),
                "Added!", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        mCursor.moveToPosition(position);

    }


    //Delete items from the list
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.delete_id:
                mCursor.moveToPosition(info.position);
                String rowId = mCursor.getString(0); //Column 0 of the cursor is the id
                mDb.delete(MyDbHelper.TABLE_NAME, "_id = ?", new String[]{rowId});
                mCursor.requery();
                mAdapter.notifyDataSetChanged();

                //Show toast
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Item removed!", Toast.LENGTH_SHORT);
                toast.show();
        }


        return super.onContextItemSelected(item);


    }

    @Override
    public void onResume() {
        super.onResume();
        mDb = mHelper.getWritableDatabase();
        String[] columns = new String[] {"_id", MyDbHelper.COL_NAME, MyDbHelper.COL_LOGIN, MyDbHelper.COL_PASS};
        mCursor = mDb.query(MyDbHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        String[] headers = new String[] {MyDbHelper.COL_NAME, MyDbHelper.COL_LOGIN, MyDbHelper.COL_PASS};
        mAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                mCursor, headers, new int[]{android.R.id.text1, android.R.id.text2});
        mList.setAdapter(mAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        mDb.close();
        mCursor.close();
    }














}

