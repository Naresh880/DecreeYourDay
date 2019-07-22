package com.example.android.play_api;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.suke.widget.SwitchButton;

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText name;
    private Spinner sizes,langs;
    private com.suke.widget.SwitchButton switchButton;
    private TextView mode,save;
    private ImageView close;
    int pos1,pos2;
    String username,size,language,theme;

    String sizevals[] = new String[] {"Small","Medium","Large"};
    String languages[] = new String[] {"French","Igbo","Spanish","English","Hausa","Yoruba"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);

        init_data();

        name = (EditText) findViewById(R.id.name);
        name.setText(getName());

        sizes = (Spinner) findViewById(R.id.sizes);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sizes, R.layout.spinner_text);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        sizes.setAdapter(adapter);

        sizes.setSelection(pos1);
        sizes.setOnItemSelectedListener(this);

        langs = (Spinner) findViewById(R.id.langs);
        ArrayAdapter<CharSequence> langadapter = ArrayAdapter.createFromResource(this,
                R.array.languages, R.layout.spinner_text);
        langadapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        langs.setAdapter(langadapter);
        langs.setSelection(pos2);
        langs.setOnItemSelectedListener(this);

        mode = (TextView) findViewById(R.id.mode);
        mode.setText(theme);
        switchButton = (SwitchButton) findViewById(R.id.toggle);
        if(theme.equals("Light"))
        {
            switchButton.setChecked(true);
        }
        else if(theme.equals("Dark"))
        {
            switchButton.setChecked(false);
        }
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                if(isChecked)
                {
                    mode.setText("Light");
                    theme = "Light";
                }
                else
                {
                    mode.setText("Dark");
                    theme = "Dark";
                }
            }
        });

        save = (TextView) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_info();
                startActivity(new Intent(SettingActivity.this,MainhomeActivity.class));
            }
        });

        close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.this.finish();
            }
        });
    }

    private void save_info()
    {
        username = name.getText().toString();
        Toast.makeText(SettingActivity.this,username +" : "+language+" : "+theme+" : "+size,Toast.LENGTH_SHORT ).show();
        SharedPreferences pref =  getApplicationContext().getSharedPreferences("PLAY_API",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Name",username);
        editor.putString("Language",language);
        editor.putString("Theme",theme);
        editor.putString("Size",size);
        editor.apply();

        Toast.makeText(SettingActivity.this,"Changes Applied Successfully!",Toast.LENGTH_SHORT).show();
    }

    private String getName() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("PLAY_API",MODE_PRIVATE);
        return prefs.getString("Name","User");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId()== R.id.sizes) {
            sizes.setSelection(i);
            size =sizevals[i];
        }
        else if(adapterView.getId() == R.id.langs)
        {
            langs.setSelection(i);
            language = languages[i];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void init_data()
    {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("PLAY_API",MODE_PRIVATE);
        theme = prefs.getString("Theme","Light");
        size = prefs.getString("Size","Medium");
        language = prefs.getString("Language","English");



        for (int i=0;i<sizevals.length;i++)
        {
            if(sizevals[i].equals(size))
            {
                pos1 = i;
            }
        }


        for (int i=0;i<languages.length;i++)
        {
            if(languages[i].equals(language))
            {
                pos2 = i;
            }
        }
    }
}
