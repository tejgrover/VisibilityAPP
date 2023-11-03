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
import android.view.inputmethod.InputMethodManager;
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
    LinearLayout tablelayout, categorylayout;
    TextView noData;

    ArrayList<String> categories=new ArrayList<String>();


    // database
    private Button btn;
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private Connection connecton;
    private EditText editText;
    public String UserId;

    //drop down menu
    String[] item = {"2024","2023","2022","2021"};
    public String FY;
    AutoCompleteTextView autoCompleteTextView, categorydropview;
    ArrayAdapter<String> adapterItems;
    LinearLayout droplayout;


    //navigation
    DrawerLayout drawerLayout;
    ImageButton menu;
    LinearLayout goal_attainment,order, payment,cash,export,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);

        //database
        droplayout=findViewById(R.id.droplayout);
        editText=findViewById(R.id.userid1);
        btn = findViewById(R.id.useridbtn1);
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        btn.setOnClickListener(new View.OnClickListener() {

            private void hideKeyboard(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
            }
            @Override
            public void onClick(View view) {
                droplayout.setVisibility(View.VISIBLE);
                hideKeyboard(view);
            }
        });



        //RecyclerView
        dataentry = findViewById(R.id.dataentry);
        tablelayout = findViewById(R.id.tableid);
        categorylayout=findViewById(R.id.categorylayout);
        noData = findViewById(R.id.no_data);

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
                FY = adapterView.getItemAtPosition(i).toString();
//              Toast.makeText(CashActivity.this,"Item: "+ FY,Toast.LENGTH_SHORT).show();
                Toast.makeText(CashActivity.this,"Loading ...",Toast.LENGTH_SHORT).show();
//                setRecyclerView();
//                if(!getList().isEmpty()) {
//                    tablelayout.setVisibility(View.VISIBLE);
//                    noData.setVisibility(View.GONE);
//                }
//                else{
//                    tablelayout.setVisibility(View.INVISIBLE);
//                    noData.setVisibility(View.VISIBLE);
//                }

                setRecyclerView();
            }
        });
    }

    //recyclerview
    private void setRecyclerView() {
        dataentry.setHasFixedSize(true);
        dataentry.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new CashdataAdapter(this,getList());
        dataentry.setAdapter(new CashdataAdapter(this,getList()));
    }


    //database

    private ArrayList<CashdataModel> getList(){
        ArrayList<CashdataModel> order_list = new ArrayList<>();
        connecton = buttonConnectToOracleDB();
        UserId = editText.getText().toString();
        System.out.println(FY);
        try {
            if (connecton!=null) {
                Statement statement = connecton.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT                         xsgq.srp_goal_header_id      AS goalid,\n" +
                        "                               xsgq.quota_id                AS allocationid,\n" +
                        "                               xsgq.comp_plan_id            AS planid,\n" +
                        "                               xsgq.start_date              AS goalStratDate,\n" +
                        "                                  xsgq.comp_plan_title\n" +
                        "                               || '        '\n" +
                        "                               || TO_CHAR (xsgq.start_date, 'DD-Mon-YYYY')\n" +
                        "                               || ' to '\n" +
                        "                               || TO_CHAR (xsgq.end_date, 'DD-Mon-YYYY')\n" +
                        "                                  AS goalsheet,\n" +
                        "                               (SELECT attribute3\n" +
                        "                                  FROM apps.cn_quotas_all\n" +
                        "                                 WHERE quota_id = xsgq.quota_id)\n" +
                        "                                  AS plantitle,\n" +
                        "                               ROUND (xsgq.goal_value, 2)   AS goal,\n" +
                        "                               SUM (\n" +
                        "                                    xss.comm_revenue_amt\n" +
                        "                                  + xss.comm_erp_revenue_adj\n" +
                        "                                  + xss.comm_man_revenue_adj\n" +
                        "                                  + xss.achievement_adj)\n" +
                        "                                  AS revenueamount,\n" +
                        "                               (CASE\n" +
                        "                                   WHEN xcpe.my_payout_eligibility IN\n" +
                        "                                           ('REV_MULTI', 'BOTH')\n" +
                        "                                   THEN\n" +
                        "                                      SUM (\n" +
                        "                                           NVL (xss.comm_revenue_mul_amt, 0)\n" +
                        "                                         + NVL (xss.comm_erp_revenue_mul_adj, 0)\n" +
                        "                                         + NVL (xss.comm_man_revenue_mul_adj, 0)\n" +
                        "                                         + NVL (xss.achievement_mul_adj, 0))\n" +
                        "                                   ELSE\n" +
                        "                                      SUM (\n" +
                        "                                           xss.comm_revenue_amt\n" +
                        "                                         + xss.comm_erp_revenue_adj\n" +
                        "                                         + xss.comm_man_revenue_adj\n" +
                        "                                         + xss.achievement_adj)\n" +
                        "                                END)\n" +
                        "                                  AS revenueamountmul,\n" +
                        "                               NVL (\n" +
                        "                                    NVL (ROUND (SUM (xss.comm_backlog_amt), 2),\n" +
                        "                                         0)\n" +
                        "                                  + (SELECT NVL (\n" +
                        "                                               SUM (\n" +
                        "                                                    comm_booking_amt\n" +
                        "                                                  - comm_revenue_amt),\n" +
                        "                                               0)\n" +
                        "                                       FROM xxgdm.xxgdm_srp_summary_pub xssp,\n" +
                        "                                            apps.xxg2c_lookups      xlve,\n" +
                        "                                            apps.cn_quotas_all      cqa\n" +
                        "                                      WHERE     xsgq.employee_id =\n" +
                        "                                                   xssp.employee_id\n" +
                        "                                            AND xsgq.srp_goal_header_id =\n" +
                        "                                                   xssp.srp_goal_header_id\n" +
                        "                                            AND xsgq.quota_id = xssp.quota_id\n" +
                        "                                            AND xsgq.comp_plan_id =\n" +
                        "                                                   xssp.comp_plan_id\n" +
                        "                                            AND lookup_type =\n" +
                        "                                                   'XXG2CC_PE_CATEGORY'\n" +
                        "                                            AND cqa.attribute3 = xlve.lookup_code\n" +
                        "                                            AND cqa.quota_id = xssp.quota_id\n" +
                        "                                            AND NVL (xlve.attribute7, 'N') = 'N'\n" +
                        "                                            AND TO_NUMBER (cqa.attribute1) < 2022 --added by ashwitho as part of DSV change FY22\n" +
                        "                                            AND xssp.erp_pos_flag = 'P'), ---Added As part of Backlog change (PRB0048760) - nudayaku\n" +
                        "                                  0)\n" +
                        "                                  --                     NVL (\n" +
                        "                                  --                          ROUND (SUM (xss.comm_backlog_amt), 2)\n" +
                        "                                  --                        + (SELECT SUM (COMM_BOOKING_AMT - COMM_REVENUE_AMT)\n" +
                        "                                  --                             FROM xxgdm.xxgdm_srp_summary_pub xssp\n" +
                        "                                  --                            WHERE     xsgq.employee_id = xssp.employee_id\n" +
                        "                                  --                                  AND xsgq.srp_goal_header_id =\n" +
                        "                                  --                                         xssp.srp_goal_header_id\n" +
                        "                                  --                                  AND xsgq.quota_id = xssp.quota_id\n" +
                        "                                  --                                  AND xsgq.comp_plan_id = xssp.comp_plan_id\n" +
                        "                                  --                                  AND xssp.erp_pos_flag = 'P'),\n" +
                        "                                  --                        0)\n" +
                        "                                  AS unrealizedrevenue,\n" +
                        "                               xsgq.payout_weight || '%'    AS weight,\n" +
                        "                               ROUND (\n" +
                        "                                    SUM (\n" +
                        "                                         xss.comm_erp_booking_adj\n" +
                        "                                       + xss.comm_man_booking_adj\n" +
                        "                                       + xss.comm_booking_madj_acq\n" +
                        "                                       + xss.comm_booking_madj)\n" +
                        "                                  + ROUND (\n" +
                        "                                       (SELECT NVL (\n" +
                        "                                                  SUM (\n" +
                        "                                                       comm_revenue_amt\n" +
                        "                                                     + comm_erp_revenue_adj\n" +
                        "                                                     + comm_backlog_amt),\n" +
                        "                                                  0)\n" +
                        "                                          FROM xxgdm.xxgdm_srp_summary_pub xssp\n" +
                        "                                         WHERE     xsgq.employee_id =\n" +
                        "                                                      xssp.employee_id\n" +
                        "                                               AND xsgq.srp_goal_header_id =\n" +
                        "                                                      xssp.srp_goal_header_id\n" +
                        "                                               AND xsgq.quota_id = xssp.quota_id\n" +
                        "                                               AND xsgq.comp_plan_id =\n" +
                        "                                                      xssp.comp_plan_id\n" +
                        "                                               AND xssp.erp_pos_flag IN\n" +
                        "                                                      ('IA', '$RN')), -- Modified as part of Apr 2023 Amplifier scope for iACV and $RN to follow same logic as of XAAS\n" +
                        "                                       2)\n" +
                        "                                  + ROUND (\n" +
                        "                                       (SELECT NVL (\n" +
                        "                                                  SUM (\n" +
                        "                                                       comm_revenue_amt\n" +
                        "                                                     + comm_backlog_amt),\n" +
                        "                                                  0)\n" +
                        "                                          FROM xxgdm.xxgdm_srp_summary_pub xssp\n" +
                        "                                         WHERE     xsgq.employee_id =\n" +
                        "                                                      xssp.employee_id\n" +
                        "                                               AND xsgq.srp_goal_header_id =\n" +
                        "                                                      xssp.srp_goal_header_id\n" +
                        "                                               AND xsgq.quota_id = xssp.quota_id\n" +
                        "                                               AND xsgq.comp_plan_id =\n" +
                        "                                                      xssp.comp_plan_id\n" +
                        "                                               AND xssp.erp_pos_flag IN ('X')), -- Modified as part of Apr 2023 Amplifier scope for iACV and $RN to follow same logic as of XAAS\n" +
                        "                                       2)\n" +
                        "                                  --added by ashwitho DSV changes FY22\n" +
                        "                                  + ROUND (\n" +
                        "                                       (SELECT NVL (SUM (comm_booking_amt), 0)\n" +
                        "                                          FROM xxgdm.xxgdm_srp_summary_pub xssp\n" +
                        "                                         WHERE     xsgq.employee_id =\n" +
                        "                                                      xssp.employee_id\n" +
                        "                                               AND xsgq.srp_goal_header_id =\n" +
                        "                                                      xssp.srp_goal_header_id\n" +
                        "                                               AND xsgq.quota_id = xssp.quota_id\n" +
                        "                                               AND xsgq.comp_plan_id =\n" +
                        "                                                      xssp.comp_plan_id\n" +
                        "                                               AND xssp.erp_pos_flag = 'E'),\n" +
                        "                                       2)\n" +
                        "                                  + CASE\n" +
                        "                                       WHEN xsgq.fiscal_year >= 2022\n" +
                        "                                       THEN\n" +
                        "                                          ROUND (\n" +
                        "                                             (SELECT NVL (\n" +
                        "                                                        SUM (\n" +
                        "                                                             comm_revenue_amt\n" +
                        "                                                           + comm_backlog_amt),\n" +
                        "                                                        0)\n" +
                        "                                                FROM xxgdm.xxgdm_srp_summary_pub\n" +
                        "                                                     xssp\n" +
                        "                                               WHERE     xsgq.employee_id =\n" +
                        "                                                            xssp.employee_id\n" +
                        "                                                     AND xsgq.srp_goal_header_id =\n" +
                        "                                                            xssp.srp_goal_header_id\n" +
                        "                                                     AND xsgq.quota_id =\n" +
                        "                                                            xssp.quota_id\n" +
                        "                                                     AND xsgq.comp_plan_id =\n" +
                        "                                                            xssp.comp_plan_id\n" +
                        "                                                     AND xssp.erp_pos_flag = 'P'),\n" +
                        "                                             2)\n" +
                        "                                       ELSE\n" +
                        "                                          ROUND (\n" +
                        "                                             (SELECT NVL (SUM (comm_booking_amt),\n" +
                        "                                                          0)\n" +
                        "                                                FROM xxgdm.xxgdm_srp_summary_pub\n" +
                        "                                                     xssp\n" +
                        "                                               WHERE     xsgq.employee_id =\n" +
                        "                                                            xssp.employee_id\n" +
                        "                                                     AND xsgq.srp_goal_header_id =\n" +
                        "                                                            xssp.srp_goal_header_id\n" +
                        "                                                     AND xsgq.quota_id =\n" +
                        "                                                            xssp.quota_id\n" +
                        "                                                     AND xsgq.comp_plan_id =\n" +
                        "                                                            xssp.comp_plan_id\n" +
                        "                                                     AND xssp.erp_pos_flag = 'P'),\n" +
                        "                                             2)\n" +
                        "                                    END,\n" +
                        "                                  2)\n" +
                        "                                  AS bookings,\n" +
                        "                               CASE xxg2c_get_emp_input_cap_func (\n" +
                        "                                       NULL,\n" +
                        "                                       NULL,\n" +
                        "                                       CASE AVG (xss.goal_value)\n" +
                        "                                          WHEN 0\n" +
                        "                                          THEN\n" +
                        "                                             0\n" +
                        "                                          ELSE\n" +
                        "                                             ROUND (\n" +
                        "                                                (  (CASE\n" +
                        "                                                       WHEN xcpe.my_payout_eligibility IN\n" +
                        "                                                               ('REV_MULTI',\n" +
                        "                                                                'BOTH')\n" +
                        "                                                       THEN\n" +
                        "                                                          SUM (\n" +
                        "                                                               NVL (\n" +
                        "                                                                  xss.comm_revenue_mul_amt,\n" +
                        "                                                                  0)\n" +
                        "                                                             + NVL (\n" +
                        "                                                                  xss.comm_erp_revenue_mul_adj,\n" +
                        "                                                                  0)\n" +
                        "                                                             + NVL (\n" +
                        "                                                                  xss.comm_man_revenue_mul_adj,\n" +
                        "                                                                  0)\n" +
                        "                                                             + NVL (\n" +
                        "                                                                  xss.achievement_mul_adj,\n" +
                        "                                                                  0))\n" +
                        "                                                       ELSE\n" +
                        "                                                          SUM (\n" +
                        "                                                               xss.comm_revenue_amt\n" +
                        "                                                             + xss.comm_erp_revenue_adj\n" +
                        "                                                             + xss.comm_man_revenue_adj\n" +
                        "                                                             + xss.achievement_adj)\n" +
                        "                                                    END)\n" +
                        "                                                 * 100\n" +
                        "                                                 / AVG (xss.goal_value)),\n" +
                        "                                                2)\n" +
                        "                                       END,\n" +
                        "                                       xsgq.quota_id,\n" +
                        "                                       xsgq.srp_goal_header_id)\n" +
                        "                                  WHEN 'N'\n" +
                        "                                  THEN\n" +
                        "                                        TO_CHAR (\n" +
                        "                                           DECODE (\n" +
                        "                                              AVG (xss.goal_value),\n" +
                        "                                              0, 0,\n" +
                        "                                              ROUND (\n" +
                        "                                                 (  (CASE\n" +
                        "                                                        WHEN xcpe.my_payout_eligibility IN\n" +
                        "                                                                ('REV_MULTI',\n" +
                        "                                                                 'BOTH')\n" +
                        "                                                        THEN\n" +
                        "                                                           SUM (\n" +
                        "                                                                NVL (\n" +
                        "                                                                   xss.comm_revenue_mul_amt,\n" +
                        "                                                                   0)\n" +
                        "                                                              + NVL (\n" +
                        "                                                                   xss.comm_erp_revenue_mul_adj,\n" +
                        "                                                                   0)\n" +
                        "                                                              + NVL (\n" +
                        "                                                                   xss.comm_man_revenue_mul_adj,\n" +
                        "                                                                   0)\n" +
                        "                                                              + NVL (\n" +
                        "                                                                   xss.achievement_mul_adj,\n" +
                        "                                                                   0))\n" +
                        "                                                        ELSE\n" +
                        "                                                           SUM (\n" +
                        "                                                                xss.comm_revenue_amt\n" +
                        "                                                              + xss.comm_erp_revenue_adj\n" +
                        "                                                              + xss.comm_man_revenue_adj\n" +
                        "                                                              + xss.achievement_adj)\n" +
                        "                                                     END)\n" +
                        "                                                  * 100\n" +
                        "                                                  / AVG (xss.goal_value)),\n" +
                        "                                                 2)),\n" +
                        "                                           '999,999,999,999,990.99')\n" +
                        "                                     || '%'\n" +
                        "                                  WHEN 'O'\n" +
                        "                                  THEN\n" +
                        "                                     (SELECT    TO_CHAR (\n" +
                        "                                                   oa.oa_cap,\n" +
                        "                                                   '999,999,999,999,990.99')\n" +
                        "                                             || '%'\n" +
                        "                                        FROM xxg2c_overachiever_details oa,\n" +
                        "                                             e2e_calc_payment_detail ecpd,\n" +
                        "                                             cn_payruns_all         cpa\n" +
                        "                                       WHERE     ecpd.payrun_id = cpa.payrun_id\n" +
                        "                                             AND cpa.status = 'UNPAID'\n" +
                        "                                             AND ecpd.period_code = oa.period_id\n" +
                        "                                             AND ecpd.goal_id =\n" +
                        "                                                    oa.srp_goal_header_id\n" +
                        "                                             AND ecpd.allocation_id = oa.quota_id\n" +
                        "                                             AND oa.srp_goal_header_id =\n" +
                        "                                                    xsgq.srp_goal_header_id\n" +
                        "                                             AND oa.quota_id = xsgq.quota_id\n" +
                        "                                             AND oa.payrun_id = ecpd.payrun_id\n" +
                        "                                             AND oa.oa_criteria_codes IS NOT NULL\n" +
                        "                                             AND oa.oa_status <> 'REMOVED')\n" +
                        "                                  WHEN 'Y'\n" +
                        "                                  THEN\n" +
                        "                                     (SELECT    TO_CHAR (\n" +
                        "                                                   ecpd.quota_cap,\n" +
                        "                                                   '999,999,999,999,990.99')\n" +
                        "                                             || '%'\n" +
                        "                                        FROM e2e_calc_payment_detail ecpd,\n" +
                        "                                             cn_payruns_all      cpa\n" +
                        "                                       WHERE     ecpd.payrun_id = cpa.payrun_id\n" +
                        "                                             AND cpa.status = 'UNPAID'\n" +
                        "                                             AND ecpd.goal_id =\n" +
                        "                                                    xsgq.srp_goal_header_id\n" +
                        "                                             AND ecpd.allocation_id =\n" +
                        "                                                    xsgq.quota_id)\n" +
                        "                               END\n" +
                        "                                  AS attainment,\n" +
                        "                               (CASE\n" +
                        "                                   WHEN (  xsgq.goal_value\n" +
                        "                                         - (  SUM (\n" +
                        "                                                   xss.comm_revenue_amt\n" +
                        "                                                 + xss.comm_erp_revenue_adj\n" +
                        "                                                 + xss.comm_man_revenue_adj\n" +
                        "                                                 + xss.achievement_adj)\n" +
                        "                                            + SUM (xss.comm_backlog_amt))) > 0\n" +
                        "                                   THEN\n" +
                        "                                      ROUND (\n" +
                        "                                           xsgq.goal_value\n" +
                        "                                         - (  SUM (\n" +
                        "                                                   xss.comm_revenue_amt\n" +
                        "                                                 + xss.comm_erp_revenue_adj\n" +
                        "                                                 + xss.comm_man_revenue_adj\n" +
                        "                                                 + xss.achievement_adj)\n" +
                        "                                            + SUM (xss.comm_backlog_amt)),\n" +
                        "                                         2)\n" +
                        "                                   ELSE\n" +
                        "                                      0\n" +
                        "                                END)\n" +
                        "                                  AS unattained,\n" +
                        "                               (CASE\n" +
                        "                                   WHEN (  SUM (\n" +
                        "                                                xss.comm_revenue_amt\n" +
                        "                                              + xss.comm_erp_revenue_adj\n" +
                        "                                              + xss.comm_man_revenue_adj\n" +
                        "                                              + xss.achievement_adj)\n" +
                        "                                         - xsgq.goal_value) > 0\n" +
                        "                                   THEN\n" +
                        "                                      ROUND (\n" +
                        "                                           SUM (\n" +
                        "                                                xss.comm_revenue_amt\n" +
                        "                                              + xss.comm_erp_revenue_adj\n" +
                        "                                              + xss.comm_man_revenue_adj\n" +
                        "                                              + xss.achievement_adj)\n" +
                        "                                         - xsgq.goal_value,\n" +
                        "                                         2)\n" +
                        "                                   ELSE\n" +
                        "                                      0\n" +
                        "                                END)\n" +
                        "                                  AS overachgoal,\n" +
                        "                                 SUM (\n" +
                        "                                      xss.booking_amt\n" +
                        "                                    + xss.erp_booking_adj\n" +
                        "                                    + xss.man_booking_adj\n" +
                        "                                    + xss.booking_madj_acq\n" +
                        "                                    + xss.booking_madj)\n" +
                        "                               - SUM (\n" +
                        "                                      xss.comm_booking_amt\n" +
                        "                                    + xss.comm_erp_booking_adj\n" +
                        "                                    + xss.comm_man_booking_adj\n" +
                        "                                    + xss.comm_booking_madj_acq\n" +
                        "                                    + xss.comm_booking_madj)\n" +
                        "                                  AS noncommbooking,\n" +
                        "                               --knatolan iacv changes\n" +
                        "                               0\n" +
                        "                                  AS bklg_new_logo_iacv_usd_amount,\n" +
                        "                               0\n" +
                        "                                  AS bklg_upsell_iacv_usd_amount,\n" +
                        "                               0\n" +
                        "                                  AS bklg_xsell_iacv_usd_amount,\n" +
                        "                               0                            AS bklg_iacv_usd_amount,\n" +
                        "                               0\n" +
                        "                                  AS bklg_cum_iacv_usd_amount,\n" +
                        "                               0\n" +
                        "                                  AS bklg_upsell_adj_iacv_amount,\n" +
                        "                               0\n" +
                        "                                  AS bklg_renewal_booking_usd_amt,\n" +
                        "                               0                            AS bklg_atr_usd_amount,\n" +
                        "                               0                            AS bklg_otr_usd_amount,\n" +
                        "                               0\n" +
                        "                                  AS bklg_attrition_usd_amount,\n" +
                        "                               --knatolan iacv end\n" +
                        "                               (CASE\n" +
                        "                                   WHEN xsgq.srp_goal_header_id = (SELECT goal_id\n" +
                        "                                                                  \n" +
                        "                                                                   FROM (  SELECT srp_goal_header_id goal_id\n" +
                        "                                                                             FROM xxg2c_srp_goal_headers_all\n" +
                        "                                                                            WHERE     period_year = "+ FY +" --fiscal year\n" +
                        "                                                                                  AND status_code = 'AUTH'\n" +
                        "                                                                                  AND employee_number = "+ UserId +"\n" +
                        "                                                                         ORDER BY start_date DESC)\n" +
                        "                                                                  WHERE ROWNUM = 1) --l_goal_id -- US633269 changes by sarviswa\n" +
                        "                                   THEN\n" +
                        "                                      'Y'\n" +
                        "                                   ELSE\n" +
                        "                                      'N'\n" +
                        "                                END)\n" +
                        "                                  AS active,\n" +
                        "                               (CASE\n" +
                        "                                   WHEN xsgq.srp_goal_header_id = (SELECT goal_id\n" +
                        "                                                                  \n" +
                        "                                                                   FROM (  SELECT srp_goal_header_id goal_id\n" +
                        "                                                                             FROM xxg2c_srp_goal_headers_all\n" +
                        "                                                                            WHERE     period_year = "+ FY +" --fiscal year\n" +
                        "                                                                                  AND status_code = 'AUTH'\n" +
                        "                                                                                  AND employee_number = "+ UserId +"\n" +
                        "                                                                         ORDER BY start_date DESC)\n" +
                        "                                                                  WHERE ROWNUM = 1)\n" +
                        "                                   THEN\n" +
                        "                                      'Y'\n" +
                        "                                   ELSE\n" +
                        "                                      'N'\n" +
                        "                                END)\n" +
                        "                                  AS currentgs,\n" +
                        "                               (CASE\n" +
                        "                                   WHEN xsgq.start_date =\n" +
                        "                                           (SELECT start_date\n" +
                        "                                              FROM e2e_calendar\n" +
                        "                                             WHERE     period_type_code = 'ANN'\n" +
                        "                                                   AND year = xsgq.fiscal_year)\n" +
                        "                                   THEN\n" +
                        "                                      (SELECT total_days\n" +
                        "                                         FROM (  SELECT total_days\n" +
                        "                                                   FROM e2e_calc_payment_detail\n" +
                        "                                                  WHERE     employee_id =\n" +
                        "                                                               xsgq.employee_id\n" +
                        "                                                        AND goal_id =\n" +
                        "                                                               xsgq.srp_goal_header_id\n" +
                        "                                                        AND bonus_type_code = 'ICC'\n" +
                        "                                               ORDER BY payrun_id DESC)\n" +
                        "                                        WHERE ROWNUM = 1)\n" +
                        "                                   ELSE\n" +
                        "                                      (xsgq.end_date - xsgq.start_date) + 1\n" +
                        "                                END)\n" +
                        "                                  AS totaldays,\n" +
                        "                               xsgq.fiscal_year             AS fiscalyear,\n" +
                        "                               xsgq.currency_code           AS currencycode,\n" +
                        "                               (SELECT DISTINCT\n" +
                        "                                       DECODE (fiscal_interval,\n" +
                        "                                               'A', 'Annual',\n" +
                        "                                               fiscal_interval)\n" +
                        "                                  FROM apps.xxg2c_srp_goal_headers_all\n" +
                        "                                 WHERE srp_goal_header_id =\n" +
                        "                                          xsgq.srp_goal_header_id)\n" +
                        "                                  AS fiscalinterval,\n" +
                        "                               (SELECT DECODE (description,\n" +
                        "                                               'YTD', description,\n" +
                        "                                               INITCAP (description))\n" +
                        "                                  FROM fnd_lookup_values\n" +
                        "                                 WHERE     lookup_type LIKE\n" +
                        "                                              'XXG2CC_PRORATION_TYPE'\n" +
                        "                                       AND lookup_code = xcpe.salary_type)\n" +
                        "                                  AS planprorationtype,\n" +
                        "                               xxg2c_get_emp_input_cap_func (\n" +
                        "                                  NULL,\n" +
                        "                                  NULL,\n" +
                        "                                  CASE AVG (xss.goal_value)\n" +
                        "                                     WHEN 0\n" +
                        "                                     THEN\n" +
                        "                                        0\n" +
                        "                                     ELSE\n" +
                        "                                        ROUND (\n" +
                        "                                           (  SUM (xss.comm_revenue_amt)\n" +
                        "                                            * 100\n" +
                        "                                            / AVG (xss.goal_value)),\n" +
                        "                                           2)\n" +
                        "                                  END,\n" +
                        "                                  xsgq.quota_id,\n" +
                        "                                  xsgq.srp_goal_header_id)\n" +
                        "                                  AS empcapflag,\n" +
                        "                               CASE xxg2c_get_emp_input_cap_func (\n" +
                        "                                       NULL,\n" +
                        "                                       NULL,\n" +
                        "                                       CASE AVG (xss.goal_value)\n" +
                        "                                          WHEN 0\n" +
                        "                                          THEN\n" +
                        "                                             0\n" +
                        "                                          ELSE\n" +
                        "                                             ROUND (\n" +
                        "                                                (  SUM (xss.comm_revenue_amt)\n" +
                        "                                                 * 100\n" +
                        "                                                 / AVG (xss.goal_value)),\n" +
                        "                                                2)\n" +
                        "                                       END,\n" +
                        "                                       xsgq.quota_id,\n" +
                        "                                       xsgq.srp_goal_header_id)\n" +
                        "                                  WHEN 'Y'\n" +
                        "                                  THEN\n" +
                        "                                     'y'\n" +
                        "                                  WHEN 'O'\n" +
                        "                                  THEN\n" +
                        "                                     'O'\n" +
                        "                                  ELSE\n" +
                        "                                     ''\n" +
                        "                               END\n" +
                        "                                  AS empflagmessage,\n" +
                        "                               (SELECT NVL (xlve.attribute7, 'N') acv_flag\n" +
                        "                                  FROM apps.xxg2c_lookups xlve,\n" +
                        "                                       apps.cn_quotas_all cqa\n" +
                        "                                 WHERE     lookup_type = 'XXG2CC_PE_CATEGORY'\n" +
                        "                                       AND cqa.attribute3 = xlve.lookup_code\n" +
                        "                                       AND cqa.quota_id = xsgq.quota_id)\n" +
                        "                                  AS acvflag,\n" +
                        "                               NVL (xsg.my_payout_pe_flag, 'N') AS mypyflag,\n" +
                        "                               MAX (xss.last_update_date)   refresh_date,\n" +
                        "                               xsgq.performance_type        performance_type,\n" +
                        "                               DECODE (xqe.pe_component, 'METRIC', 'Y', 'N')\n" +
                        "                                  AS metricflag, --Modified as part of DDCP-3726\n" +
                        "                               xqe.pe_elig                  AS petype, --Modified as part of DDCP-3726\n" +
                        "                               xsgq.start_date              AS start_date,\n" +
                        "                               xsg.SRP_PE_TYPE --Modified as part of DDCP-3726\n" +
                        "                                              AS             amp_identifier, -- Added as part of Apr 2023 Amplifier scope\n" +
                        "                               xqe.calc_engine              AS calc_engine, --Added as part of DDCP-3726\n" +
                        "                               cqaa.quota_sequence\n" +
                        "                          FROM xxgdm_srp_goal_quotas_pub xsgq,\n" +
                        "                               xxgdm_srp_summary_pub xss,\n" +
                        "                               xxg2c_comp_plans_ext_v xcpe,\n" +
                        "                               xxg2c_srp_goal_quotas_all xsg,\n" +
                        "                               apps.cn_quotas_all    cqa,\n" +
                        "                               cn_quota_assigns_all  cqaa,\n" +
                        "                               apps.xxg2c_lookups    xlve,\n" +
                        "                               apps.xxg2c_quotas_ext xqe\n" +
                        "                         WHERE     xsgq.employee_id = "+ UserId +"\n" +
                        "                               AND xsgq.fiscal_year IN ("+ FY +", --fiscal\n" +
                        "                                                        "+ FY +", --fiscal\n" +
                        "                                                        "+ FY +", --fiscal\n" +
                        "                                                        "+ FY +") --fiscal -- Added prev_year2 to show FY21 in UI for Q1FY24)\n" +
                        "                               AND xcpe.comp_plan_id = xss.comp_plan_id\n" +
                        "                               AND xsgq.comp_plan_id = xcpe.comp_plan_id\n" +
                        "                               AND xsgq.status_code = 'A'\n" +
                        "                               AND xsgq.employee_id = xss.employee_id\n" +
                        "                               AND xsgq.srp_goal_header_id =\n" +
                        "                                      xss.srp_goal_header_id\n" +
                        "                               AND xsgq.quota_id = xss.quota_id\n" +
                        "                               AND xsg.srp_goal_header_id =\n" +
                        "                                      xss.srp_goal_header_id\n" +
                        "                               AND xsg.quota_id = xss.quota_id\n" +
                        "                               --AND NVL (xsg.shadow_pe, 'N') = 'N'\n" +
                        "                               AND (   NVL (xsg.shadow_pe, 'N') = 'N'\n" +
                        "                                    OR (    NVL (xsg.shadow_pe, 'N') = 'Y'\n" +
                        "                                        AND xsg.SRP_PE_TYPE = 'AMPLIFIER')) --added not show shado PE for reguler PE's\n" +
                        "                               AND xsgq.comp_plan_id = xss.comp_plan_id\n" +
                        "                               AND xsgq.bonus_type_code = xss.bonus_type_code\n" +
                        "                               AND xsgq.bonus_type_code = 'ICC'\n" +
                        "                               AND cqa.quota_id = xsgq.quota_id --knatolan: iACV changes\n" +
                        "                               AND xlve.lookup_type = 'XXG2CC_PE_CATEGORY'\n" +
                        "                               AND cqa.attribute3 = xlve.lookup_code\n" +
                        "                               AND cqa.quota_id = xqe.quota_id\n" +
                        "                               AND cqa.quota_id = cqaa.quota_id\n" +
                        "                               AND xsgq.comp_plan_id = cqaa.comp_plan_id\n" +
                        "                               AND xqe.calc_engine = 'OIC' --xlve.attribute8 IS NULL -- Modified to consider records with OIC and NULL as OIC\n" +
                        "                      ---AND NVL (xlve.attribute10, 'N') <> 'AMP-UPLIFT' -- Added as part of Amplifier scope Restricting uplift PEs from appearing in Goal to Cash\n" +
                        "                      GROUP BY xsgq.srp_goal_header_id,\n" +
                        "                                  xsgq.comp_plan_title\n" +
                        "                               || '        '\n" +
                        "                               || TO_CHAR (xsgq.start_date, 'DD-Mon-YYYY')\n" +
                        "                               || ' to '\n" +
                        "                               || TO_CHAR (xsgq.end_date, 'DD-Mon-YYYY'),\n" +
                        "                               xsgq.quota_id,\n" +
                        "                               xsgq.goal_value,\n" +
                        "                               xsgq.quota_id,\n" +
                        "                               xsgq.employee_id,\n" +
                        "                               xsgq.comp_plan_id,\n" +
                        "                               xsgq.payout_weight,\n" +
                        "                               \n" +
                        "                               DECODE (xsgq.service_flag,\n" +
                        "                                       'C', 'N',\n" +
                        "                                       xsgq.service_flag),\n" +
                        "                               xsgq.start_date,\n" +
                        "                               xsgq.end_date,\n" +
                        "                               xsgq.fiscal_year,\n" +
                        "                               xsgq.currency_code,\n" +
                        "                               xcpe.salary_type,\n" +
                        "                               xsg.my_payout_pe_flag,\n" +
                        "                               xcpe.my_payout_eligibility,\n" +
                        "                               xsgq.performance_type,\n" +
                        "                               DECODE (xqe.pe_component, 'METRIC', 'Y', 'N'),\n" +
                        "                               xqe.pe_elig,\n" +
                        "                               xsg.SRP_PE_TYPE, -- Added as part of Apr 2023 Amplifier scope\n" +
                        "                               xqe.calc_engine,\n" +
                        "                               cqaa.quota_sequence\n" +
                        "                      UNION ALL\n" +
                        "                        SELECT xsgq.srp_goal_header_id      AS goalid,\n" +
                        "                               xsgq.quota_id                AS allocationid,\n" +
                        "                               xsgq.comp_plan_id            AS planid,\n" +
                        "                               xsgq.start_date,\n" +
                        "                                  xsgq.comp_plan_title\n" +
                        "                               || '        '\n" +
                        "                               || TO_CHAR (xsgq.start_date, 'DD-Mon-YYYY')\n" +
                        "                               || ' to '\n" +
                        "                               || TO_CHAR (xsgq.end_date, 'DD-Mon-YYYY')\n" +
                        "                                  AS goalsheet,\n" +
                        "                               (SELECT attribute3\n" +
                        "                                  FROM apps.cn_quotas_all\n" +
                        "                                 WHERE quota_id = xsgq.quota_id)\n" +
                        "                                  AS plantitle,\n" +
                        "                               ROUND (xsgq.goal_value, 2)   AS goal,\n" +
                        "                               SUM (xss.comm_revenue_amt)   AS revenueamount,\n" +
                        "                               SUM (xss.comm_revenue_amt)   AS revenueamountmul,\n" +
                        "                               NVL (ROUND (SUM (xss.comm_backlog_amt), 2), 0)\n" +
                        "                                  AS unrealizedrevenue,\n" +
                        "                               xsgq.payout_weight || '%'    AS weight,\n" +
                        "                               0                            AS bookings,   --0\n" +
                        "                                  TO_CHAR (\n" +
                        "                                     DECODE (\n" +
                        "                                        AVG (xss.goal_value),\n" +
                        "                                        0, 0,\n" +
                        "                                        ROUND (\n" +
                        "                                           (  (SUM (xss.comm_revenue_amt))\n" +
                        "                                            * 100\n" +
                        "                                            / AVG (xss.goal_value)),\n" +
                        "                                           2)),\n" +
                        "                                     '999,999,999,999,990.99')\n" +
                        "                               || '%'\n" +
                        "                                  AS \"attainment\",\n" +
                        "                               (CASE\n" +
                        "                                   WHEN (  xsgq.goal_value\n" +
                        "                                         - (  SUM (xss.comm_revenue_amt)\n" +
                        "                                            + SUM (xss.comm_backlog_amt))) > 0\n" +
                        "                                   THEN\n" +
                        "                                      ROUND (\n" +
                        "                                           xsgq.goal_value\n" +
                        "                                         - (  SUM (xss.comm_revenue_amt)\n" +
                        "                                            + SUM (xss.comm_backlog_amt)),\n" +
                        "                                         2)\n" +
                        "                                   ELSE\n" +
                        "                                      0\n" +
                        "                                END)\n" +
                        "                                  AS \"unattained\",\n" +
                        "                               0                            AS \"overachGoal\",\n" +
                        "                               0                            AS \"nonCommBooking\", -- check on this\n" +
                        "                               --knatolan iacv changes start\n" +
                        "                               SUM (xss.bklg_new_logo_iacv_usd_amount)\n" +
                        "                                  AS bklg_new_logo_iacv_usd_amount,\n" +
                        "                               SUM (xss.bklg_upsell_iacv_usd_amount)\n" +
                        "                                  AS bklg_upsell_iacv_usd_amount,\n" +
                        "                               SUM (xss.bklg_xsell_iacv_usd_amount)\n" +
                        "                                  AS bklg_xsell_iacv_usd_amount,\n" +
                        "                               SUM (xss.bklg_iacv_usd_amount)\n" +
                        "                                  AS bklg_iacv_usd_amount,\n" +
                        "                               SUM (xss.bklg_cum_iacv_usd_amount)\n" +
                        "                                  AS bklg_cum_iacv_usd_amount,\n" +
                        "                               SUM (xss.bklg_upsell_adj_iacv_amount)\n" +
                        "                                  AS bklg_upsell_adj_iacv_amount,\n" +
                        "                               SUM (xss.bklg_renewal_booking_usd_amt)\n" +
                        "                                  AS bklg_renewal_booking_usd_amt,\n" +
                        "                               SUM (xss.bklg_atr_usd_amount)\n" +
                        "                                  AS bklg_atr_usd_amount,\n" +
                        "                               SUM (xss.bklg_otr_usd_amount)\n" +
                        "                                  AS bklg_otr_usd_amount,\n" +
                        "                               SUM (xss.bklg_attrition_usd_amount)\n" +
                        "                                  AS bklg_attrition_usd_amount,\n" +
                        "                               --knatolan iacv changes end\n" +
                        "                               (CASE\n" +
                        "                                   WHEN xsgq.srp_goal_header_id = (SELECT goal_id\n" +
                        "                                                                  \n" +
                        "                                                                   FROM (  SELECT srp_goal_header_id goal_id\n" +
                        "                                                                             FROM xxg2c_srp_goal_headers_all\n" +
                        "                                                                            WHERE     period_year = "+ FY +" --fiscal year\n" +
                        "                                                                                  AND status_code = 'AUTH'\n" +
                        "                                                                                  AND employee_number = "+ UserId +"\n" +
                        "                                                                         ORDER BY start_date DESC)\n" +
                        "                                                                  WHERE ROWNUM = 1) --l_goal_id -- US633269 changes by sarviswa\n" +
                        "                                   THEN\n" +
                        "                                      'Y'\n" +
                        "                                   ELSE\n" +
                        "                                      'N'\n" +
                        "                                END)\n" +
                        "                                  AS \"active\",\n" +
                        "                               (CASE\n" +
                        "                                   WHEN xsgq.srp_goal_header_id = (SELECT goal_id\n" +
                        "                                                                  \n" +
                        "                                                                   FROM (  SELECT srp_goal_header_id goal_id\n" +
                        "                                                                             FROM xxg2c_srp_goal_headers_all\n" +
                        "                                                                            WHERE     period_year = "+ FY +" --fiscal year\n" +
                        "                                                                                  AND status_code = 'AUTH'\n" +
                        "                                                                                  AND employee_number = "+ UserId +"\n" +
                        "                                                                         ORDER BY start_date DESC)\n" +
                        "                                                                  WHERE ROWNUM = 1)\n" +
                        "                                   THEN\n" +
                        "                                      'Y'\n" +
                        "                                   ELSE\n" +
                        "                                      'N'\n" +
                        "                                END)\n" +
                        "                                  AS \"currentGS\",\n" +
                        "                               (CASE\n" +
                        "                                   WHEN xsgq.start_date =\n" +
                        "                                           (SELECT start_date\n" +
                        "                                              FROM e2e_calendar\n" +
                        "                                             WHERE     period_type_code = 'ANN'\n" +
                        "                                                   AND year = xsgq.fiscal_year)\n" +
                        "                                   THEN\n" +
                        "                                      (SELECT total_days\n" +
                        "                                         FROM (  SELECT total_days\n" +
                        "                                                   FROM e2e_calc_payment_detail\n" +
                        "                                                  WHERE     employee_id =\n" +
                        "                                                               xsgq.employee_id\n" +
                        "                                                        AND goal_id =\n" +
                        "                                                               xsgq.srp_goal_header_id\n" +
                        "                                                        AND bonus_type_code = 'ICC'\n" +
                        "                                               ORDER BY payrun_id DESC)\n" +
                        "                                        WHERE ROWNUM = 1)\n" +
                        "                                   ELSE\n" +
                        "                                      (xsgq.end_date - xsgq.start_date) + 1\n" +
                        "                                END)\n" +
                        "                                  AS \"totalDays\",\n" +
                        "                               xsgq.fiscal_year             AS \"fiscalYear\",\n" +
                        "                               xsgq.currency_code           AS \"currencyCode\",\n" +
                        "                               (SELECT DISTINCT\n" +
                        "                                       DECODE (fiscal_interval,\n" +
                        "                                               'A', 'Annual',\n" +
                        "                                               fiscal_interval)\n" +
                        "                                  FROM apps.xxg2c_srp_goal_headers_all\n" +
                        "                                 WHERE srp_goal_header_id =\n" +
                        "                                          xsgq.srp_goal_header_id)\n" +
                        "                                  AS \"fiscalInterval\",\n" +
                        "                               (SELECT DECODE (description,\n" +
                        "                                               'YTD', description,\n" +
                        "                                               INITCAP (description))\n" +
                        "                                  FROM fnd_lookup_values\n" +
                        "                                 WHERE     lookup_type LIKE\n" +
                        "                                              'XXG2CC_PRORATION_TYPE'\n" +
                        "                                       AND lookup_code = xcpe.salary_type)\n" +
                        "                                  AS \"planProrationType\",\n" +
                        "                               xxg2c_get_emp_input_cap_func (\n" +
                        "                                  NULL,\n" +
                        "                                  NULL,\n" +
                        "                                  CASE AVG (xss.goal_value)\n" +
                        "                                     WHEN 0\n" +
                        "                                     THEN\n" +
                        "                                        0\n" +
                        "                                     ELSE\n" +
                        "                                        ROUND (\n" +
                        "                                           (  SUM (xss.comm_revenue_amt)\n" +
                        "                                            * 100\n" +
                        "                                            / AVG (xss.goal_value)),\n" +
                        "                                           2)\n" +
                        "                                  END,\n" +
                        "                                  xsgq.quota_id,\n" +
                        "                                  xsgq.srp_goal_header_id)\n" +
                        "                                  AS \"EmpCapFlag\",        -- what's this flag?\n" +
                        "                               CASE xxg2c_get_emp_input_cap_func (\n" +
                        "                                       NULL,\n" +
                        "                                       NULL,\n" +
                        "                                       CASE AVG (xss.goal_value)\n" +
                        "                                          WHEN 0\n" +
                        "                                          THEN\n" +
                        "                                             0\n" +
                        "                                          ELSE\n" +
                        "                                             ROUND (\n" +
                        "                                                (  SUM (xss.comm_revenue_amt)\n" +
                        "                                                 * 100\n" +
                        "                                                 / AVG (xss.goal_value)),\n" +
                        "                                                2)\n" +
                        "                                       END,\n" +
                        "                                       xsgq.quota_id,\n" +
                        "                                       xsgq.srp_goal_header_id)\n" +
                        "                                  WHEN 'Y'\n" +
                        "                                  THEN\n" +
                        "                                     'y'\n" +
                        "                                  WHEN 'O'\n" +
                        "                                  THEN\n" +
                        "                                     'o'\n" +
                        "                                  ELSE\n" +
                        "                                     ''\n" +
                        "                               END\n" +
                        "                                  AS \"EmpFlagMessage\",\n" +
                        "                               (SELECT NVL (xlve.attribute7, 'N') acv_flag\n" +
                        "                                  FROM apps.xxg2c_lookups xlve,\n" +
                        "                                       apps.cn_quotas_all cqa\n" +
                        "                                 WHERE     lookup_type = 'XXG2CC_PE_CATEGORY'\n" +
                        "                                       AND cqa.attribute3 = xlve.lookup_code\n" +
                        "                                       AND cqa.quota_id = xsgq.quota_id)\n" +
                        "                                  AS \"acvFlag\",\n" +
                        "                               NVL (xsg.my_payout_pe_flag, 'N') AS \"myPYFlag\",\n" +
                        "                               MAX (xss.last_update_date)   refresh_date,\n" +
                        "                               xsgq.performance_type        performance_type,\n" +
                        "                               DECODE (xqe.pe_component, 'METRIC', 'Y', 'N')\n" +
                        "                                  AS metricflag, --Modified as part of DDCP-3726\n" +
                        "                               xqe.pe_elig                  petype, --Modified as part of DDCP-3726\n" +
                        "                               xsgq.start_date              AS start_date,\n" +
                        "                               xsg.SRP_PE_TYPE              AS amp_identifier, --Modified as part of DDCP-3726\n" +
                        "                               xqe.calc_engine              AS calc_engine, --Added as part of DDCP-3726\n" +
                        "                               cqaa.quota_sequence\n" +
                        "                          FROM xxgdm_srp_goal_quotas_pub xsgq,\n" +
                        "                               xxgdm_srp_summary_pub xss,\n" +
                        "                               xxg2c_comp_plans_ext_v xcpe,\n" +
                        "                               xxg2c_srp_goal_quotas_all xsg,\n" +
                        "                               apps.cn_quotas_all    cqa,\n" +
                        "                               apps.cn_quota_assigns_all cqaa,\n" +
                        "                               apps.xxg2c_lookups    xlve,\n" +
                        "                               apps.xxg2c_quotas_ext xqe\n" +
                        "                         WHERE     xsgq.employee_id = "+ UserId +"\n" +
                        "                               AND xsgq.fiscal_year IN ("+ FY +", --fiscal\n" +
                        "                                                        "+ FY +", --fiscal\n" +
                        "                                                        "+ FY +", --fiscal\n" +
                        "                                                        "+ FY +") --fiscal -- Added prev_year2 to show FY21 in UI for Q1FY24)\n" +
                        "                               AND xcpe.comp_plan_id = xss.comp_plan_id\n" +
                        "                               AND xsgq.comp_plan_id = xcpe.comp_plan_id\n" +
                        "                               AND xsgq.status_code = 'A'\n" +
                        "                               AND xsgq.employee_id = xss.employee_id\n" +
                        "                               AND xsgq.srp_goal_header_id =\n" +
                        "                                      xss.srp_goal_header_id\n" +
                        "                               AND xsgq.quota_id = xss.quota_id\n" +
                        "                               AND xsg.srp_goal_header_id =\n" +
                        "                                      xss.srp_goal_header_id\n" +
                        "                               AND xsg.quota_id = xss.quota_id\n" +
                        "                               --AND NVL (xsg.shadow_pe, 'N') = 'N'\n" +
                        "                               AND (   NVL (xsg.shadow_pe, 'N') = 'N'\n" +
                        "                                    OR (    NVL (xsg.shadow_pe, 'N') = 'Y'\n" +
                        "                                        AND xsg.SRP_PE_TYPE = 'AMPLIFIER'))\n" +
                        "                               AND xsgq.comp_plan_id = xss.comp_plan_id\n" +
                        "                               AND xsgq.bonus_type_code = xss.bonus_type_code\n" +
                        "                               AND xsgq.bonus_type_code = 'ICC'\n" +
                        "                               AND cqa.quota_id = xsgq.quota_id\n" +
                        "                               AND cqa.quota_id = cqaa.quota_id\n" +
                        "                               AND xsgq.comp_plan_id = cqaa.comp_plan_id\n" +
                        "                               AND xlve.lookup_type = 'XXG2CC_PE_CATEGORY'\n" +
                        "                               AND cqa.attribute3 = xlve.lookup_code\n" +
                        "                               AND cqa.quota_id = xqe.quota_id\n" +
                        "                               AND xqe.calc_engine = 'SAP' --xlve.attribute8 IS NOT NULL -- Modified to consider the PE only when Attribute8 is SAP\n" +
                        "                      -- AND NVL (xlve.attribute10, 'N') <> 'AMP-UPLIFT' -- Added as part of Amplifier scope Restricting uplift PEs from appearing in Goal to Cash\n" +
                        "                      GROUP BY xsgq.srp_goal_header_id,\n" +
                        "                                  xsgq.comp_plan_title\n" +
                        "                               || '        '\n" +
                        "                               || TO_CHAR (xsgq.start_date, 'DD-Mon-YYYY')\n" +
                        "                               || ' to '\n" +
                        "                               || TO_CHAR (xsgq.end_date, 'DD-Mon-YYYY'),\n" +
                        "                               xsgq.quota_id,\n" +
                        "                               xsgq.goal_value,\n" +
                        "                               xsgq.quota_id,\n" +
                        "                               xsgq.employee_id,\n" +
                        "                               xsgq.comp_plan_id,\n" +
                        "                               xsgq.payout_weight,\n" +
                        "                               \n" +
                        "                               DECODE (xsgq.service_flag,\n" +
                        "                                       'C', 'N',\n" +
                        "                                       xsgq.service_flag),\n" +
                        "                               xsgq.start_date,\n" +
                        "                               xsgq.end_date,\n" +
                        "                               xsgq.fiscal_year,\n" +
                        "                               xsgq.currency_code,\n" +
                        "                               xcpe.salary_type,\n" +
                        "                               xsg.my_payout_pe_flag,\n" +
                        "                               xcpe.my_payout_eligibility,\n" +
                        "                               xsgq.performance_type,\n" +
                        "                               DECODE (xqe.pe_component, 'METRIC', 'Y', 'N'),\n" +
                        "                               xqe.pe_elig,\n" +
                        "                               xsg.SRP_PE_TYPE, -- Added as part of Apr 2023 Amplifier scope\n" +
                        "                               xqe.calc_engine,\n" +
                        "                               cqaa.quota_sequence\n" +
                        "                      ORDER BY 4 DESC,                       --xsgq.start_date\n" +
                        "                                      1 DESC,        --xsgq.srp_goal_header_id\n" +
                        "                                             11 DESC      --xsgq.payout_weight\n" +
                        "                                                    --   DECODE (xsgq.service_flag, 'C', 'N', xsgq.service_flag)");
                while (resultSet.next()) {
                    String category = resultSet.getString(6 );
                    String goal = resultSet.getString(7 );
                    String booking = resultSet.getString(12 );
                    String noncomm = resultSet.getString(16);
                    String backlog = resultSet.getString(10 );
                    String revoriginal = resultSet.getString(8 );
                    String revmultiplied = resultSet.getString(9 );
                    String revattainment = resultSet.getString(13 );
                    categories.add(category);
                    System.out.println(category);
                    order_list.add(new CashdataModel(category, goal,booking,noncomm,backlog,revoriginal,revmultiplied,revattainment));
                }

                categorydropview=findViewById(R.id.auto_complete_text3);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,categories);
                categorydropview.setAdapter(adapter);
            }

            if(!order_list.isEmpty()) {
                categorylayout.setVisibility(View.VISIBLE);
                tablelayout.setVisibility(View.VISIBLE);
                noData.setVisibility(View.GONE);

            }
            else{
                tablelayout.setVisibility(View.INVISIBLE);
                noData.setVisibility(View.VISIBLE);
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
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            this.connecton = DriverManager.getConnection("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=dbs-pdv-vm-2020.cisco.com)(PORT=1576))(CONNECT_DATA=(SERVICE_NAME=DV1G2C_SRVC_OTH.cisco.com)(Server=Dedicated)))", "APPS", "B1UE2UTH");
            Toast.makeText(this, "CONNECTED", Toast.LENGTH_LONG).show();
            Toast.makeText(CashActivity.this,"Fiscal Year: "+ FY,Toast.LENGTH_SHORT).show();
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