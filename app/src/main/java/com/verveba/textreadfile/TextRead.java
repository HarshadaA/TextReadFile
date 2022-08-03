package com.verveba.textreadfile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class TextRead extends AppCompatActivity {

    Button btnChooseAFile;
    TextView txtip,txtuserid,txtpass,txtno,txt_csv;

    private static final int READ_REQUEST_CODE = 42;

    ActivityResultLauncher<Intent> filePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_read);
        btnChooseAFile = findViewById(R.id.btnChooseFile);
        txtip = findViewById(R.id.txtip);
        txtuserid = findViewById(R.id.txtuserid);
        txtpass = findViewById(R.id.txtpass);
        txtno = findViewById(R.id.txtno);
        txt_csv = findViewById(R.id.txt_csv);

        txtip.setMovementMethod(new ScrollingMovementMethod());

        filePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent intent1 = result.getData();

                        Uri uri = intent1.getData();

                        byte[] byteData = getBytes(TextRead.this, uri);

                        String TxtReadResult = (new String(byteData));

                        String[] names = TxtReadResult.split("\r\n", 5); // "1" means stop splitting after one space
                        txtip.setText("UserIP:- " + names[0]);
                        txtuserid.setText("txtuserid:- "+ names[1]);
                        txtpass.setText("txtpass:- " + names[2]);
                        txtno.setText("txtno:- "+ names[3]);
                        txt_csv.setText("txt_csv:- " +names[4]);

                    }
                });
        btnChooseAFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseFile();
            }
        });
    }

    public void ChooseFile() {
        try {
            Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
            fileIntent.setType("text/*");
            filePicker.launch(fileIntent);
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
            Toast.makeText(TextRead.this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

    byte[] getBytes(Context context, Uri uri) {
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toByteArray();
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage().toString());
            Toast.makeText(context, "getBytes error:" + ex.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }
}