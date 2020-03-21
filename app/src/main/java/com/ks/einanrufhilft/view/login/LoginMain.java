package com.ks.einanrufhilft.view.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.ks.einanrufhilft.R;

public class LoginMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        ViewPager viewPager1 = findViewById(R.id.viewpager);
        viewPager1.setAdapter(new CustomPagerAdapter(this));
    }
}
