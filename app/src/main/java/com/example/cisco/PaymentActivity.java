package com.example.cisco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
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

public class PaymentActivity extends AppCompatActivity {

    // database
    private Button btn;
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private Connection connecton;
    private EditText editText;
    public String UserId;
    private TextView paymentdate,totalpayment;



    //navigation
    DrawerLayout drawerLayout;
    ImageButton menu;
    LinearLayout goal_attainment,order, payment,cash,export,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        //database
        editText=findViewById(R.id.userid);
        paymentdate = findViewById(R.id.paymentdate);
        totalpayment = findViewById(R.id.totalpayment);
        btn = findViewById(R.id.useridbtn);
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connecton = buttonConnectToOracleDB();
                UserId =editText.getText().toString();
                try {
                    if (connecton != null) {
                        Statement statement = connecton.createStatement();
                        ResultSet resultSet = statement.executeQuery("select GS_END_DATE,GOAL_VALUE from APPS.e2e_calc_payment_detail where employee_id='"+ UserId +"' and rate_factor='1.64182' order by period_code desc");
                        while (resultSet.next()) {
                            String datetime = resultSet.getString(1);
                            String date = datetime.split(" ")[0];
                            paymentdate.setText(date);
                            totalpayment.setText(resultSet.getString(2));
                        }
                    }
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
            }
        });

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
                redirectActivity(PaymentActivity.this,MainActivity.class);
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(PaymentActivity.this,OrderActivity.class);
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(PaymentActivity.this,CashActivity.class);
            }
        });
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(PaymentActivity.this,ExportActivity.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PaymentActivity.this,"Logout",Toast.LENGTH_SHORT).show();
            }
        });
    }

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


    //database
    public Connection buttonConnectToOracleDB() {
        try {
            Class.forName(DRIVER);
            this.connecton = DriverManager.getConnection("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=dbs-pdv-vm-2020.cisco.com)(PORT=1576))(CONNECT_DATA=(SERVICE_NAME=DV1G2C_SRVC_OTH.cisco.com)(Server=Dedicated)))", "APPS", "B1UE2UTH");

            Toast.makeText(this, "CONNECTED", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            paymentdate.setText(e.toString());
        }

        return connecton;
    }

}