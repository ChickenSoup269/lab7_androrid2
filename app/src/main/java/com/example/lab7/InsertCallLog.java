package com.example.lab7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class InsertCallLog extends AppCompatActivity {
    // Insert CallLog
    private static final int PERMISSION_REQUEST_WRITE_CALL_LOG = 1001;
     TextInputLayout txtInputContactName, txtInputContactNumber;
     Button btnAdd, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_call_log);
        addControls();
        addEvents();
    }

    private void addControls(){
        txtInputContactName = (TextInputLayout) findViewById(R.id.txtInputContactName);
        txtInputContactNumber = (TextInputLayout) findViewById(R.id.txtInputContactNumber);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnCancel = (Button) findViewById(R.id.btnCancel);
    }

    private void addEvents(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private boolean validateName(){
        String name = Objects.requireNonNull(txtInputContactName.getEditText()).getText().toString().trim();

        if(name.isEmpty()){
            txtInputContactName.setError("Name không được để trống!");
            return  false;
        }
        else if (name.length() > 15) {
            txtInputContactName.setError("Tên quá dài!");
            return  false;
        }
        else {
            txtInputContactName.setError(null);
            return true;
        }
    }

    private boolean validateNumber() {
        String number = Objects.requireNonNull(txtInputContactNumber.getEditText()).getText().toString().trim();

        if (number.isEmpty()) {
            txtInputContactNumber.setError("Number không được để trống!");
            return  false;
        }
        else if (number.length() > 10) {
            txtInputContactNumber.setError("Số quá dài!");
            return  false;
        }
        else {
            txtInputContactNumber.setError((null));
            return true;
        }
    }

    private void addCallLogEntry() {
        CallLogHelper.insertPlaceholderCall(getContentResolver(),
                Objects.requireNonNull(txtInputContactName.getEditText()).getText().toString(),
                Objects.requireNonNull(txtInputContactNumber.getEditText()).getText().toString());

        // Trả về kết quả thành công và kết thúc InsertCallLog
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();

    }

    public void check(View view){
        if(!validateName() && !validateNumber())  {
            return;
        }
        if(!validateName() || !validateNumber())  {
            return;
        }

        // Kiểm tra quyền WRITE_CALL_LOG
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALL_LOG}, PERMISSION_REQUEST_WRITE_CALL_LOG);
        } else {
            addCallLogEntry();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_WRITE_CALL_LOG) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addCallLogEntry();
            }
        }
    }

}