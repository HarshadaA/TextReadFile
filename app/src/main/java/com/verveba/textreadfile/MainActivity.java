package com.verveba.textreadfile;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.sax.StartElementListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button save;
    TextView output;
    private static final int PERMISSI_REQUEST_STORAGE =1000;
    private static final int READ_REQUEST_CODE =42;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        output = findViewById(R.id.output);
        final EditText enterText = findViewById(R.id.enterText);
        save = findViewById(R.id.save);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSI_REQUEST_STORAGE);
        }

//        File dir = new File(Environment.getExternalStorageDirectory() +"/" + "ftpdetails");
//        String path = dir.getAbsolutePath() + "/" +"ftpdetails";

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformFileSearch();
            }
        });
    }

    private String readText(String input){
        StringBuilder text = new StringBuilder();
        File file = new File(input);
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) == null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    private void PerformFileSearch(){
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("text/*");
       startActivityForResult(i,READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(data != null){
//            Uri uri =data.getData();
//            String path = uri.getPath();
//            String fileContent = readText(path);
////            path = path.substring(path.indexOf(":") +1);
        File userFile = new File(Environment.getExternalStorageDirectory() +"/" + "ftpdetails");
        String fileContent = readText(userFile.getAbsolutePath());
        Toast.makeText(this, fileContent,Toast.LENGTH_SHORT).show();
            output.setText(readText(fileContent));
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PERMISSI_REQUEST_STORAGE){
            Toast.makeText(MainActivity.this,"Permission Granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this,"Permission not Granted", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}