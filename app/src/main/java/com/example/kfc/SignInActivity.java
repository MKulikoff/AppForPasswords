package com.example.kfc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;
    private TextView toggleLoginSignUpTextView;
    private Button loginSignUpButton;
    private boolean loginModeActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        repeatPasswordEditText = findViewById(R.id.repeatPasswordEditText);
        toggleLoginSignUpTextView = findViewById(R.id.toggleLoginSignUpTextView);
        loginSignUpButton = findViewById(R.id.loginSignUpButton);

        loginSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSignUpUser(emailEditText.getText().toString().trim(), passwordEditText.getText().toString().trim());
            }

            ;
        });

    }

    private void loginSignUpUser(String email, String password) {
        if(loginModeActive) {
            if (emailEditText.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please input your Email", Toast.LENGTH_SHORT).show();
            }
            else if (passwordEditText.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please input your password", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startActivity(new Intent(SignInActivity.this, Table.class));
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "Wrong Email or Password, please try again.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                    // ...
                                }

                                // ...
                            }
                        });
            }
        } else {
            if(!passwordEditText.getText().toString().trim().equals(repeatPasswordEditText.getText().toString().trim())) {
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            }  else if (emailEditText.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please input your Email", Toast.LENGTH_SHORT).show();
            }
            else if (passwordEditText.getText().toString().trim().length() < 6) {
                Toast.makeText(this, "Password must be at least 7 characters", Toast.LENGTH_SHORT).show();
            }
            else if (passwordEditText.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please input your password", Toast.LENGTH_SHORT).show();
            }
            else if (repeatPasswordEditText.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show();
            }
            else {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // updateUI(user);
                                    startActivity(new Intent(SignInActivity.this, Table.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "This email has already been registered.",
                                            Toast.LENGTH_SHORT).show();
                                    // updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        }

    }

    public void toggleLoginMode(View view) {
        if (loginModeActive) {
            loginModeActive = false;
            loginSignUpButton.setText("Sign In");
            toggleLoginSignUpTextView.setText("Or tap to Log In");
            repeatPasswordEditText.setVisibility(View.VISIBLE);
        } else {
            loginModeActive = true;
            loginSignUpButton.setText("Log In");
            toggleLoginSignUpTextView.setText("Or, tap to Sign Up");
            repeatPasswordEditText.setVisibility(View.GONE);
        }
    }


}
