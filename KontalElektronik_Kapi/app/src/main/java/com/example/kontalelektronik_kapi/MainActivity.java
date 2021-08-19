package com.example.kontalelektronik_kapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText phoneEntered, passwordEntered;

    private RESTInterface rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneEntered = findViewById(R.id.phoneNumber);
        passwordEntered = findViewById(R.id.passwordText);

        findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneEntered.getText().toString().isEmpty()) {
                    phoneEntered.setError("Geçerli bir telefon numarası giriniz!");
                    phoneEntered.requestFocus();
                    return;
                }
                if (passwordEntered.getText().toString().isEmpty()) {
                    passwordEntered.setError("Şifre giriniz!");
                    passwordEntered.requestFocus();
                    return;
                }
                login();
            }
        });

        findViewById(R.id.signupButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                intent.putExtra("phone", phoneEntered.getText().toString());
                startActivity(intent);
            }
        });
    }

    public void login() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("#ServerURL#")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rest = retrofit.create(RESTInterface.class);
        createPost();
    }

    private void createPost() {
        UserInformations userInfo = new UserInformations("login", "+90"+phoneEntered.getText().toString(), passwordEntered.getText().toString());

        Call<UserInformations> call = rest.createPost(userInfo);
        call.enqueue(new Callback<UserInformations>() {
            @Override
            public void onResponse(Call<UserInformations> call, Response<UserInformations> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(MainActivity.this, response.code()+" HATA!", Toast.LENGTH_LONG).show();
                    return;
                }

                UserInformations userInfoResponse = response.body();

                if (userInfoResponse.getMessage().equals("1")) {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    UserInformations userActualInfo = new UserInformations(userInfoResponse.getMessage(), userInfoResponse.getpNum(), userInfoResponse.getPassword(), userInfoResponse.getName(), userInfoResponse.getSurname(), userInfoResponse.getRole(), userInfoResponse.getPermission());
                    intent.putExtra("user", userActualInfo);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "Hatalı telefon veya şifre!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserInformations> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
        });
    }
}
