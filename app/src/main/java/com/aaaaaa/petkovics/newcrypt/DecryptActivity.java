package com.aaaaaa.petkovics.newcrypt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DecryptActivity extends AppCompatActivity {
    private Button decryptButton;
    private Button toEncryptActivity;
    private TextView dataTextView;
    private TextView ivTextView;
    private TextView resultTextView;

    public void openDecryptActivity() {
        Intent intent =  new Intent(this, EncryptActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decrypt);

        final Cryptor cryptor = Cryptor.getInstance();
        try {
            cryptor.keygen();
        } catch (Exception e) {}

        decryptButton = findViewById(R.id.decryptButton);
        toEncryptActivity = findViewById(R.id.encryptActivityButton);

        dataTextView = findViewById(R.id.dataTextView);
        ivTextView = findViewById(R.id.ivResultTextView);
        resultTextView = findViewById(R.id.resultTextView);

        decryptButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Map<String, byte[]> map = new HashMap<>();
                //map.put("iv", ivTextView.getText().toString().getBytes(StandardCharsets.US_ASCII));
                //map.put("encrypted", dataTextView.getText().toString().getBytes(StandardCharsets.UTF_8));


                map.put("iv", Base64.decode(ivTextView.getText().toString(), Base64.DEFAULT));
                map.put("encrypted", Base64.decode(dataTextView.getText().toString(), Base64.DEFAULT));



                String str2 = null;
                try {
                    str2 = new String(cryptor.decrypt(map));
                } catch (Exception e) {
                    System.out.println("Decrypt button listener exception: " + e.getMessage());
                }
                resultTextView.setText(str2);



               /* Map<String, byte[]> map2 = new HashMap<>();
                map2.put("iv", Base64.decode(iv, Base64.DEFAULT));
                map2.put("encrypted", Base64.decode(encrypted, Base64.DEFAULT));
                try {
                    String testDecrypted = new String(cryptor.decrypt(map2));
                    System.out.println("Test decrypt: " + testDecrypted);
                } catch (Exception e) {}*/

            }
        });

        toEncryptActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openDecryptActivity();
            }
        });

    }
}
