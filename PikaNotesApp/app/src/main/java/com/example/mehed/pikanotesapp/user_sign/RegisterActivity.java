package com.example.mehed.pikanotesapp.user_sign;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private DatabaseReference fusersDatabase;
    private Button btnReg;
    private EditText inName,inEmail, inPass;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnReg = (Button) findViewById(R.id.btn_reg);
        inName =(EditText) findViewById(R.id.input_reg_name);
        inEmail=(EditText) findViewById(R.id.input_reg_email);
        inPass=(EditText) findViewById(R.id.input_reg_pass);
        fAuth=FirebaseAuth.getInstance();
        fusersDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname=inName.getText().toString();
                String umail=inEmail.getText().toString();
                String upass=inPass.getText().toString();
                registerUser(uname,umail,upass);

            }
        });
    }
    private void registerUser(final String name, String email, String password)
    {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Processing Your Request, Please Wait...");
        progressDialog.show();
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    fusersDatabase.child(fAuth.getCurrentUser().getUid()).child("Basic").child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                                Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                


                            } else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "ERROR" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "ERROR"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
