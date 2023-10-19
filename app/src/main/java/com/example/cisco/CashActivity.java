package com.example.cisco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CashActivity extends AppCompatActivity {


    //Recyclerview
    RecyclerView dataentry;
    CashdataAdapter adapter;

    // database
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private Connection connecton;

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

        //database
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        //RecyclerView
        dataentry = findViewById(R.id.dataentry);

        //navigation
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
//                Toast.makeText(CashActivity.this,"Item: "+ item,Toast.LENGTH_SHORT).show();
                setRecyclerView();
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


    //database
    private ArrayList<CashdataModel> getList(){
        ArrayList<CashdataModel> order_list = new ArrayList<>();
        connecton = buttonConnectToOracleDB();
        try {
            if (connecton!=null) {
                Statement statement = connecton.createStatement();
                ResultSet resultSet = statement.executeQuery("select employee_id,plan_id,plan_title,YTD_PAID from APPS.e2e_calc_payment_detail where employee_id='455284' and plan_title='2022 CSP04' and plan_id='421006' and period_code='FY23P2' order by period_code desc");
                while (resultSet.next()) {
                    String category = resultSet.getString(1 );
                    String goal = resultSet.getString(2 );
                    String booking = resultSet.getString(3 );
                    String backlog = resultSet.getString(4 );
                    order_list.add(new CashdataModel(category, goal,booking,backlog));
                }
            }
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return order_list;
    }


    //database
    public Connection buttonConnectToOracleDB() {
        try {
            Class.forName(DRIVER);
            this.connecton = DriverManager.getConnection("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=dbs-pdv-vm-2020.cisco.com)(PORT=1576))(CONNECT_DATA=(SERVICE_NAME=DV1G2C_SRVC_OTH.cisco.com)(Server=Dedicated)))", "APPS", "B1UE2UTH");
            Toast.makeText(this, "CONNECTED", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return connecton;
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