package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    int count=0;
    EditText mInputEt;
    Button mSaveBtn;
    EditText mInputEt_sp;
    Button mSaveBtn_sp;
    EditText mInputEt_t;
    Button mSaveBtn_t;
    String mText;
    String mText_sp;
    String mText_t;
    String dd;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    FloatingActionButton sendEmail;

    Spinner spinner;
    ArrayList<String> spinnerArray;
    ArrayAdapter<String> adapter;
    Spinner spinner_sp;
    ArrayList<String> spinnerArray_sp;
    ArrayAdapter<String> adapter_sp;
    Spinner spinner_t;
    ArrayList<String> spinnerArray_t;
    ArrayAdapter<String> adapter_t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = MainActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.black));

        // Initialize views
        mInputEt = findViewById(R.id.inputEt);
//        mSaveBtn = findViewById(R.id.saveBtn);
        mInputEt_sp = findViewById(R.id.inputEt_sp);
//        mSaveBtn_sp = findViewById(R.id.saveBtn_sp);
        mInputEt_t = findViewById(R.id.inputEt_t);
        mSaveBtn_t = findViewById(R.id.saveBtn_t);
        sendEmail = findViewById(R.id.sendEmail);

        mInputEt.setText("h");
        mInputEt_sp.setText("");
        mInputEt_t.setText("");

//        For Sub-Project
        spinner = findViewById(R.id.spinner_languages);
        spinnerArray = new ArrayList<>();
        spinnerArray.add("");

        spinner_sp = findViewById(R.id.spinner_languages_sp);
        spinnerArray_sp = new ArrayList<>();
        spinnerArray_sp.add("");

        spinner_t = findViewById(R.id.spinner_languages_t);
        spinnerArray_t = new ArrayList<>();
        spinnerArray_t.add("");

        readCSV();

        adapter = new ArrayAdapter<>(MainActivity.this, android.support.design.R.layout.support_simple_spinner_dropdown_item,spinnerArray);
        // Set default value in drop down
        spinner.setAdapter(adapter);

        // spinnerArray.add("Android");
        adapter_sp = new ArrayAdapter<>(MainActivity.this, android.support.design.R.layout.support_simple_spinner_dropdown_item,spinnerArray_sp);
        // Set default value in drop down
        spinner_sp.setAdapter(adapter_sp);

        adapter_t = new ArrayAdapter<>(MainActivity.this, android.support.design.R.layout.support_simple_spinner_dropdown_item,spinnerArray_t);
        // Set default value in drop down
        spinner_t.setAdapter(adapter_t);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // To get the selected value
                mText = (String) adapterView.getItemAtPosition(i);
                // To set the value which user select from the drop down list
                mInputEt.setText(mText);
                Toast.makeText(MainActivity.this,""+adapterView.getItemAtPosition(i),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(MainActivity.this, "Please Select!", Toast.LENGTH_SHORT).show();
            }
        });

        spinner_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // To get the selected value
                mText_sp = (String) adapterView.getItemAtPosition(i);
                // To set the value which user select from the drop down list
                mInputEt_sp.setText(mText_sp);
                Toast.makeText(MainActivity.this,""+adapterView.getItemAtPosition(i),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(MainActivity.this, "Please Select!", Toast.LENGTH_SHORT).show();
            }
        });

        spinner_t.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // To get the selected value
                mText_t = (String) adapterView.getItemAtPosition(i);
                // To set the value which user select from the drop down list
                mInputEt_t.setText(mText_t);
                Toast.makeText(MainActivity.this,""+adapterView.getItemAtPosition(i),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(MainActivity.this, "Please Select!", Toast.LENGTH_SHORT).show();
            }
        });


//        mSaveBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Input text form EditTextd
//                mText = mInputEt.getText().toString().trim();   // .trim() remove spaces before and after text
//                // Validate
//                if (mText.isEmpty()){   // User hasn't enter anything
//                    Toast.makeText(MainActivity.this,"Please enter something!",Toast.LENGTH_SHORT).show();
//                }
//                else{   // User has entered string data
//                        // If user OS > marshmellow we need runtime permission
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
//                        PackageManager.PERMISSION_DENIED) {
//                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                            // Show popup for runtime permission
//                            requestPermissions(permissions,WRITE_EXTERNAL_STORAGE_CODE);
//                        }
//                        else {
//                            // Permission already granted, save data
//                            if (mText_sp == null || mText_t == null ) {
//                                mText_sp = "";
//                                mText_t = "";}
//                            saveToTxtFile(mText, mText_sp, mText_t);
//                        }
//                    }
//                    else {
//                        // System OS is < marshmellow, no need to runtime permission, save data
//                        if (mText_sp == null || mText_t == null ) {
//                            mText_sp = "";
//                            mText_t = "";}
//                        saveToTxtFile(mText, mText_sp, mText_t);
//                    }
//                }
//            }
//        });

//        mSaveBtn_sp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Input text form EditTextd
//                mText_sp = mInputEt_sp.getText().toString().trim();   // .trim() remove spaces before and after text
//                // Validate
//                if (mText_sp.isEmpty()){   // User hasn't enter anything
//                    Toast.makeText(MainActivity.this,"Please enter something!",Toast.LENGTH_SHORT).show();
//                }
//                else{   // User has entered string data
//                    // If user OS > marshmellow we need runtime permission
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
//                                PackageManager.PERMISSION_DENIED) {
//                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                            // Show popup for runtime permission
//                            requestPermissions(permissions,WRITE_EXTERNAL_STORAGE_CODE);
//                        }
//                        else {
//                            // Permission already granted, save data
//                            if (mText == null || mText_t == null ) {
//                                mText = "";
//                                mText_t = "";}
//                            saveToTxtFile(mText, mText_sp, mText_t);
//                        }
//                    }
//                    else {
//                        // System OS is < marshmellow, no need to runtime permission, save data
//                        if (mText == null || mText_t == null ) {
//                            mText = "";
//                            mText_t = "";}
//                        saveToTxtFile(mText, mText_sp, mText_t);
//                    }
//                }
//            }
//        });
//        Handle button click
        mSaveBtn_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Input text form EditText
                mText = mInputEt.getText().toString().trim();
                mText_sp = mInputEt_sp.getText().toString().trim();
                mText_t = mInputEt_t.getText().toString().trim();   // .trim() remove spaces before and after text

                // Validate
                if (mText.isEmpty() && mText_sp.isEmpty() && mText_t.isEmpty()){   // User hasn't enter anything
                    Toast.makeText(MainActivity.this,"Please enter something!",Toast.LENGTH_SHORT).show();
                }
                else{   // User has entered string data
                    // If user OS > marshmellow we need runtime permission
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                PackageManager.PERMISSION_DENIED) {
                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            // Show popup for runtime permission
                            requestPermissions(permissions,WRITE_EXTERNAL_STORAGE_CODE);
                        }
                        else {
                            if (mText == null || mText_sp == null ) {
                                mText = "";
                                mText_sp = "";}
                                // Permission already granted, save data
                                saveToTxtFile(mText, mText_sp, mText_t);
                        }
                    }
                    else {
                        // System OS is < marshmellow, no need to runtime permission, save data
                        if (mText == null || mText_sp == null) {
                            mText = "";
                            mText_sp = "";}
                            saveToTxtFile(mText, mText_sp, mText_t);
                    }
                }
            }
        });

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PopActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case WRITE_EXTERNAL_STORAGE_CODE:{
                // If request is cancelled, the result arrays are empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Permission was granted, save data
                    saveToTxtFile(mText, mText_sp, mText_t);
                }
                else {
                    // Permission was denied, show toast
                    Toast.makeText(this,"Storage permission is required to store data",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


        private void readCSV(){
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.getDefault()).format(System.currentTimeMillis());
        String fileName = "MyFile_" + timeStamp + ".csv";
        String path = (Environment.getExternalStorageDirectory()) + "/My Files/" + fileName;
        String line = "";
        try {
            spinnerArray.clear();
            spinnerArray_sp.clear();
            spinnerArray_t.clear();

            BufferedReader br = new BufferedReader(new FileReader(path));
            String[] values = new String[0];
            System.out.println(values);

            Map<String,String> mp = new HashMap<>();
            Map<String,String> mp_sp = new HashMap<>();
            Map<String,String> mp_t = new HashMap<>();
            br.readLine();
            while ((line = br.readLine()) != null) {
                values = line.split(",");
                mp.put(values[0],values[0]);
              //  if(values[1]!="" && values[1]!=null)
                mp_sp.put(values[1],values[1]);
             //   if(values[2]!="" && values[2]!=null)
                mp_t.put(values[2],values[2]);
            }

            //entry.getValue()!=null && entry.getValue()!="")
            for (Map.Entry<String,String> entry : mp.entrySet()){
                if(!entry.getValue().isEmpty()){
                spinnerArray.add(entry.getValue());
                }
            }
            for (Map.Entry<String,String> entry : mp_sp.entrySet()){
                if(!entry.getValue().isEmpty()){
                spinnerArray_sp.add(entry.getValue());
                }
            }
            for (Map.Entry<String,String> entry : mp_t.entrySet()){
                if(!entry.getValue().isEmpty()){
                spinnerArray_t.add(entry.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveToTxtFile(String mText, String mText_sp, String mText_t) {
        // Get current time for filename
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(System.currentTimeMillis());
        String timeStamp2 = new SimpleDateFormat(" HH:mm:ss",
                Locale.getDefault()).format(System.currentTimeMillis());
        System.out.println(timeStamp);
        try {
            mText = mText.toUpperCase() + ","+ mText_sp +","+ mText_t +","+ timeStamp2;
            // Path tp storage
            File path = Environment.getExternalStorageDirectory();
            // Create folder named "My Files"
            File dir = new File(path + "/My Files/");
            dir.mkdirs();
            // File name
            String fileName = "MyFile_" + timeStamp + ".csv";   //e.g. --> MyFile_20220412_152322.txt

            String p = "PROJECT";
            String sp = "SUB-PROJECT";
            String t = "TASKS";
            String ti = "TIME";
            String header = (p + "," + sp + "," + t + "," + ti);
            header += "\n";
            File file = new File(dir, fileName);

            if (!file.exists()) {
               // File file = new File(dir, fileName);
                file.createNewFile();
                // FileWriter class is used to store characters in file

                mText += "\n";

                FileWriter fileWritter = new FileWriter(file,true);
                if (count==0) {
                    fileWritter.write(header);
                    count++;
                }
                fileWritter.write(mText);
                fileWritter.close();
            }
            else {
              //  File file = new File(dir, fileName);

                mText += "\n";
                // FileWriter class is used to store characters in file
                FileWriter fw = new FileWriter(file,true);
                fw.write(mText);
                fw.close();
//                BufferedWriter bw = new BufferedWriter(fw);
//                bw.(mText);
//                bw.close();
            }

            // Show filename and path where file is saved
            Toast.makeText(this,fileName+" is saved to\n" + dir,Toast.LENGTH_SHORT).show();
            readCSV();
        }
        catch (Exception e) {
            // If anything goes wrong
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}