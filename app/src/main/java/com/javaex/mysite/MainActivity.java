package com.javaex.mysite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // FIELD
    private Button btnSave;
    private EditText edtName;
    private EditText edtPassword;
    private EditText edtContent;

    // CONSTRUCTORS

    // GETTER:SETTER

    // METHOD - 1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = (EditText)findViewById(R.id.edtName);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        edtContent = (EditText)findViewById(R.id.edtContent);

        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String password = edtPassword.getText().toString();
                String content = edtContent.getText().toString();

                Log.d("INFORMATION", "[name= " + name + ", password= " + password + ", content= " + content + "]");

                Toast.makeText(getApplicationContext(),
                        "INFORMATION [name= " + name + ", password= " + password + ", content= " + content + "]",
                        Toast.LENGTH_SHORT).show();
            }
        });

        /*
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String password = edtPassword.getText().toString();
                String content = edtContent.getText().toString();

                Log.d("clickButtonGroup", "" + view.getId());

                if (view.getId() == R.id.btnSave) {
                    Toast.makeText(getApplicationContext(),
                            "INFORMATION [name= " + name + ", password= " + password + ", content= " + content + "]",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(listener)
        */
    }
}