package com.mileston.walk.walkmilestonandroid.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mileston.walk.walkmilestonandroid.R;
import com.mileston.walk.walkmilestonandroid.SignUpActivity;
import com.mileston.walk.walkmilestonandroid.models.UserUpdate;

import butterknife.BindView;

public class UpdateActivity extends AppCompatActivity {
    private static final String TAG = "UpdateActivity";

    private EditText _emailText;
    private EditText _walletText;
    private EditText _sexoText;
    private EditText _countryText;
    private EditText _cityText;
    private EditText _dateBirth;
    private EditText _pass;

    FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseUser usFire = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        _emailText = (EditText) findViewById(R.id.input_email);
        _walletText = (EditText) findViewById(R.id.input_wallet);
        _sexoText = (EditText) findViewById(R.id.input_gender);
        _countryText = (EditText) findViewById(R.id.input_country);
        _cityText = (EditText) findViewById(R.id.input_city);
        _pass = (EditText) findViewById(R.id.input_pass_update);
        _dateBirth = (EditText) findViewById(R.id.input_birth);

        _countryText.setFocusable(false);
        _emailText.setFocusable(false);
        _sexoText.setFocusable(false);
        _cityText.setFocusable(false);
        _pass.setFocusable(false);
        _dateBirth.setFocusable(false);
        _walletText.setFocusable(false);

        mDatabase.child("users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        UserUpdate usUpdate = dataSnapshot.getValue(UserUpdate.class);
                        // [START_EXCLUDE]
                        if (usUpdate == null) {
                            Toast.makeText(UpdateActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            _emailText.setText(usFire.getEmail());
                            _walletText.setText(usUpdate.getWallet());
                            _sexoText.setText(usUpdate.getGender());
                            _dateBirth.setText(usUpdate.getBirth());
                            _countryText.setText(usUpdate.getCountry());
                            _cityText.setText(usUpdate.getCity());
                        }
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        android.util.Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });

        clickUpdate();
        clickUpdatePass();
        clickUpdateWallet();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    public void clickUpdateWallet(){
        _walletText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateActivity.this);
                LayoutInflater inflater = UpdateActivity.this.getLayoutInflater();
                View mView = inflater.inflate(R.layout.dialog_wallet, null);
                final EditText etWallet = (EditText)mView.findViewById(R.id.wallet_dialog);
                alertDialog.setTitle("WALLET");
                alertDialog.setMessage("Enter Wallet");
                alertDialog.setView(mView)
                        // Add action buttons
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                final String textValue=etWallet.getText().toString();
                                if (textValue.isEmpty()) {
                                    Toast.makeText(UpdateActivity.this, "Enter Valid Wallet", Toast.LENGTH_SHORT).show();

                                } else {
                                    _walletText.setError(null);
                                    mDatabase.child("users").child(FirebaseAuth.getInstance().getUid()).child("wallet").setValue(textValue);
                                    Toast.makeText(UpdateActivity.this, "Wallet Change", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });

    }

    public void clickUpdatePass(){
        _pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateActivity.this);
                LayoutInflater inflater = UpdateActivity.this.getLayoutInflater();
                View mView = inflater.inflate(R.layout.dialog_pass, null);
                final EditText etPassOld = (EditText)mView.findViewById(R.id.old_pass);
                final EditText etPassNew = (EditText)mView.findViewById(R.id.new_pass);
                alertDialog.setTitle("PASSWORD");
                alertDialog.setView(mView)
                        .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Editable textValue = input.getText();
                                final String textValueOld = etPassOld.getText().toString();
                                final String textValueNew = etPassNew.getText().toString();
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                // Get auth credentials from the user for re-authentication
                                if (textValueNew.equals("") || textValueOld.equals("")) {
                                    Toast.makeText(UpdateActivity.this, "Enter Pass", Toast.LENGTH_SHORT).show();
                                } else {
                                    AuthCredential credential = EmailAuthProvider
                                            .getCredential(user.getEmail(), textValueOld); // Current Login Credentials \\
                                    // Prompt the user to re-provide their sign-in credentials
                                    user.reauthenticate(credential)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        user.updatePassword(textValueNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d(TAG, "Password updated");
                                                                    Toast.makeText(UpdateActivity.this, "Password updated", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Log.d(TAG, "Error password not updated");
                                                                    Toast.makeText(UpdateActivity.this, "Error password not updated", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        Log.d(TAG, "Error auth failed");
                                                        Toast.makeText(UpdateActivity.this, "Error auth failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                }
                            }

                        });
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });
    }

    public void clickUpdate(){
        _emailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateActivity.this);
                LayoutInflater inflater = UpdateActivity.this.getLayoutInflater();
                View mView = inflater.inflate(R.layout.dialog_email, null);
                final EditText etEmail= (EditText)mView.findViewById(R.id.email_dialog);
                final EditText etPassEmail = (EditText)mView.findViewById(R.id.password_dialog_email);
                alertDialog.setTitle("EMAIL");
                alertDialog.setView(mView).setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final String text = etEmail.getText().toString();
                                final String textpass = etPassEmail.getText().toString();
                                if (text.equals("") || textpass.equals("")) {
                                    Toast.makeText(UpdateActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                                } else {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    // Get auth credentials from the user for re-authentication
                                    AuthCredential credential = EmailAuthProvider
                                            .getCredential(user.getEmail(), textpass); // Current Login Credentials \\
                                    // Prompt the user to re-provide their sign-in credentials
                                    user.reauthenticate(credential)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.d(TAG, "User re-authenticated.");
                                                    //Now change your email address \\
                                                    //----------------Code for Changing Email Address----------\\
                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                    user.updateEmail(text)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Log.d(TAG, "User email address updated.");
                                                                        Toast.makeText(UpdateActivity.this, "Email update", Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        Log.d(TAG, "Error Email not updated");
                                                                        Toast.makeText(UpdateActivity.this, "Error Email not updated", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                    //----------------------------------------------------------\\
                                                }
                                            });

                                    dialog.dismiss();
                                }

                            }
                        });
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();

            }

        });
    }

}
