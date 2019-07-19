package com.example.android.play_api;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);

        name = (EditText) findViewById(R.id.name);
        name.setText(getName());

        sizes = (Spinner) findViewById(R.id.sizes);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sizes, R.layout.spinner_text);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        sizes.setAdapter(adapter);
        sizes.setSelection(0);
        sizes.setOnItemSelectedListener(this);

        langs = (Spinner) findViewById(R.id.langs);
        ArrayAdapter<CharSequence> langadapter = ArrayAdapter.createFromResource(this,
                R.array.languages, R.layout.spinner_text);
        langadapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        langs.setAdapter(langadapter);
        langs.setSelection(0);
        langs.setOnItemSelectedListener(this);
    }

    private String getName() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("PLAY_API",MODE_PRIVATE);
        return prefs.getString("Name","User");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId()== R.id.sizes) {
            sizes.setSelection(i);
        }
        else if(adapterView.getId() == R.id.langs)
        {
            langs.setSelection(i);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }*/
}
