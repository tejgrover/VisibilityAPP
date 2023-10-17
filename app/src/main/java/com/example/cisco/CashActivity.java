package com.example.cisco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CashActivity extends AppCompatActivity {


    //Recyclerview
    RecyclerView dataentry;
    CashdataAdapter adapter;

    //drop down menu
    String[] item = {"FY24","FY25","FY26","FY27"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    //navigation
    DrawerLayout drawerLayout;
    ImageButton menu;
    LinearLayout goal_attainment,order, payment,cash,export,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);

        //RecyclerView
        dataentry = findViewById(R.id.dataentry);
        setRecyclerView();


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
                redirectActivity(CashActivity.this,MainActivity.class);
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(CashActivity.this,OrderActivity.class);
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(CashActivity.this,PaymentActivity.class);
            }
        });
        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(CashActivity.this,ExportActivity.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CashActivity.this,"Logout",Toast.LENGTH_SHORT).show();
            }
        });


        //drop down menu
        autoCompleteTextView=findViewById(R.id.auto_complete_text2);
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,item);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(CashActivity.this,"Item: "+ item,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //recyclerview
    private void setRecyclerView() {
        dataentry.setHasFixedSize(true);
        dataentry.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CashdataAdapter(this,getList());
        dataentry.setAdapter(adapter);
    }

    private List<CashdataModel> getList(){
        List<CashdataModel> order_list = new ArrayList<>();
        order_list.add(new CashdataModel("PRD & SVC","1.15B","127.632.298.14","127.632.298.14"));
        order_list.add(new CashdataModel("PRD & SVC","1.15B","127.632.298.14","127.632.298.14"));
        order_list.add(new CashdataModel("PRD & SVC","1.15B","127.632.298.14","127.632.298.14"));
        order_list.add(new CashdataModel("PRD & SVC","1.15B","127.632.298.14","127.632.298.14"));
        order_list.add(new CashdataModel("PRD & SVC","1.15B","127.632.298.14","127.632.298.14"));
        return order_list;
    }


    //navigation
    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public static void redirectActivity(Activity activity, Class secondActivity){
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
}