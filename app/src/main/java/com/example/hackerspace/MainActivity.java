package com.example.hackerspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText nome, email, senha;
    Button registrar;
    TextView possuiConta;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nome        = findViewById(R.id.editUserName);
        email       = findViewById(R.id.editEmail);
        senha       = findViewById(R.id.editPassword);
        registrar   = findViewById(R.id.btRegistrar);
        possuiConta = findViewById(R.id.txtPossuiConta);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }


        registrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String EndEmail = email.getText().toString().trim();
                String password = senha.getText().toString().trim();

                if (TextUtils.isEmpty(EndEmail)){
                    email.setError("Informe seu Email");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    senha.setError("Insira uma senha");
                    return;
                }

                if (password.length() < 6){
                    senha.setError("Sua senha deve conter 6 ou mais caracteres");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //Registrando usuario no Firebase

                fAuth.createUserWithEmailAndPassword(EndEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Usuario criado", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));

                    } else {
                            Toast.makeText(MainActivity.this, "Erro ! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        possuiConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

    }
}