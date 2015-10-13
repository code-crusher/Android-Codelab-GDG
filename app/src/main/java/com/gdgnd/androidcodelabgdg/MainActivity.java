package com.gdgnd.androidcodelabgdg;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private TextInputLayout firstNameLayout, secondNameLayout;
    private EditText firstName, surName;
    private FloatingActionButton fab;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        firstNameLayout = (TextInputLayout) findViewById(R.id.firstNameLayout);
        secondNameLayout = (TextInputLayout) findViewById(R.id.secondNameLayout);
        firstName = (EditText) findViewById(R.id.firstName);
        surName = (EditText) findViewById(R.id.surName);

        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Contact"));
        tabLayout.addTab(tabLayout.newTab().setText("Message"));
        tabLayout.addTab(tabLayout.newTab().setText("Images"));

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeValues(view);
            }
        });

        setStoredValues();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fab.show();
                if (surName.getText().toString().trim().length() > 0) {
                    secondNameLayout.setError("");
                }
                if (firstName.getText().toString().trim().length() > 0) {
                    firstNameLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        firstName.addTextChangedListener(textWatcher);
        surName.addTextChangedListener(textWatcher);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        Toast.makeText(getApplicationContext(), "Current selection : " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private void storeValues(View view) {
        if (firstName.getText().toString().trim().length() == 0) {
            firstNameLayout.setError("First Name is required*");
            return;
        } else if (surName.getText().toString().trim().length() == 0) {
            secondNameLayout.setError("Surname is required*");
            return;
        } else {
            SharedPreferences prefs = this.getSharedPreferences("stored", MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("firstname", firstName.getText().toString());
            edit.putString("surname", surName.getText().toString());
            edit.apply();
            Snackbar.make(view, "Details Saved!", Snackbar.LENGTH_LONG)
                    .setAction("Clear", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clearValues();
                            setStoredValues();
                        }
                    }).show();
        }
    }

    private void setStoredValues() {
        SharedPreferences prefs = this.getSharedPreferences("stored", MODE_PRIVATE);
        firstName.setText(prefs.getString("firstname", ""));
        surName.setText(prefs.getString("surname", ""));
    }

    private void clearValues() {
        SharedPreferences prefs = this.getSharedPreferences("stored", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("firstname", "");
        edit.putString("surname", "");
        edit.putString("address", "");
        edit.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
