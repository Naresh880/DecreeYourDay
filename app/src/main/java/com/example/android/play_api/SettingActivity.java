package com.example.android.play_api;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText name;
    private Spinner sizes,langs;
    private com.suke.widget.SwitchButton switchButton;
    private TextView mode,save;
    private ImageView close;
    private CircleImageView profile;
    private FloatingActionButton edit;
    private static final int PICK_IMAGE = 1;
    Uri imageuri;
    int pos1,pos2;
    String path;
    private int ischanged = 0;
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


        profile = (CircleImageView) findViewById(R.id.profileImage);

        edit = (FloatingActionButton) findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"Select Profile Picture"),PICK_IMAGE);
            }
        });

        if(!(getProfile().equals("None")))
        {
            loadImageFromStorage(getProfile());
        }
    }

    private void save_info()
    {
        username = name.getText().toString();
        SharedPreferences pref =  getApplicationContext().getSharedPreferences("PLAY_API",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Name",username);
        editor.putString("Language",language);
        editor.putString("Theme",theme);
        editor.putString("Size",size);
        if(ischanged==1)
        {
            saveImagePath(path);
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK)
        {
            imageuri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
                path = saveToInternalStorage(bitmap);
                ischanged = 1;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            profile.setImageBitmap(bitmapImage);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            profile.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    private String getProfile() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("PLAY_API",MODE_PRIVATE);
        return prefs.getString("ImagePath","None");
    }

    void saveImagePath(String path) {
        SharedPreferences pref =  getApplicationContext().getSharedPreferences("PLAY_API",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ImagePath",path);
        editor.apply();
    }
}
