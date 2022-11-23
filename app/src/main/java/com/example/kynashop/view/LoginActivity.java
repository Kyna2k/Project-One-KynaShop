package com.example.kynashop.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kynashop.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private Button btn_dangnhap;
    private EditText number_phone,otp;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private AlertDialog alertDialog;
    private CardView btn_google,btn_facebook;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Code nay là để cái chữ ở thang statucbar có màu đen (thanh thông báo)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        btn_dangnhap = findViewById(R.id.btn_dangnhap);
        number_phone = findViewById(R.id.number_phone);
        btn_google = findViewById(R.id.btn_google);
        btn_facebook = findViewById(R.id.btn_facebook);
        mAuth = FirebaseAuth.getInstance();
        btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(number_phone.getText().toString().length() >= 10 && number_phone.getText().toString().length() <=12)
//                {
//                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
//                            .setPhoneNumber("+84" +number_phone.getText().toString())
//                            .setTimeout(60L, TimeUnit.SECONDS)
//                            .setActivity(LoginActivity.this)
//                            .setCallbacks(mCallbacks)
//                            .build();
//                    PhoneAuthProvider.verifyPhoneNumber(options);
//                    dialog_OTP();
//                }else {
//                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đúng định dạng số điện thoại", Toast.LENGTH_SHORT).show();
//                }

            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                //
                Log.d("check", "onVerificationCompleted:" + credential);
                Log.d("yes", " " + credential.getSmsCode());
                if(credential.getSmsCode()!=null)
                {
                    otp.setText(credential.getSmsCode());
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.e("Check", "onCodeSent: " + s );
                mVerificationId = s;
                mResendToken = forceResendingToken;
            }
        };
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("Facebook", "onSuccess: " + loginResult.toString());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        btn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void dialog_OTP()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.diaglog_otp,null);
        otp = view.findViewById(R.id.otp);
        Button btn_xacthuc = view.findViewById(R.id.btn_xacthuc);
        TextView phone_number = view.findViewById(R.id.phone_number);
        phone_number.setText("+84 " + number_phone.getText().toString());
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn_xacthuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOTP(otp.getText().toString());

            }
        });
    }
    private void getOTP(String s)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,s);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("check", "signInWithCredential:success");
                            alertDialog.dismiss();
                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            Toast.makeText(LoginActivity.this, "Thành Công", Toast.LENGTH_SHORT).show();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("check", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}