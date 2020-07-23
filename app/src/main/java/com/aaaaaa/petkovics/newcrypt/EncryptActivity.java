package com.aaaaaa.petkovics.newcrypt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

public class EncryptActivity extends AppCompatActivity {
    private Button encryptButton;
    private Button toDecryptActivity;


    private TextView ivResultTextView;
    private TextView resultTextView;
    private TextView dataTextView;

    public void openDecryptActivity() {
        Intent intent =  new Intent(this, DecryptActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encrypt);

        encryptButton = findViewById(R.id.encryptButton);
        toDecryptActivity = findViewById(R.id.decryptActivityButton);

        ivResultTextView = findViewById(R.id.ivResultTextView);
        resultTextView = findViewById(R.id.resultTextView);
        dataTextView = findViewById(R.id.dataTextView);

        toDecryptActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDecryptActivity();
            }
        });

        final Cryptor cryptor = Cryptor.getInstance();
        try {
            cryptor.keygen();

          /*  Map<String, byte[]> map = cryptor.encrypt("passtoencrypt");
            String str1 = Base64.encodeToString(map.get("encrypted"), Base64.DEFAULT);
            System.out.println("Str1: " + str1);

            String str2 = new String(cryptor.decrypt(map));
            System.out.println("Str2: " + str2);*/

        } catch (Exception e) {
            System.out.println("Exception happened in keygen(): " + e.getMessage());
        }

        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Map<String, byte[]> map = cryptor.encrypt(dataTextView.getText().toString());
                    ivResultTextView.setText(Base64.encodeToString(map.get("iv"), Base64.DEFAULT).trim());
                    resultTextView.setText(Base64.encodeToString(map.get("encrypted"), Base64.DEFAULT).trim());
                } catch (Exception e) {
                    System.out.println("Exception in encrypt button listener: " + e.getMessage());
                }
            }
        });
    }
}
