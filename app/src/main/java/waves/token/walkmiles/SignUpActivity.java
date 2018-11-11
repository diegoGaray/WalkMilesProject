package waves.token.walkmiles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jdev.countryutil.Constants;
import com.jdev.countryutil.CountryUtil;
import waves.token.walkmiles.activities.MainActivity;
import waves.token.walkmiles.models.User;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity{
    private static final String TAG = "SignupActivity";

    @BindView(waves.token.walkmiles.R.id.input_email) EditText _emailText;
    @BindView(waves.token.walkmiles.R.id.input_wallet) EditText _walletText;
    //@BindView(R.id.input_gender) EditText _sexoText;
    @BindView(waves.token.walkmiles.R.id.input_country) EditText _countryText;
    @BindView(waves.token.walkmiles.R.id.input_city) EditText _cityText;
    @BindView(waves.token.walkmiles.R.id.input_password) EditText _passwordText;
    @BindView(waves.token.walkmiles.R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(waves.token.walkmiles.R.id.btn_signup) Button _signupButton;
    @BindView(waves.token.walkmiles.R.id.datePicker) DatePicker _dateBirth;
    @BindView(waves.token.walkmiles.R.id.link_login) TextView _loginLink;
    SimpleDateFormat simpleDateFormat;
    private String gender = "other";
    private boolean checked2;

    FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(waves.token.walkmiles.R.layout.activity_signup);
        ButterKnife.bind(this);
        checked2 = false;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _countryText.setFocusable(false);
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _countryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CountryUtil(SignUpActivity.this).setTitle("Select Country").build();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d("SESION", "Sesion iniciada con email: "+user.getEmail());

                }else{
                    Log.d("SESION", "Sesion cerrada");
                }
            }
        };
    }

    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = (CheckBox) findViewById(waves.token.walkmiles.R.id.checkbox);
        if(checkBox.isChecked()){
            checked2 = true;
        }else{
            checked2 = false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.KEY_RESULT_CODE) {
            try {
                _countryText.setText(data.getStringExtra(Constants.KEY_COUNTRY_NAME));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAuthListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case waves.token.walkmiles.R.id.rbtnMale:
                if (checked)
                    gender = ((RadioButton) view).getText().toString();
                    break;
            case waves.token.walkmiles.R.id.rbtnFemale:
                if (checked)
                    gender = ((RadioButton) view).getText().toString();
                    break;
        }
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }


        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                waves.token.walkmiles.R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        final String wallet = _walletText.getText().toString();
        //final String gender = _sexoText.getText().toString();
        //final String birth = _birthText.getText().toString();
        final String pais = _countryText.getText().toString();
        final String city = _cityText.getText().toString();
        int day = _dateBirth.getDayOfMonth();
        int month = _dateBirth.getMonth() +1;
        int year = _dateBirth.getYear();
        final String birth = String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year);
        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Log.i("SESION", "usuario creado correctamente");
                                    User us = new User();
                                    us.setEmail(email);
                                    us.setWallet(wallet);
                                  //  us.setGender(gender);
                                    us.setBirth(birth);
                                    us.setCountry(pais);
                                    us.setCity(city);
                                    us.setGender(gender);
                                    mDatabase.child("users").child(FirebaseAuth.getInstance().getUid()).setValue(us);
                                    onSignupSuccess();
                                }else{
                                    Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_SHORT).show();
                                    Log.i("SESION", task.getException().getMessage());
                                }
                            }
                        });
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_SHORT).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String wallet = _walletText.getText().toString();
      //  String gender = _sexoText.getText().toString();
      //  String birth = _birthText.getText().toString();
        String pais = _countryText.getText().toString();
        String city = _cityText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (wallet.isEmpty()) {
            _walletText.setText("wallet");
            valid = true;
        } else {
            _walletText.setError(null);
        }

       /* if (gender.isEmpty()) {
            _sexoText.setError("Enter Valid Gender");
            valid = false;
        } else {
            _sexoText.setError(null);
        }*/


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        /*if (birth.isEmpty()) {
            _birthText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _birthText.setError(null);
        }*/

        if (pais.isEmpty()) {
            _countryText.setError("Enter Valid Country");
            valid = false;
        } else {
            _countryText.setError(null);
        }

        if (city.isEmpty()) {
            _cityText.setError("Enter Valid City");
            valid = false;
        } else {
            _cityText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            _passwordText.setError("between 6 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 6 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        if (checked2){
        }else {
            Toast.makeText(SignUpActivity.this, "You have to authorize the registration", Toast.LENGTH_SHORT).show();
            valid = false;
        }


        return valid;
    }
}