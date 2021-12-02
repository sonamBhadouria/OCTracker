package com.hosp.oxygen.entry.ui.airportuser;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hosp.oxygen.entry.R;
import com.hosp.oxygen.entry.ui.hospuser.HospUserActivity;
import com.hosp.oxygen.entry.ui.login.LoginActivity;
import com.hosp.oxygen.entry.ui.login.LoginRequest;
import com.hosp.oxygen.entry.ui.login.LoginResponse;
import com.hosp.oxygen.entry.util.APIClient;
import com.hosp.oxygen.entry.util.APIInterface;
import com.hosp.oxygen.entry.util.FormatConversionHelper;
import com.hosp.oxygen.entry.util.GpsTracker;
import com.hosp.oxygen.entry.util.SharedPrefData;
import com.hosp.oxygen.entry.util.ShowProgressDialogue;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AirportUserActivity extends AppCompatActivity {
    TextView qr_response;
    Button btn_submit;
    ShowProgressDialogue showProgressDialogue;


    EditText edt_serial_number_equip;
    EditText edt_capacity;
    EditText manuf_name;
    TextView date_response;


    private APIInterface mApiInterface;

    AirportUserResponse airportUserResponse;

    String getDate, selectedDate;
    int day = 0, month = 0, year = 0;
   Button btn_date;
    private GpsTracker gpsTracker;
    String str_lat="",str_long="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airport_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mApiInterface = APIClient.getClient().create(APIInterface.class);
        showProgressDialogue =new ShowProgressDialogue();

        btn_date=findViewById(R.id.btn_date);
        edt_serial_number_equip = findViewById(R.id.edt_serial_number_equip);
         edt_capacity = findViewById(R.id.edt_capacity);
         manuf_name = findViewById(R.id.manuf_name);
         date_response = findViewById(R.id.date_response);
        currentLocationGet();

        qr_response = findViewById(R.id.qr_response);
        btn_submit=findViewById(R.id.btn_submit);
        Button btn_qr_code = findViewById(R.id.btn_qr_code);
        btn_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                IntentIntegrator intentIntegrator = new IntentIntegrator(AirportUserActivity.this);
                intentIntegrator.setPrompt("Scan a QR Code");
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.initiateScan();
            }
        });

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Setcalender();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 if(TextUtils.isEmpty(qr_response.getText().toString()))
                 {
                     Toast.makeText(AirportUserActivity.this,"Please scan a QR Code",Toast.LENGTH_LONG).show();
                 }else if(TextUtils.isEmpty(edt_serial_number_equip.getText().toString()))
                {
                    Toast.makeText(AirportUserActivity.this,"Please enter serial number",Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(edt_capacity.getText().toString()))
                {
                    Toast.makeText(AirportUserActivity.this,"Please enter capacity",Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(manuf_name.getText().toString()))
                {
                    Toast.makeText(AirportUserActivity.this,"Please enter manufacturer name",Toast.LENGTH_LONG).show();
                }
                 else if(TextUtils.isEmpty(date_response.getText().toString()))
                {
                    Toast.makeText(AirportUserActivity.this,"Please select date",Toast.LENGTH_LONG).show();
                }
                 else
                 {

                     syncOCData();

                 }




            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_app:
                 finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void currentLocationGet() {
        gpsTracker = new GpsTracker(AirportUserActivity.this);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            if (String.valueOf(latitude).length() > 0 && String.valueOf(longitude).length() > 0) {

                str_lat = String.valueOf(latitude);
                str_long = String.valueOf(longitude);
            }
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
               // Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                qr_response.setText(intentResult.getContents()+"-"+intentResult.getFormatName());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    public void syncOCData() {
        airportUserResponse=new AirportUserResponse();
        AirportUserRequest airportUserRequest = new AirportUserRequest();
        airportUserRequest.setSerial_no(edt_serial_number_equip.getText().toString()); // admin@admin.com // asr1@gmaill.com
        airportUserRequest.setCapacity(edt_capacity.getText().toString()); // admin@admin.com // asr1@gmaill.com
        airportUserRequest.setManufacturer_name(manuf_name.getText().toString()); // admin@admin.com // asr1@gmaill.com
        airportUserRequest.setQr_code_detail(qr_response.getText().toString()); // admin@admin.com // asr1@gmaill.com
        airportUserRequest.setDoc_url(""); // admin@admin.com // asr1@gmaill.com
        //airportUserRequest.setDate_of_manufacturing("2021-05-25T07:58:33.109Z"); // admin@admin.com // asr1@gmaill.com
        airportUserRequest.setDate_of_manufacturing(date_response.getText().toString()); // admin@admin.com // asr1@gmaill.com
        airportUserRequest.setLatitude(str_lat);
        airportUserRequest.setLongitude(str_long);

        String accesstoken="Bearer "+getFromPrefs(SharedPrefData.ACCESS_Token);
        try {
            showProgressDialogue.setProgressDialog(AirportUserActivity.this,"Please wait...");
            mApiInterface.syncoc("application/json",accesstoken, airportUserRequest).enqueue(new Callback<AirportUserResponse>() {
                @Override
                public void onResponse(Call<AirportUserResponse> call, Response<AirportUserResponse> response) {
                    showProgressDialogue.dismissDialogue();
                    if (response.body() != null) {
                        Toast.makeText(AirportUserActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        if(response.body().isSuccess())
                        {
                            finish();
                            Intent myIntent= new Intent(AirportUserActivity.this, AirportUserActivity.class);
                            startActivity(myIntent);
                        }
                    }
                    else {
                        Toast.makeText(AirportUserActivity.this,"Error!!",Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<AirportUserResponse> call, Throwable t) {
                    showProgressDialogue.dismissDialogue();
                    Toast.makeText(AirportUserActivity.this,"Error!!!",Toast.LENGTH_LONG).show();

                }
            });

        } catch (Exception ex) {
            showProgressDialogue.dismissDialogue();
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public String getFromPrefs(String key) {
        SharedPreferences prefs = getSharedPreferences(SharedPrefData.PREF_NAME, MODE_PRIVATE);
        return prefs.getString(key, SharedPrefData.DEFAULT_VALUE);
    }


    Calendar myCalendar = Calendar.getInstance();
    public void Setcalender() {
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(this,  date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        datePickerDialog.getDatePicker().setMaxDate(c.getTime().getTime());
        c.add(Calendar.YEAR, -100);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            year = year;
            month = monthOfYear + 1;
            day = dayOfMonth;
            selectedDate = FormatConversionHelper.getFormatedDateTime(day + "-" + month + "-" + year, "dd-MM-yyyy", "yyyy-MM-dd");
            Log.e("date", "selectDate after change " + selectedDate);
           date_response.setText(selectedDate);
        }

    };


}