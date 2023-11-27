package com.example.lab7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Bai1 extends AppCompatActivity {
    // Bài1
    FloatingActionButton fab1;
    Button btnInsert;
    ListView lvCallLog;
    TextView txtDeleteContact;
    ArrayList<MyCallLog> lsCallLog = new ArrayList<>();
    ArrayList<String>lsDataLV = new ArrayList<>();
    ArrayAdapter<String>adapter;
    private static final int REQUEST_CODE_ADD_ENTRY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai1);
        addControls();
        addEvents();
    }

    public void addControls(){
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        btnInsert = (Button) findViewById(R.id.btnInsert);
        lvCallLog = (ListView) findViewById(R.id.lvCalLog);
        txtDeleteContact = (TextView) findViewById(R.id.txtDeleteContact);
    }

    public void addEvents(){
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onBackPressed();}
        });

        txtDeleteContact.setOnClickListener(v -> showConfirmationDialog());

        Cursor curLog;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.READ_CALL_LOG
            }, 101);
        }
        else {
            curLog= CallLogHelper.getAllCallLogs(getContentResolver());
            setCallLogs(curLog);
        }
        addDataForLv();
        adapter = new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,lsDataLV);
        lvCallLog.setAdapter(adapter);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddEntryScreen();
            }
        });
    }

    @SuppressLint("Range")
    private void setCallLogs(Cursor curLog){
        while (curLog.moveToNext()){
            MyCallLog clog = new MyCallLog();

            clog.number = curLog.getString(curLog.getColumnIndex(CallLog.Calls.NUMBER));
            String callName = curLog.getString(curLog.getColumnIndex(CallLog.Calls.CACHED_NAME));

            if (callName == null){
                clog.name = "Unknown";
            }
            else {
                clog.name = callName;
            }
            String callDate = curLog.getString(curLog.getColumnIndex(CallLog.Calls.DATE));
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            clog.date = formatter.format(new Date(Long.parseLong(callDate)));

            String callType = curLog.getString(curLog.getColumnIndex(CallLog.Calls.TYPE));

            if (callType.equals("1")){
                clog.type="Incoming";
            }
            else {
                clog.type ="Outcoming";
            }

            clog.time = curLog.getString(curLog.getColumnIndex(CallLog.Calls.DURATION));
            lsCallLog.add(clog);
        }
    }
    void  addDataForLv(){
        for
        (MyCallLog m:lsCallLog){
            String s = m.toString();
            lsDataLV.add(s);
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa")
                .setMessage("Bạn có muốn xóa hết tất cả các contact không?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAllCallLogs();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void openAddEntryScreen() {
        Intent intent = new Intent(Bai1.this, InsertCallLog.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_ENTRY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_ENTRY && resultCode == RESULT_OK) {
            updateListView();
        }
    }

    private void deleteAllCallLogs() {
        Uri callLogUri = CallLog.Calls.CONTENT_URI;
        int deletedRows = getContentResolver().delete(callLogUri, null, null);
        if (deletedRows > 0) {
            updateListView();
            Toast.makeText(this, "Đã xóa tất cả cuộc gọi", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Không có cuộc gọi để xóa", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateListView() {
        lsCallLog.clear();
        lsDataLV.clear();
        Cursor updatedCursor = CallLogHelper.getAllCallLogs(getContentResolver());
        setCallLogs(updatedCursor);
        addDataForLv();
        adapter.notifyDataSetChanged();
    }

}