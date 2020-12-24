package com.example.kontalelektronik_kapi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    TextView infoText;
    UserInformations user;
    RESTInterface rest;
    String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        user = (UserInformations)intent.getSerializableExtra("user");
        setTitle(user.getName().toUpperCase() + " " + user.getSurname().toUpperCase() + " Hoşgeldiniz");

        infoText = findViewById(R.id.infoText);

        if (user.getRole().equals("admin")) {
            findViewById(R.id.permissionButton).setVisibility(View.VISIBLE);
            findViewById(R.id.doorButton).setVisibility(View.VISIBLE);
            info = user.getName() + " " + user.getSurname() + "\n" + "Hoşgeldin Reis";
            infoText.setText(info);
        }else {
            findViewById(R.id.permissionButton).setVisibility(View.INVISIBLE);
            if (user.getPermission().equals("1")) {
                findViewById(R.id.doorButton).setVisibility(View.VISIBLE);
                info = user.getName() + " " + user.getSurname() + "\n" + "Hoşgeldiniz";
                infoText.setText(info);
            }
            else {
                findViewById(R.id.doorButton).setVisibility(View.INVISIBLE);
                info = user.getName() + " " + user.getSurname() + "\n" + "Kapı Aç/Kapat Yetkiniz Yok!";
                infoText.setText(info);
            }
        }

        findViewById(R.id.doorButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //doorFunction();
            }
        });

        findViewById(R.id.permissionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Yetki verilecek numara giriniz");

                // Set up the input
                final EditText input = new EditText(ProfileActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Yetki Ver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        givePermission(input.getText().toString());
                    }
                });
                builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        findViewById(R.id.logoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Çıkış yapmak istiyor musunuz?");
                // Set up the buttons
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();

                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    private void givePermission(String phoneNum) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://34.71.236.81/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rest = retrofit.create(RESTInterface.class);

        UserInformations userInfo = new UserInformations("permission", "+90"+phoneNum);

        Call<UserInformations> call = rest.createPost(userInfo);
        call.enqueue(new Callback<UserInformations>() {
            @Override
            public void onResponse(Call<UserInformations> call, Response<UserInformations> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(ProfileActivity.this, response.code()+" HATA!", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<UserInformations> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    //private void doorFunction() {}
}