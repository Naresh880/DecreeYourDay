package com.example.android.play_api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class IntroActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Button next,back;
    private TextView swipe;
    private LinearLayout dotLayout;
    private sliderAdapter adapter;
    private TextView[] dots;
    private String username;
    private NumberPicker picker;
    private String language;

    private static final String FILENAME = "name.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        if(!isFirstTime())
        {
            startHomeActivity();
        }

        next = (Button) findViewById(R.id.next_btn);
        back = (Button) findViewById(R.id.back_btn);
        swipe = (TextView) findViewById(R.id.swipe_text);
        dotLayout = (LinearLayout) findViewById(R.id.dot_layout);
        back.setVisibility(View.INVISIBLE);
        back.setEnabled(false);
        next.setEnabled(false);
        next.setVisibility(View.INVISIBLE);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new sliderAdapter(getApplicationContext());
        viewPager.setAdapter(adapter);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pageid = viewPager.getCurrentItem()+1;
                if(pageid<3)
                {
                    viewPager.setCurrentItem(pageid);
                }
                else
                {
                    startHomeActivity();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pageid = viewPager.getCurrentItem();
                if(pageid>0)
                {
                    viewPager.setCurrentItem(pageid-1);
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                if(i==0)
                {
                    back.setText("Back");
                    next.setEnabled(false);
                    next.setVisibility(View.INVISIBLE);
                    back.setEnabled(false);
                    back.setVisibility(View.INVISIBLE);
                    swipe.setVisibility(View.VISIBLE);
                }
                else if(i==1)
                {
                    back.setText("Back");
                    back.setVisibility(View.VISIBLE);
                    back.setEnabled(true);
                    next.setEnabled(true);
                    next.setVisibility(View.VISIBLE);
                    next.setText("Next");
                    swipe.setVisibility(View.INVISIBLE);
                }
                else if(i==2)
                {
                    next.setText("Finish");
                    next.setEnabled(true);
                    next.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);
                    back.setEnabled(true);
                    swipe.setVisibility(View.INVISIBLE);
                }

                setDots(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        setDots(0);

    }



    private boolean isFirstTime()
    {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("PLAY_API",MODE_PRIVATE);
        return prefs.getBoolean("FirstTimeStartFlag",true);
    }

    private void setFirsttimeFlag(boolean start)
    {
        SharedPreferences ref = getApplicationContext().getSharedPreferences("PLAY_API",MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putBoolean("FirstTimeStartFlag",start);
        editor.commit();
    }

    private void setDots(int pagenumber)
    {
        dotLayout.removeAllViews();
        dots = new TextView[3];
        for (int i=0;i<3;i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(Color.parseColor("#ffffff"));
            dotLayout.addView(dots[i]);
        }

        if(dots.length > 0)
        {
            dots[pagenumber].setTextSize(40);
            dots[pagenumber].setTextColor(Color.parseColor("#f76b1c"));
        }

    }

    public void startHomeActivity()
    {
        setFirsttimeFlag(false);
        saveLanguage(language);
        startActivity(new Intent(IntroActivity.this,MainhomeActivity.class));
        finish();
    }


    public class sliderAdapter extends PagerAdapter{

        int[] layouts = {R.layout.slider1,R.layout.slider2,R.layout.slider3};
        LayoutInflater inflater;

        private Context context;

        public sliderAdapter(Context cont)
        {
            this.context = cont;
        }
        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return (view == (LinearLayout)o);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.slider1,container,false);
            if(position==0)
            {
                View one = inflater.inflate(R.layout.slider1,container,false);
                TextView text  = (TextView) one.findViewById(R.id.hellos);
                String[] texts = {"Hello","Hola","Salut","Sannu","Nnọọ","Pẹlẹ o","Hello"};
                animateText(text,texts,0,false);
                container.addView(one);
                v =  one;
            }
            else if(position==1)
            {
                View two = inflater.inflate(R.layout.slider2,container,false);
                final EditText name = (EditText) two.findViewById(R.id.name_text);
                TextView save = (TextView) two.findViewById(R.id.save_btn);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        username = name.getText().toString();
                        savename(username);
                        Toast.makeText(IntroActivity.this,"Saved",Toast.LENGTH_SHORT).show();

                    }
                });

                container.addView(two);
                v= two;
            }
            else if(position==2)
            {
                View three = inflater.inflate(R.layout.slider3,container,false);

                picker = (NumberPicker) three.findViewById(R.id.numberPicker);
                final String[] pickerVals  = new String[] {"French","Igbo","Spanish","English","Hausa","Yoruba"};

                picker.setDisplayedValues(pickerVals);
                picker.setMinValue(0);
                picker.setMaxValue(5);
                language = "English";
                picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker pickers, int oldVal, int newVal) {
                        int pos = picker.getValue();
                        language = pickerVals[pos];
                        Toast.makeText(IntroActivity.this,pickerVals[pos],Toast.LENGTH_SHORT).show();
                    }
                });


                container.addView(three);
                v = three;
            }

            return v;
        }


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

            container.removeView((LinearLayout)object);
        }
    }



    void savename(String name)
    {
        SharedPreferences pref =  getApplicationContext().getSharedPreferences("PLAY_API",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Name",name);
        editor.commit();

    }

     void saveLanguage(String language) {
         SharedPreferences pref =  getApplicationContext().getSharedPreferences("PLAY_API",MODE_PRIVATE);
         SharedPreferences.Editor editor = pref.edit();
         editor.putString("Language",language);
         editor.commit();
    }

    void saveTheme() {
        SharedPreferences pref =  getApplicationContext().getSharedPreferences("PLAY_API",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Theme","Light");
        editor.putString("Size","Medium");
        editor.commit();
    }




    private void animateText(final TextView textView, final String texts[], final int textIndex, final boolean forever) {
        //textView <-- The View which displays the texts
        //texts[] <-- Holds R references to the texts to display
        //textIndex <-- index of the first text to show in texts[]
        //forever <-- If equals true then after the last text it starts all over again with the first text resulting in an infinite loop. You have been warned.

        int fadeInDuration = 1500; // Configure time values here
        int timeBetween = 1000;
        int fadeOutDuration = 1000;

        textView.setVisibility(View.VISIBLE);    //Visible or invisible by default - this will apply when the animation ends
        textView.setText(texts[textIndex]);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
        fadeIn.setDuration(fadeInDuration);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
        fadeOut.setStartOffset(fadeInDuration + timeBetween);
        fadeOut.setDuration(fadeOutDuration);

        AnimationSet animation = new AnimationSet(false); // change to false
        animation.addAnimation(fadeIn);
        if((texts.length-1) != textIndex) animation.addAnimation(fadeOut);
        animation.setRepeatCount(1);
        textView.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                if (texts.length - 1 > textIndex) {
                    animateText(textView, texts, textIndex + 1, forever); //Calls itself until it gets to the end of the array
                } else {
                    textView.setVisibility(View.VISIBLE);
                    if (forever == true) {
                        animateText(textView, texts, 0, forever);  //Calls itself to start the animation all over again in a loop if forever = true
                    } else {//do something when the end is reached}
                    }
                }
            }
            public void onAnimationRepeat (Animation animation){
                // TODO Auto-generated method stub
            }
            public void onAnimationStart (Animation animation){
                // TODO Auto-generated method stub
            }
            });

        }


}

