package com.zadorozhnykh.passwordreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.view.View.OnClickListener;


public class MainActivity2 extends ActionBarActivity implements OnClickListener {

    static SharedPreferences mSettings;

    Vibrator vibrator;
    EditText mEdit;
    TextView tWrong, tCaption;
    Button button, button_create;

    static final String SAVED_PASS = "pass";
    private static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_activity2);

        Log.d(TAG, "start!!!");

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        button_create = (Button) findViewById(R.id.button_create);
        button_create.setOnClickListener(this);

        mEdit = (EditText) findViewById(R.id.pass);
        tWrong = (TextView) findViewById(R.id.textWrong);
        tCaption = (TextView) findViewById(R.id.textView);
        vibrator = (Vibrator) getSystemService (VIBRATOR_SERVICE);

       mSettings = getPreferences(MODE_PRIVATE);

        if (mSettings.getString(SAVED_PASS, "").equals(""))
        {
            button.setVisibility(View.INVISIBLE);
            button_create.setVisibility(View.VISIBLE);
            tCaption.setText(R.string.enter_the_new_pass);

        } else {
            button.setVisibility(View.VISIBLE);
            button_create.setVisibility(View.INVISIBLE);
            tCaption.setText(R.string.enter_pass);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //If pressed Create
            case R.id.button_create:
                createPass();
                break;
            //If pressed OK
            case R.id.button:
                enterApp();
                break;
            default:
                break;
        }
    }

    public String substringBegin(String s) // deleting spaces before pass
    {
        if (s.charAt(0) == ' ') {
            s = s.substring(1, s.length());
            return substringBegin(s);
        } else {
            return s;
        }
    }

    public String substringEnd(String s) // deleting spaces after pass
    {
        if (s.charAt(s.length()-1) == ' ') {
            s = s.substring(0, s.length()-1);
            return substringEnd(s);
        } else {
            return s;
        }
    }

    //If the Create button pressed
    void createPass() {

        mSettings = getPreferences(MODE_PRIVATE);


        String passwordEntered = mEdit.getText().toString();

        if (passwordEntered == "")
        {
            Toast.makeText(this, "Password can't be empty", Toast.LENGTH_SHORT).show();
            mEdit.setText("");
        }
        passwordEntered = substringBegin(passwordEntered);
        passwordEntered = substringEnd(passwordEntered);


        Editor ed = mSettings.edit();
        ed.putString(SAVED_PASS, passwordEntered);
        ed.apply();
        //Log for logcat, used for debugging
        Log.d(TAG, "Password = " + mSettings.getString(SAVED_PASS, ""));

        Toast.makeText(this, "Password saved", Toast.LENGTH_SHORT).show();
        mEdit.setText(null);

        button.setVisibility(View.VISIBLE);
        button_create.setVisibility(View.INVISIBLE);
    }

    //If the Enter button pressed
    void enterApp() {
        if (mEdit.getText().toString().equals ( mSettings.getString(SAVED_PASS, ""))) {
            MainActivity2.this.startActivity(new Intent(MainActivity2.this, MainActivity.class));
            mEdit.setText("");
            tWrong.setVisibility(View.INVISIBLE);
            Log.d(TAG, "Password = " + mSettings.getString(SAVED_PASS, ""));

        }
        //If the password is wrong
        else {
            vibrator.vibrate(300);
            tWrong.setVisibility(View.VISIBLE);//show "Wrong Password" message
            mEdit.setText(null);//remove wrong password from EditText field

        }
    }

    //remove "Wrong Password" text by tap
    public boolean onTouchEvent(MotionEvent event) {
        tWrong.setVisibility(View.INVISIBLE);
        return true;
    }
}
