package com.example.android.play_api;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class ReadActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarLayout appBarLayout;
    LinearLayout temporary,movable;
    LinearLayout speechlay;
    FrameLayout main_layout;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggler;
    private NestedScrollView scroller;
    private TextView date,title,verse,verse_desc,content,prayer,bible_vers;
    private FloatingActionButton fab;
    private CardView popup;
    private int status = 0;
    private Dialog verse_popup;
    TextToSpeech textToSpeech;
    TextView readcontent;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        android.support.v7.widget.Toolbar tools = findViewById(R.id.toolbar);
        setSupportActionBar(tools);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);

        for (int i = 0; i < tools.getChildCount(); i++) {
            if(tools.getChildAt(i) instanceof ImageButton){
                tools.getChildAt(i).setScaleX(1.5f);
                tools.getChildAt(i).setScaleY(1.5f);
            }
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        toggler = new ActionBarDrawerToggle(this,drawer,R.string.open,R.string.close);
        drawer.addDrawerListener(toggler);
        toggler.syncState();
        scroller = (NestedScrollView) findViewById(R.id.scroller);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setEnabled(false);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.name);
        String nameText = getName()+"!";
        name.setText(nameText);
        item fromintent = (item) getIntent().getSerializableExtra("ITEM");
        init_data(fromintent);
        speachText();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ReadActivity.this,"speaker fab",Toast.LENGTH_SHORT).show();
                speak();
            }
        });

        appBarLayout = (AppBarLayout)findViewById(R.id.app_bar);
         main_layout = (FrameLayout)findViewById(R.id.main_layout);
         temporary = (LinearLayout) findViewById(R.id.temporary);
         movable = (LinearLayout)findViewById(R.id.movable);
        speechlay = (LinearLayout)findViewById(R.id.listner);
        readcontent = findViewById(R.id.content);

        speechlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });


        ViewTreeObserver vto = movable.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    movable.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    movable.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width  = movable.getMeasuredWidth();
                int height = movable.getMeasuredHeight();

                temporary.setPadding(0,height,0,0);

            }
        });

       /* verse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status == 0) {
                    movable.setVisibility(View.INVISIBLE);
                    verse_desc.setVisibility(View.INVISIBLE);
                    popup.setVisibility(View.VISIBLE);
                    status =1;
                }
                else
                {
                    movable.setVisibility(View.VISIBLE);
                    verse_desc.setVisibility(View.VISIBLE);
                    popup.setVisibility(View.GONE);
                    status = 0;
                }
            }
        });*/

       verse.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               verse_popup.show();
           }
       });





        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {


                float percent = ((float)Math.abs(i)/appBarLayout.getTotalScrollRange());
                temporary.setAlpha(1-percent);

                int left = (int)Math.floor(86*percent);
                int top = (int)Math.floor(180*percent);


                movable.setPadding(left,top,0,0);

                int bottom = 6 + (int)Math.floor(22*(1-percent));
                main_layout.setPadding(0,0,0,bottom );

                if(Math.abs(i) == appBarLayout.getTotalScrollRange()){

                    // fader.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.VISIBLE);
                    fab.setEnabled(true);
                }
                else if(i==0)
                {
                    //fader.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.INVISIBLE);
                    fab.setEnabled(false);
                }
                else
                {
                    //fader.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.INVISIBLE);
                    fab.setEnabled(false);
                }


            }
        });
    }


    private void init_data(item intent) {
        //date,title,verse,verse_desc,content,prayer
        date = (TextView) findViewById(R.id.date);
        title = (TextView) findViewById(R.id.title);
        verse = (TextView) findViewById(R.id.verse);
        verse_desc = (TextView) findViewById(R.id.verse_desc);
        content = (TextView) findViewById(R.id.content);
        prayer = (TextView) findViewById(R.id.prayertext);

        date.setText(get_date(intent.getDate()));
        title.setText(intent.getTitle());
        String ver = intent.getBible();
        verse.setText(ver.split("\n")[0]);
        String vert = get_ver(ver.split("\n")[1]);
        verse_desc.setText(vert);
        Spannable text = new SpannableString(intent.getDevotion());
        text.setSpan(new RelativeSizeSpan(2f),0,1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        content.setText(text);
        String pray = intent.getPrayer();
        int len  = pray.length();
        if(pray.charAt(len-1)=='\n')
        {
            pray = pray.substring(0,len-2)+"";
        }
        prayer.setText(pray);
        String bib = intent.getBible();
        int l  = bib.length();
        if(bib.charAt(l-1)=='\n')
        {
            bib = bib.substring(0,l-2)+"";
        }

        verse_popup = new Dialog(this);
        verse_popup.setContentView(R.layout.bible_verse);

        TextView bib_vers = (TextView) verse_popup.findViewById(R.id.bible_desc);
        bib_vers.setText(bib);
        verse_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }

    private String get_ver(String ver) {

        String tokens[] = ver.split(" ");

        if(tokens.length<=15)
        {
            return ver+"...";
        }
        else
        {
            String sent = "";
            for(int i=0;i<15;i++)
            {
                sent = sent+tokens[i]+" ";
            }

            sent = sent+"...";
            return sent;
        }
    }

    public String get_date(Date date)
    {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTime(date);

        int currentDay = localCalendar.get(Calendar.DATE);
        int currentMonth = localCalendar.get(Calendar.MONTH) + 1;
        int currentYear = localCalendar.get(Calendar.YEAR);
        int currentDayOfWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
        String[] days = {"Sun.","Mon.","Tue.","Wed.","Thu.","Fri.","Sat"};
        String[] months ={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"};

        String string = days[currentDayOfWeek-1]+" "+currentDay+" "+months[currentMonth-1]+" "+currentYear;
        return string;
    }


    public int get_px(int dp)
    {
        Resources r = getApplicationContext().getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,r.getDisplayMetrics());

        return px;
    }

    private String getName() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("PLAY_API",MODE_PRIVATE);
        return prefs.getString("Name","User");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(toggler.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if(id == R.id.about)
        {
            startActivity(new Intent(ReadActivity.this,MainsplashActivity.class));
            drawer.closeDrawers();
        }
        else if(id == R.id.prayer)
        {
            startActivity(new Intent(ReadActivity.this,PrayerActivity.class));
            drawer.closeDrawers();
        }
        else if(id== R.id.devotion)
        {
            startActivity(new Intent(ReadActivity.this,HomeActivity.class));
            drawer.closeDrawers();
        }

        return false;
    }



    private void speachText(){
        //text to speech

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        speechlay.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

    }
    private void speak() {
        String text = readcontent.getText().toString() ;
        float pitch = (float) 1.0;
        if (pitch < 0.1) pitch = 0.1f;
        float speed = (float) 1.0;
        if (speed < 0.1) speed = 0.1f;

        textToSpeech.setPitch(pitch);
        textToSpeech.setSpeechRate(speed);

        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        super.onDestroy();
    }
}
