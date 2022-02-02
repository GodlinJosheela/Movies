package com.gjrs.greedygame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.gjrs.greedygame.databinding.ActivityMainBinding;
import com.gjrs.greedygame.ui.main.MovieBagMainActivity;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.movieBag.setOnClickListener(view1 -> {
            Intent intent = new Intent(MainActivity.this, MovieBagMainActivity.class);
            startActivity(intent);
        });
    }
}