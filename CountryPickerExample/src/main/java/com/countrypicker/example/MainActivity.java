package com.countrypicker.example;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;

import com.countrypicker.CountryPicker;
import com.countrypicker.CountryPickerListener;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.countrypickerexample.R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        CountryPicker picker = new CountryPicker();
        picker.setListener(new CountryPickerListener() {

            @Override
            public void onSelectCountry(String name, String code) {
                Toast.makeText(
                        MainActivity.this,
                        "Country Name: " + name + " - Code: " + code,
                        Toast.LENGTH_SHORT).show();
            }
        });

        transaction.replace(com.countrypickerexample.R.id.home, picker);

        transaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.countrypickerexample.R.menu.main, menu);
        MenuItem item = menu.findItem(com.countrypickerexample.R.id.show_dialog);
        item.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CountryPicker picker = CountryPicker.newInstance("Select Country");
                picker.setListener(new CountryPickerListener() {

                    @Override
                    public void onSelectCountry(String name, String code) {
                        Toast.makeText(
                                MainActivity.this,
                                "Country Name: " + name + " - Code: " + code,
                                Toast.LENGTH_SHORT).show();
                    }
                });

                picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
                return false;
            }
        });
        return true;
    }
}
