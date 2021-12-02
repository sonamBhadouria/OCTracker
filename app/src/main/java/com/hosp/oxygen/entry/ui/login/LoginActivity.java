package com.hosp.oxygen.entry.ui.login;

import android.Manifest;
import android.app.Activity;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hosp.oxygen.entry.R;
import com.hosp.oxygen.entry.ui.airportuser.AirportUserActivity;
import com.hosp.oxygen.entry.ui.hospuser.HospUserActivity;
import com.hosp.oxygen.entry.ui.login.LoginViewModel;
import com.hosp.oxygen.entry.ui.login.LoginViewModelFactory;
import com.hosp.oxygen.entry.ui.signup.SignUpActivity;
import com.hosp.oxygen.entry.util.APIClient;
import com.hosp.oxygen.entry.util.APIInterface;
import com.hosp.oxygen.entry.util.PermissionResultCallback;
import com.hosp.oxygen.entry.util.PermissionUtils;
import com.hosp.oxygen.entry.util.SharedPrefData;
import com.hosp.oxygen.entry.util.ShowProgressDialogue;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, PermissionResultCallback {

  //  private LoginViewModel loginViewModel;
    LoginResponse mLoginResponse;
    EditText usernameEditText;
    EditText passwordEditText;
    private APIInterface mApiInterface;
    Button login;
    ShowProgressDialogue showProgressDialogue;
    PermissionUtils permissionUtils;
    ArrayList<String> permissions=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       /* loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);*/
       mApiInterface = APIClient.getClient().create(APIInterface.class);
        login=findViewById(R.id.login);
        showProgressDialogue =new ShowProgressDialogue();

        final TextView tv_signup = findViewById(R.id.tv_signup);
         usernameEditText = findViewById(R.id.username);
         passwordEditText = findViewById(R.id.password);

     //   usernameEditText.setText("abc@abc.com");
    //    passwordEditText.setText("abc");

      //  usernameEditText.setText("test@test.com");
       // passwordEditText.setText("test");

        final Button loginButton = findViewById(R.id.login);
       // final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        permissionUtils=new PermissionUtils(LoginActivity.this);

        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNetworkAvailable(LoginActivity.this)){
                    Toast.makeText(LoginActivity.this,"Internet not available",Toast.LENGTH_LONG).show();
                }else {
                    Intent myIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(myIntent);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNetworkAvailable(LoginActivity.this)){
                    Toast.makeText(LoginActivity.this,"Internet not available",Toast.LENGTH_LONG).show();
                }if(!isEmailValid(usernameEditText.getText().toString()))
                {
                    Toast.makeText(LoginActivity.this, "Please enter a valid email address",Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(passwordEditText.getText().toString()))
                {
                    Toast.makeText(LoginActivity.this, "Please enter a  password",Toast.LENGTH_LONG).show();
                }else
                login();
            }
        });

        permissionUtils.check_permission(permissions,"All the permissions are required to access the app functionality",1);

    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }



    public void login() {
        mLoginResponse=new LoginResponse();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName(usernameEditText.getText().toString()); // admin@admin.com // asr1@gmaill.com
        loginRequest.setPassword(passwordEditText.getText().toString());
        loginRequest.setGrant_type("password");
        loginRequest.setRefreshToken("");

        try {
            showProgressDialogue.setProgressDialog(LoginActivity.this,"Please wait...");
            mApiInterface.login("application/json", loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.body() != null) {
                        showProgressDialogue.dismissDialogue();
                        Toast.makeText(LoginActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        if (response.body().getAccessToken()!= null && !response.body().getAccessToken().equals("") ) {
                            //Toast.makeText(LoginActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                            SharedPrefData.saveIntoPrefs(getApplicationContext(),SharedPrefData.ACCESS_Token,response.body().getAccessToken(),0);
                            SharedPrefData.saveIntoPrefs(getApplicationContext(),SharedPrefData.refreshToken,response.body().getRefreshToken(),0);
                            SharedPrefData.saveIntoPrefs(getApplicationContext(),SharedPrefData.userName,response.body().getUserName(),0);
                            SharedPrefData.saveIntoPrefs(getApplicationContext(),SharedPrefData.userId,Integer.toString(response.body().getUserId()),0);
                            SharedPrefData.saveIntoPrefs(getApplicationContext(),SharedPrefData.email,response.body().getEmail(),0);
                            SharedPrefData.saveIntoPrefs(getApplicationContext(),SharedPrefData.roleName,response.body().getRoleName(),0);
                            SharedPrefData.saveIntoPrefs(getApplicationContext(),SharedPrefData.roleId,Integer.toString(response.body().getRoleId()),0);

                            if(response.body().getRoleId()==3)  //warehouse
                            {
                                usernameEditText.setText("");
                                passwordEditText.setText("");
                                Intent myIntent= new Intent(LoginActivity.this, AirportUserActivity.class);
                                startActivity(myIntent);
                               // finish();
                            }else if(response.body().getRoleId()==4)  //hosp user
                            {
                                usernameEditText.setText("");
                                passwordEditText.setText("");
                                Intent myIntent= new Intent(LoginActivity.this, HospUserActivity.class);
                                startActivity(myIntent);
                               // finish();
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,"Error- Unsupported Role Id: "+response.body().getMessage(),Toast.LENGTH_LONG).show();
                            }


                        }
                    }
                    else {
                        showProgressDialogue.dismissDialogue();
                        Toast.makeText(LoginActivity.this,"Not able to login in app",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    showProgressDialogue.dismissDialogue();
                    Toast.makeText(LoginActivity.this,"please enter correct credentials",Toast.LENGTH_LONG).show();

                }
            });

        } catch (Exception ex) {
            showProgressDialogue.dismissDialogue();
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    public static boolean isNetworkAvailable(Context context) {
        if(context == null)  return false;


        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                        return true;
                    }
                }
            }

            else {

                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Log.i("update_statut", "Network is available : true");
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("update_statut", "" + e.getMessage());
                }
            }
        }
        Log.i("update_statut","Network is available : FALSE ");
        return false;
    }

    @Override
    public void PermissionGranted(int request_code) {

    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {

    }

    @Override
    public void PermissionDenied(int request_code) {

    }

    @Override
    public void NeverAskAgain(int request_code) {

    }
}