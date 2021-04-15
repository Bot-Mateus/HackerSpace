package com.example.hackerspace;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email, senha;
    Button login;
    TextView fazerRegistro, esqueceuSenha;
    CheckBox lembrarMim;
    ProgressBar progressBar;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email           = findViewById(R.id.editEmail);
        senha           = findViewById(R.id.editPassword);
        login           = findViewById(R.id.btLogin);
        fazerRegistro   = findViewById(R.id.textCriarConta);
        esqueceuSenha   = findViewById(R.id.txtEsqueceuSenha);
        lembrarMim      = findViewById(R.id.checkLembrarMim);
        fAuth           = FirebaseAuth.getInstance();
        progressBar     = findViewById(R.id.progressBar2);


        //Lembrar de Mim ---------------------------------------------------------------------------
        final SharedPreferences sharedPreferences;
        final SharedPreferences.Editor editor;
        sharedPreferences=getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        editor=sharedPreferences.edit();
        String mail = sharedPreferences.getString("email","");
        String passwords = sharedPreferences.getString("passowrd","");
        Boolean checked = sharedPreferences.getBoolean("checked", false);
        lembrarMim.setChecked(checked);
        email.setText(mail);
        senha.setText(passwords);
        //------------------------------------------------------------------------------------------


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String EndEmail = email.getText().toString().trim();
                String password = senha.getText().toString().trim();

                if (TextUtils.isEmpty(EndEmail)){
                    email.setError("Informe seu Email");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    senha.setError("Informe sua senha");
                    return;
                }

                //Lembrar de mim--------------------------------------------------------------------
                if (lembrarMim.isChecked()){
                    Boolean boolChecked = lembrarMim.isChecked();
                    editor.putString("email",email.getText().toString());
                    editor.putString("passowrd",senha.getText().toString());
                    editor.putBoolean("checked", boolChecked);
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Seus dados foram salvos", Toast.LENGTH_SHORT).show();
                }else{
                   sharedPreferences.edit().clear().apply();
                }
                //----------------------------------------------------------------------------------

                progressBar.setVisibility(View.VISIBLE);


                //Autenticar usuario----------------------------------------------------------------
                fAuth.signInWithEmailAndPassword(EndEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Logado com sucesso", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), InicioActivity.class));
                            email.getText().clear();
                            senha.getText().clear();

                        } else {
                            Toast.makeText(LoginActivity.this, "Erro ! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
                //----------------------------------------------------------------------------------


            }
        });


        fazerRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });

        esqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText redefinirEmail = new EditText(view.getContext());
                AlertDialog.Builder redefinirSenhaDialog = new AlertDialog.Builder(view.getContext());
                redefinirSenhaDialog.setTitle("Redefinir a Senha ?");
                redefinirSenhaDialog.setMessage("Informe o seu Email para receber o link para a troca da senha");
                redefinirSenhaDialog.setView(redefinirEmail);




                redefinirSenhaDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Pegue o email para enviar o link para o reset

                        String mail = redefinirEmail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Acesse o link enviado para o seu email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Erro ! Não foi possível enviar o Link" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });




                redefinirSenhaDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Fecha o Dialogo


                    }
                });

                redefinirSenhaDialog.create().show();

            }
        });

    }
    private void getPreferencesData(){

    }
}