package com.example.cisco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //drop down menu
    String[] item = {"2024 CS538","2024 CS539","2024 CS540","2024 CS541"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    //navigation drawer
    DrawerLayout drawerLayout;
    ImageButton menu;

    LinearLayout goal_attainment,order, payment,cash,export,logout;

    //graph
    BarChart stackedChart;
    int[] colorClassArray = new int[]{Color.rgb(7,144,174),Color.rgb(66,137,203),Color.rgb(219,99,31),Color.rgb(35,121,70),Color.rgb(61,173,86)};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //custom image for action bar start
        // androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        // actionBar.setDisplayShowCustomEnabled(true);
        // LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // View view = inflater.inflate(R.layout.custom_logo,null);
        //actionBar.setCustomView(view);


        //Navigation Drawer
        drawerLayout = findViewById(R.id.drawerlayout);
        menu = findViewById(R.id.menu);
        goal_attainment = findViewById(R.id.goalattainment);
        order = findViewById(R.id.ordersearch);
        payment = findViewById(R.id.payments);
        cash = findViewById(R.id.cash);
        export = findViewById(R.id.export);
        logout = findViewById(R.id.logoutbtn);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });
        goal_attainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this,OrderActivity.class);
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this,PaymentActivity.class);
            }
        });
        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this,CashActivity.class);
            }
        });
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this,ExportActivity.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Logout",Toast.LENGTH_SHORT).show();
            }
        });



        //drop down menu
        autoCompleteTextView=findViewById(R.id.auto_complete_text);
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,item);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this,"Item: "+ item,Toast.LENGTH_SHORT).show();
            }
        });


        //graph chart
        stackedChart = findViewById(R.id.stacked_BarChart);
        BarDataSet barDataSet = new BarDataSet(dataValues1(),"Bar Set");
        barDataSet.setColors(colorClassArray);
        barDataSet.setStackLabels(new String[]{"Goal","Booking","Remaining to Goal", "Revenue","Backing"});

        BarData barData = new BarData(barDataSet);
        stackedChart.setData(barData);
        stackedChart.animateY(3000);


    }

    //Navigatiin Drawer
    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public static void redirectActivity(Activity activity,Class secondActivity){
        Intent intent = new Intent(activity,secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    // Graph data
    private ArrayList<BarEntry> dataValues1(){
        ArrayList<BarEntry> dataVals = new ArrayList<>();
        dataVals.add(new BarEntry(0,new float[]{11,0,0,0,0}));
        dataVals.add(new BarEntry(1,new float[]{0,2,8f,0,0}));
        dataVals.add(new BarEntry(2,new float[]{0,0,0,0.6f,0.6f}));
        dataVals.add(new BarEntry(3.3f,new float[]{1.5f,0,0,0,0}));
        dataVals.add(new BarEntry(4.3f,new float[]{0,0.5f,1,0,0}));
        dataVals.add(new BarEntry(5,new float[]{0,0,0,0,0}));
        return dataVals;
    }

}