package com.zadorozhnykh.passwordreminder;


import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity3 extends ActionBarActivity implements View.OnClickListener {



    EditText old, new1, new2;





    Button apply;

    private static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity3);




        apply = (Button)findViewById(R.id.apply);
        apply.setOnClickListener(this);




        old = (EditText) findViewById(R.id.editText);
        new1 = (EditText) findViewById(R.id.editText2);
        new2 = (EditText) findViewById(R.id.editText3);
    }



   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

             case R.id.action_exit:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.apply:
                Log.d(TAG, "кнопка Create");
                applyChange();
                break;

            default:
                break;
        }
    }

    void applyChange()        {
        //If everything OK and the password has been changed!

        if (new1.getText().toString().equals("")) {
            Toast.makeText(this, "Password can not be empty", Toast.LENGTH_SHORT).show();
            new1.setText("");
        } else {
        if (!new1.getText().toString().equals(new2.getText().toString()))
                Toast.makeText(this, "The new passwords must be same", Toast.LENGTH_SHORT).show();
        else
        if (!old.getText().toString().equals(MainActivity2.mSettings.getString(MainActivity2.SAVED_PASS, "")))
                Toast.makeText(this, "The old passwords is not correct", Toast.LENGTH_SHORT).show();
        else if (old.getText().toString().equals(MainActivity2.mSettings.getString(MainActivity2.SAVED_PASS, "")) &&
                   new1.getText().toString().equals(new2.getText().toString())) {
            MainActivity2.mSettings = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor ed = MainActivity2.mSettings.edit();
            ed.putString(MainActivity2.SAVED_PASS, new1.getText().toString());
            ed.apply();
            Log.d(TAG, "Saved_PASS = " + MainActivity2.SAVED_PASS);
            Log.d(TAG, "mSettings = " + MainActivity2.mSettings.getString(MainActivity2.SAVED_PASS, ""));
            Toast.makeText(this, "Password changed", Toast.LENGTH_SHORT).show();
            old.setText(null);
            new1.setText(null);
            new2.setText(null);
        }
        else
            Toast.makeText(this, "Please try again!", Toast.LENGTH_LONG).show();
    }
}

}

