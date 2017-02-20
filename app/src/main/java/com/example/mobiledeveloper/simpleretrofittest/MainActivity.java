package com.example.mobiledeveloper.simpleretrofittest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ProgressBar progressBar;
    ArrayAdapter<Zone> arrayAdapter;
    TextView errorView;
    Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        errorView = (TextView) findViewById(R.id.errorView);

        arrayAdapter =
                new ArrayAdapter<Zone>(this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new ArrayList<Zone>());
        listView.setAdapter(arrayAdapter);

        controller = new Controller(this);
        controller.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_load) {
            progressBar.setVisibility(View.VISIBLE);
            errorView.setVisibility(GONE);
            listView.setVisibility(GONE);

            controller.restart();
        } else if (item.getItemId() == R.id.action_stop) {
            progressBar.setVisibility(View.GONE);
            errorView.setVisibility(View.GONE);
            listView.setVisibility(GONE);

            Toast.makeText(this, "Loading Cancelled", Toast.LENGTH_SHORT).show();

            controller.stop();
        }

        return true;
    }

    public void success(List<Zone> zones) {
        progressBar.setVisibility(GONE);
        errorView.setVisibility(GONE);
        listView.setVisibility(View.VISIBLE);

        arrayAdapter.clear();
        arrayAdapter.addAll(zones);
    }

    public void fail(String errorMsg) {
        progressBar.setVisibility(GONE);
        errorView.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);

        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
