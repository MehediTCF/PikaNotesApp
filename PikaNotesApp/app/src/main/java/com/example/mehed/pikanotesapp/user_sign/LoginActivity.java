package com.example.mehed.pikanotesapp.user_sign;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mehed.pikanotesapp.MainActivity;
import com.example.mehed.pikanotesapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText inputEmail, inputPass;
    private Button btnLogIn;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        System.out.println(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        inputEmail=(EditText) findViewById(R.id.input_log_email);
        inputPass=(EditText) findViewById(R.id.input_log_pass);
        btnLogIn=(Button) findViewById(R.id.btn_log);
        fAuth=FirebaseAuth.getInstance();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lEmail = inputEmail.getText().toString();
                String lPass=inputPass.getText().toString();
                if(!TextUtils.isEmpty(lEmail) && !TextUtils.isEmpty(lPass))
                {
                    logIn(lEmail, lPass);

                }


            }
        });
    }
    private void logIn(String email, String password)
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in, Please Wait...");
        progressDialog.show();

        fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                    Toast.makeText(LoginActivity.this, "Sign In Successful", Toast.LENGTH_SHORT).show();

                } else
                {
                    Toast.makeText(LoginActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
            break;
        }
        return true;

    }
}
