package com.example.kynashop.view;

import static com.example.kynashop.API.API_Services.BASE_Service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kynashop.API.API_Services;
import com.example.kynashop.R;
import com.example.kynashop.model.KhachHang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CapNhatThongTinActivity extends AppCompatActivity {
    private KhachHang khachHang;
    private ImageView btn_back;
    private TextView title;
    private Button btn_xacthuc,btn_capnhat;
    private EditText number_phone,ten_khachhang,email,diachi,otp;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private AlertDialog alertDialog;
    private FirebaseAuth mAuth;
    private API_Services requestInterface;
    private int Type;
    private ArrayList<KhachHang> ds_khachhang = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_thong_tin);
        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(API_Services.class);
        mAuth = FirebaseAuth.getInstance();
        khachHang = (KhachHang) getIntent().getExtras().getSerializable("KH");
        Type = getIntent().getExtras().getInt("type",-1);
        btn_back = findViewById(R.id.btn_back);
        title = findViewById(R.id.title);
        btn_xacthuc = findViewById(R.id.btn_xacthuc);
        btn_capnhat = findViewById(R.id.btn_capnhat);
        number_phone = findViewById(R.id.number_phone);
        ten_khachhang = findViewById(R.id.ten_khachhang);
        email = findViewById(R.id.email);
        diachi = findViewById(R.id.diachi);
        btn_capnhat.setEnabled(false);
        if(khachHang.getSoDienThoai() != null)
        {
            btn_xacthuc.setEnabled(false);
            number_phone.setEnabled(false);
            number_phone.setText(khachHang.getSoDienThoai());
            btn_capnhat.setEnabled(true);
            btn_xacthuc.setText("Đã xác thực");
        }else {
            btn_xacthuc.setEnabled(true);
            number_phone.setEnabled(true);
            btn_xacthuc.setOnClickListener(xacthuc);
            getCheck();
        }
        String ten = (khachHang.getTenKhachHang() == null)? "" : khachHang.getTenKhachHang();
        String dc = (khachHang.getDiaChi() == null)? "" : khachHang.getDiaChi();
        String mail = (khachHang.getEmail() == null) ? "" : khachHang.getEmail();
        ten_khachhang.setText(ten);
        diachi.setText(dc);
        email.setText(mail);
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
        btn_capnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(diachi.getText().toString().equals(""))
                {
                    Toast.makeText(CapNhatThongTinActivity.this, "Vui lòng nhập địa chỉ nhận hàng", Toast.LENGTH_SHORT).show();
                }else {
                    KhachHang khachHang_update = khachHang;
                    khachHang_update.setTenKhachHang(ten_khachhang.getText().toString());
                    khachHang_update.setSoDienThoai(number_phone.getText().toString());
                    khachHang_update.setDiaChi(diachi.getText().toString());
                    khachHang_update.setEmail(email.getText().toString());
                    CapNhat(khachHang_update);
                }

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void CapNhat(KhachHang khachHangne)
    {
        new CompositeDisposable().add(requestInterface.updateKhachHang(khachHangne)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        );
    }

    private void handleError(Throwable throwable) {
        Log.e("loicapnhatkhachhang", "handleError: " + throwable.getMessage() );
    }

    private void handleResponse(Integer integer) {
        if(integer > 0)
        {
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            if(Type == 005)
            {
                setResult(1809);
            }
            finish();
        }
    }
    public void getCheck()
    {
        new CompositeDisposable().add(requestInterface.GetAllKhachHang()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::check, this::nocheck)
        );
    }

    private void nocheck(Throwable throwable) {
    }

    private void check(ArrayList<KhachHang> khachHangs) {
        ds_khachhang = khachHangs;
    }

    View.OnClickListener xacthuc = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(number_phone.getText().toString().length() >= 10 && number_phone.getText().toString().length() <=12)
            {
//                int x = -1;
//                if(ds_khachhang.size() > 0)
//                {
//                    for(KhachHang kh : ds_khachhang)
//                    {
//                        if((kh.getSoDienThoai() != null) && (kh.getSoDienThoai().equals(number_phone.getText().toString())))
//                        {
//                            x++;
//                            break;
//                        }
//                    }
//                }
//
//                if(x == 0) {
//                    Toast.makeText(CapNhatThongTinActivity.this, "Số Điện đã tồn tại", Toast.LENGTH_SHORT).show();
//                }else {
//                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
//                            .setPhoneNumber("+84" +number_phone.getText().toString())
//                            .setTimeout(60L, TimeUnit.SECONDS)
//                            .setActivity(CapNhatThongTinActivity.this)
//                            .setCallbacks(mCallbacks)
//                            .build();
//                    PhoneAuthProvider.verifyPhoneNumber(options);
//                    dialog_OTP();
//                }
                btn_capnhat.setEnabled(true);
                btn_xacthuc.setText("Đã xác thực");
                btn_xacthuc.setEnabled(false);
                number_phone.setEnabled(false);
            }else {
                Toast.makeText(CapNhatThongTinActivity.this, "Vui lòng nhập đúng định dạng số điện thoại", Toast.LENGTH_SHORT).show();
            }


        }
    };
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
                            Toast.makeText(CapNhatThongTinActivity.this, "Thành Công", Toast.LENGTH_SHORT).show();
                            btn_capnhat.setEnabled(true);
                            btn_xacthuc.setText("Đã xác thực");
                            btn_xacthuc.setEnabled(false);
                            number_phone.setEnabled(false);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("check", "signInWithCredential:failure", task.getException());
                            Toast.makeText(CapNhatThongTinActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                btn_capnhat.setEnabled(false);
                            }
                        }
                    }
                });
    }
}