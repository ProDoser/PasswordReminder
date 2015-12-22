package com.zadorozhnykh.passwordreminder;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class About extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_main:
                About.this.startActivity(new Intent(About.this, MainActivity.class));
                finish();
                return true;
            case R.id.action_settings:
                About.this.startActivity(new Intent(About.this, MainActivity3.class));
                finish();
                return true;
            case R.id.action_about:
                return true;
            case R.id.action_exit:
                finish();
                return true;



        }

        return super.onOptionsItemSelected(item);
    }

}
