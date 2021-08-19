package com.example.kontalelektronik_kapi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SetPasswordActivity extends AppCompatActivity {

    RESTInterface rest;
    EditText password, name, surname;
    String phoneNo;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetPasswordActivity.this);
        builder.setTitle("Kayıt işlemini iptal etmek istiyor musunuz?");

        // Set up the buttons
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(SetPasswordActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        Intent intent = getIntent();
        phoneNo = intent.getStringExtra("phone");

        password = findViewById(R.id.passwordText);
        name = findViewById(R.id.nameText);
        surname = findViewById(R.id.surnameText);

        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.completeSignUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });
    }

    private void createUser() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("#ServerURL#")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rest = retrofit.create(RESTInterface.class);
        createPost();
    }

    private void createPost() {
        UserInformations userInfo = new UserInformations("createUser", "+90" + phoneNo, password.getText().toString(), name.getText().toString(), surname.getText().toString(), "user", "0");

        Call<UserInformations> call = rest.createPost(userInfo);
        call.enqueue(new Callback<UserInformations>() {
            @Override
            public void onResponse(Call<UserInformations> call, Response<UserInformations> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(SetPasswordActivity.this, response.code()+" Kullanıcı zaten mevcut", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(SetPasswordActivity.this, response.code()+" Başarılı.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SetPasswordActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<UserInformations> call, Throwable t) {
                Toast.makeText(SetPasswordActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
        });
    }
}
