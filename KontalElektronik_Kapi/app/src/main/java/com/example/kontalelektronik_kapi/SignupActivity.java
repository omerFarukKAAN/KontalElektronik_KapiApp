package com.example.kontalelektronik_kapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        String phoneNumber = getIntent().getStringExtra("phone");

        phone = findViewById(R.id.phoneText);
        phone.setText(phoneNumber);

        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNo = phone.getText().toString();

                if (phoneNo.isEmpty()) {
                    phone.setError("Geçerli bir telefon numarası giriniz!");
                    phone.requestFocus();
                    return;
                }

                Intent intent = new Intent(SignupActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("phone", phoneNo);
                startActivity(intent);
            }
        });
    }
}