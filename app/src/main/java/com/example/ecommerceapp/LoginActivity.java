package com.example.ecommerceapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail,loginPassword;
    private Button loginBtn;
    private ProgressDialog loadingBar;
    private TextView adminPanel,notAdminPanel;

    private DatabaseReference rootRef;
    private FirebaseAuth mAuth;
    private String patentDbName = "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

        rootRef = FirebaseDatabase.getInstance().getReference();

        loginBtn = (Button)findViewById(R.id.login_btn);
        loginPassword = (EditText)findViewById(R.id.password_edit_text);
        loginEmail = (EditText)findViewById(R.id.login_email);
        adminPanel = (TextView)findViewById(R.id.admin_panel_link);
        notAdminPanel = (TextView)findViewById(R.id.not_admin_panel_link);

        loadingBar = new ProgressDialog(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        adminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn.setText("Login Admin");
                adminPanel.setVisibility(View.INVISIBLE);
                notAdminPanel.setVisibility(View.VISIBLE);
                patentDbName = "Admins";

            }
        });
        notAdminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn.setText("Login");
                adminPanel.setVisibility(View.VISIBLE);
                notAdminPanel.setVisibility(View.INVISIBLE);
                patentDbName = "Users";
            }
        });


    }

    private void loginUser() {
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }else {
            loadingBar.setTitle("Login");
            loadingBar.setMessage("Please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            allowAccessToAccount(email,password);
        }
    }

    private void allowAccessToAccount(final String email, final String password) {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    if (patentDbName.equals("Users")){
                        sendUserToHomeActivity();
                        Toast.makeText(LoginActivity.this, "you are Logged In successfully as user", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }else if (patentDbName.equals("Admins")){
                        sendUserToAdminsNewProductActivity();
                        Toast.makeText(LoginActivity.this, "you are Logged In successfully as an admin", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            }
        });


    }

    private void sendUserToHomeActivity() {
        Intent homeIntent = new Intent(LoginActivity.this,HomeActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }
    private void sendUserToAdminsNewProductActivity() {
        Intent homeIntent = new Intent(LoginActivity.this,AdminAddNewProductActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }
}
