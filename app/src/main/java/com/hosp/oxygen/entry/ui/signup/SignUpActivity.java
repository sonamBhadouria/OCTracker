package com.hosp.oxygen.entry.ui.signup;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hosp.oxygen.entry.R;
import com.hosp.oxygen.entry.ui.login.LoginActivity;
import com.hosp.oxygen.entry.ui.login.LoginRequest;
import com.hosp.oxygen.entry.ui.login.LoginResponse;
import com.hosp.oxygen.entry.util.APIClient;
import com.hosp.oxygen.entry.util.APIInterface;
import com.hosp.oxygen.entry.util.ShowProgressDialogue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private APIInterface mApiInterface;
    SignUpResponse signUpResponse;

    EditText username;
    EditText password;
    EditText edt_fname;
    EditText edt_lname;
    EditText edt_m_number;
    RadioButton radioButton;
    RadioButton radioButton2;
    Button signup;

    ShowProgressDialogue showProgressDialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
     //   Toolbar toolbar = findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);
        mApiInterface = APIClient.getClient().create(APIInterface.class);
        showProgressDialogue =new ShowProgressDialogue();

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        edt_fname=findViewById(R.id.edt_fname);
        edt_lname=findViewById(R.id.edt_lname);
        edt_m_number=findViewById(R.id.edt_m_number);
        radioButton=findViewById(R.id.radioButton);
        radioButton2=findViewById(R.id.radioButton2);
        signup=findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!isEmailValid(username.getText()))
                {
                    Toast.makeText(SignUpActivity.this, "Please enter a valid email address",Toast.LENGTH_LONG).show();
                }else if(password.getText().toString().length()<6)
                {
                    Toast.makeText(SignUpActivity.this, "Please enter a minimum 6 digit password",Toast.LENGTH_LONG).show();
                }else if(!isValid(password.getText().toString()))
                {
                    Toast.makeText(SignUpActivity.this, "Password must contains both number and alphabets",Toast.LENGTH_LONG).show();
                } else if(TextUtils.isEmpty(edt_fname.getText().toString()))
                {
                    Toast.makeText(SignUpActivity.this, "Please enter first name",Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(edt_lname.getText().toString()))
                {
                    Toast.makeText(SignUpActivity.this, "Please enter last name",Toast.LENGTH_LONG).show();
                }else if(edt_m_number.getText().toString().length()<10)
                {
                    Toast.makeText(SignUpActivity.this, "Please enter a minimum 10 digit phone number",Toast.LENGTH_LONG).show();
                }
                else
                signup();
            }
        });


    }

    public boolean isValid(String s) {
        String n = ".*[0-9].*";
        String A = ".*[A-Z].*";
        String a = ".*[a-z].*";
        return s.matches(n) && (s.matches(a) || s.matches(A));
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void signup() {
        signUpResponse=new SignUpResponse();
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(username.getText().toString()); // admin@admin.com // asr1@gmaill.com
        signUpRequest.setUserpassword(password.getText().toString()); // admin@admin.com // asr1@gmaill.com
        signUpRequest.setFirstname(edt_fname.getText().toString()); // admin@admin.com // asr1@gmaill.com
        signUpRequest.setLastname(edt_lname.getText().toString()); // admin@admin.com // asr1@gmaill.com
        signUpRequest.setMobile(edt_m_number.getText().toString()); // admin@admin.com // asr1@gmaill.com

        if(radioButton.isChecked())
        {
            signUpRequest.setRoleid(3);
        }else
        {
            signUpRequest.setRoleid(4);
        }



        try {
            showProgressDialogue.setProgressDialog(SignUpActivity.this,"Please wait...");

            mApiInterface.signup("application/json", signUpRequest).enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                    showProgressDialogue.dismissDialogue();
                    if (response.body() != null) {
                        if (response.body().getMessage() != null) {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            if(response.body().isSuccess()){

                                finish();
                            }
                        }
                    }
                    else {
                        Toast.makeText(SignUpActivity.this,"please enter correct credentials",Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable t) {
                    showProgressDialogue.dismissDialogue();
                    Toast.makeText(SignUpActivity.this,"Error!!",Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception ex) {
            showProgressDialogue.dismissDialogue();
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }

    }


}