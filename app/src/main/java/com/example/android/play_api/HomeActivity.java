package com.example.android.play_api;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.audiofx.Equalizer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.play_api.Adapters.ImageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RecyclerAdapter.OnItemClickListener {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggler;
    private LinearLayout read;
    private LinearLayout listen;
    private TextView date;
    private Button donate;
    private ImageView donate_img;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private AppBarLayout appBarLayout;
    private NestedScrollView scroller;
    private CardView cardView;
    private LinearLayout previous;
    Dialog calendar_dialog;
    private TextView speechtitle;
    private TextView speechtitlesynopsis;
    private TextToSpeech textToSpeech;
    String Language;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    private static final String URL = "https://dyd-njs.herokuapp.com/getDevotions";

    int image[]={R.drawable.prayerhome,R.drawable.pray};

    private List<item> itemList = new ArrayList<item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        android.support.v7.widget.Toolbar tools = findViewById(R.id.toolbar);
        setSupportActionBar(tools);
        getSupportActionBar().setTitle(null);
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

        donate = (Button) findViewById(R.id.donate_btn);
        donate_img = (ImageView) findViewById(R.id.donate_img);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        read = (LinearLayout) findViewById(R.id.read);
        listen = (LinearLayout)findViewById(R.id.listen);
        speechtitle = (TextView) findViewById(R.id.title);
        speechtitlesynopsis = (TextView) findViewById(R.id.synopsis);
        date = (TextView) findViewById(R.id.date);
        cardView = (CardView) findViewById(R.id.card);
        previous = (LinearLayout) findViewById(R.id.previous_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        toggler = new ActionBarDrawerToggle(this,drawer,R.string.open,R.string.close);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        drawer.addDrawerListener(toggler);
        toggler.syncState();
        scroller = (NestedScrollView) findViewById(R.id.scroller);
        scroller.setFadingEdgeLength(60);

        SharedPreferences myPrefs;
        myPrefs = getSharedPreferences("PLAY_API",MODE_PRIVATE);
      Language=myPrefs.getString("Language", "");

       /* final ViewPager viewPager = findViewById(R.id.viewPager);
        ImageAdapter adapter = new ImageAdapter(this);
        viewPager.setAdapter(adapter);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                viewPager.post(new Runnable(){

                    @Override
                    public void run() {
                        viewPager.setCurrentItem((viewPager.getCurrentItem()+1)%ImageAdapter.mImageIds.length);
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 3000, 3000);*/
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.name);
        String nameText = getName()+"!";
        name.setText(nameText);

        init_data();

        speachText();
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,ReadActivity.class));
            }
        });

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });



        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

                float percent = ((float)Math.abs(i)/appBarLayout.getTotalScrollRange());

                int top = (int)Math.floor(65*percent);
                cardView.setAlpha(((1-percent)<0.8)?(1-percent):1);

                previous.setPadding(0,top,0,0);

                if(Math.abs(i) == appBarLayout.getTotalScrollRange()){

                   // fader.setVisibility(View.INVISIBLE);
                    recyclerView.setNestedScrollingEnabled(true);
                }
                else if(i==0)
                {
                    //fader.setVisibility(View.INVISIBLE);
                    recyclerView.setNestedScrollingEnabled(false);
                }
                else
                {
                    //fader.setVisibility(View.VISIBLE);
                    recyclerView.setNestedScrollingEnabled(false);
                }
            }
        });

        date.setText(get_date());

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this,"Donate is clicked",Toast.LENGTH_SHORT).show();
            }
        });

        donate_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this,"Donate image is clicked",Toast.LENGTH_SHORT).show();
            }
        });



    }

    private String getName() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("PLAY_API",MODE_PRIVATE);
        return prefs.getString("Name","User");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id==R.id.calendar)
        {
            calendar_dialog = new Dialog(this);

            calendar_dialog.setContentView(R.layout.calendar_popup);
            calendar_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            calendar_dialog.show();
        }

        if(toggler.onOptionsItemSelected(item))
        {
            return true;
        }



        return super.onOptionsItemSelected(item);



    }

    public String get_date()
    {
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());

        int currentDay = localCalendar.get(Calendar.DATE);
        int currentMonth = localCalendar.get(Calendar.MONTH) + 1;
        int currentYear = localCalendar.get(Calendar.YEAR);
        int currentDayOfWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
        String[] days = {"Sun.","Mon.","Tue.","Wed.","Thu.","Fri.","Sat"};
        String[] months ={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"};

        String string = days[currentDayOfWeek-1]+" "+currentDay+" "+months[currentMonth-1]+" "+currentYear;
        return string;
    }

    public void init_data()
    {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Data....");
        dialog.show();
        final String[] months ={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"};
        Toast.makeText(HomeActivity.this,"came into init",Toast.LENGTH_SHORT).show();
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    Toast.makeText(HomeActivity.this,"response shown",Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("data");

                    for(int i=0;i<array.length();i++)
                    {
                        JSONObject obj = array.getJSONObject(i);
                        String dat = obj.getString("quote_date");
                        String title = obj.getString("topic");
                        String devotion = obj.getString("devotion");
                        String verse = obj.getString("bible_verse");
                        String prayer = obj.getString("prayer");

                        String[] dates = dat.split("-");
                        int ind = Integer.parseInt(dates[1]);

                        Date adder = new SimpleDateFormat("yyyy-MM-dd").parse(dat);
                        itemList.add(new item(months[ind-1],Integer.parseInt(dates[2]),title,adder,verse,devotion,prayer));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this,"there is a json error",Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Collections.sort(itemList, new Comparator<item>() {
                    @Override
                    public int compare(item t1, item t2) {
                        return t2.getDate().compareTo(t1.getDate());
                    }
                });
                recyclerView.setLayoutManager(layoutManager);
                adapter = new RecyclerAdapter(itemList);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(HomeActivity.this);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        MyRequestQueue.add(request);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if(id == R.id.about)
        {
            startActivity(new Intent(HomeActivity.this,MainsplashActivity.class));
            drawer.closeDrawers();
        }
        else if(id == R.id.prayer)
        {
            startActivity(new Intent(HomeActivity.this,PrayerActivity.class));
            drawer.closeDrawers();
        }
        else if(id == R.id.settings)
        {
            startActivity(new Intent(HomeActivity.this,SettingActivity.class));
            drawer.closeDrawers();
        }
        else if(id== R.id.devotion)
        {
            drawer.closeDrawers();
        }
        return false;
    }



    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(HomeActivity.this,ReadActivity.class);
        intent.putExtra("ITEM",itemList.get(position));
        startActivity(intent);
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
                        listen.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

    }
    private void speak() {
        String text = speechtitle.getText().toString() + speechtitlesynopsis.getText().toString();
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
