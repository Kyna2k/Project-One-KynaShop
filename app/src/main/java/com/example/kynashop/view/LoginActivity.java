package com.example.kynashop.view;

import static com.example.kynashop.API.API_Services.BASE_Service;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kynashop.API.API_Services;
import com.example.kynashop.LoadingSreen.LoadingScreen;
import com.example.kynashop.R;
import com.example.kynashop.model.KhachHang;
import com.example.kynashop.model.LoginModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
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
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private API_Services requestInterface;
    private KhachHang khachHang_getFromFB_GG;
    private String name,email,image;
    private int type = 1;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(API_Services.class);
        //Code nay là để cái chữ ở thang statucbar có màu đen (thanh thông báo)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        btn_dangnhap = findViewById(R.id.btn_dangnhap);
        number_phone = findViewById(R.id.number_phone);
        btn_google = findViewById(R.id.btn_google);
        btn_facebook = findViewById(R.id.btn_facebook);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(number_phone.getText().toString().length() >= 10 && number_phone.getText().toString().length() <=12)
                {
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                            .setPhoneNumber("+84" +number_phone.getText().toString())
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(LoginActivity.this)
                            .setCallbacks(mCallbacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                    dialog_OTP();
                }else {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đúng định dạng số điện thoại", Toast.LENGTH_SHORT).show();
                }
//                LoadingScreen.LoadingShow(LoginActivity.this,"Đang đăng nhập");
//                LoginModel loginModel = new LoginModel(number_phone.getText().toString(),1);
//                type = 1;
//                login(loginModel);

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
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                checkLogin.launch(signInIntent);
            }
        });

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("Facebook", "onSuccess: " + loginResult.toString());
                getUserProfile(loginResult.getAccessToken());
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
    private void login(LoginModel loginModel)
    {
        new CompositeDisposable().add(requestInterface.login(loginModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        );
    }

    private void handleError(Throwable throwable) {
        Toast.makeText(this, "Lỗi đăng nhập", Toast.LENGTH_SHORT).show();
        Log.e("loilogin", "handleError: " + throwable.getMessage() );
        try {
            LoadingScreen.LoadingDismi();

        }catch (Exception E)
        {

        }
    }

    private void handleResponse(KhachHang khachHang) {
        khachHang_getFromFB_GG = khachHang;
        if(type != 1)
        {
            khachHang_getFromFB_GG.setTenKhachHang(name);
            khachHang_getFromFB_GG.setAvatar(image);
            CapNhat(khachHang_getFromFB_GG);
        }
        SharedPreferences sharedPreferences = getSharedPreferences("KhachHach",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("makhachhang",khachHang.getMaKhachHang());
        editor.apply();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",khachHang);
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        try {
            LoadingScreen.LoadingDismi();

        }catch (Exception E)
        {

        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
    private void getUserProfile(AccessToken accessToken) {
        LoadingScreen.LoadingShow(LoginActivity.this,"Đang đăng nhập");
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    name = object.getString("name");
                    email = object.getString("id");
                    image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    type = 3;
                    LoginModel loginModel = new LoginModel(email,3);
                    login(loginModel);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        request.executeAsync();
    }
    public void CapNhat(KhachHang khachHangne)
    {
        new CompositeDisposable().add(requestInterface.updateKhachHang(khachHangne)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::capnhat, this::loicapnhat)
        );
    }

    private void loicapnhat(Throwable throwable) {
        Log.e("loicapnhatkhachhang", "handleError: " + throwable.getMessage() );
    }

    private void capnhat(Integer integer) {
        if(integer > 0)
        {
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        }
    }

    private void dialog_OTP()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.diaglog_otp,null);
        otp = view.findViewById(R.id.otp);
        Button btn_xacthuc = view.findViewById(R.id.btn_xacthuc);
        TextView phone_number = view.findViewById(R.id.phone_number);
        ImageView cancel = view.findViewById(R.id.cancel);
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
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
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
                            login(new LoginModel(number_phone.getText().toString(),1));
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
    ActivityResultLauncher<Intent> checkLogin = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            //đăng nhập thành công
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            name = account.getDisplayName();
                            image = account.getPhotoUrl().toString();
                            type =2;
                            String email_gg = account.getEmail();
                            LoginModel loginModel = new LoginModel(email_gg,2);
                            LoadingScreen.LoadingShow(LoginActivity.this,"Đang đăng nhập");
                            login(loginModel);
                        } catch (ApiException e) {
                            //đăng nhập thất bại
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

}