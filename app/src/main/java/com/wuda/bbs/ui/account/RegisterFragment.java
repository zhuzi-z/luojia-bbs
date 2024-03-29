package com.wuda.bbs.ui.account;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.base.BaseFragment;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;
import com.wuda.bbs.utils.validator.TextValidator;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends BaseFragment {

    private TextInputEditText uid_et;
    private TextInputLayout uid_tl;
    private TextInputEditText password_et;
    private TextInputLayout password_tl;
    private TextInputEditText password2_et;
    private TextInputLayout password2_tl;
    private TextInputEditText nickname_et;
    private TextInputLayout nickname_tl;
    private TextInputEditText realName_et;
    private TextInputLayout realName_tl;
    private TextInputEditText campusId_et;
    private TextInputLayout campusId_tl;
    private TextInputEditText idNumber_et;
    private TextInputLayout idNumber_tl;
    private TextInputEditText birthday_et;
    private TextInputLayout birthday_tl;
    private TextInputEditText email_et;
    private TextInputLayout email_tl;
    private TextInputEditText phoneNumber_et;
    private TextInputLayout phoneNumber_tl;
    private RadioButton gender_man_btn;
    private RadioButton gender_woman_btn;
    private Button submit_btn;

    private RegisterViewModel mViewModel;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);

        uid_et = view.findViewById(R.id.register_uid_inputTextEdit);
        uid_tl = view.findViewById(R.id.register_uid_textInputLayout);
        password_et = view.findViewById(R.id.register_password_inputTextEdit);
        password_tl = view.findViewById(R.id.register_password_textInputLayout);
        password2_et = view.findViewById(R.id.register_password2_inputTextEdit);
        password2_tl = view.findViewById(R.id.register_password2_textInputLayout);
        nickname_et = view.findViewById(R.id.register_nickname_inputTextEdit);
        nickname_tl = view.findViewById(R.id.register_nickname_textInputLayout);
        realName_et =view.findViewById(R.id.register_realName_inputTextEdit);
        realName_tl = view.findViewById(R.id.register_realName_textInputLayout);
        campusId_et = view.findViewById(R.id.register_campusId_inputTextEdit);
        campusId_tl = view.findViewById(R.id.register_campusId_textInputLayout);
        idNumber_et = view.findViewById(R.id.register_idNumber_inputTextEdit);
        idNumber_tl = view.findViewById(R.id.register_idNumber_textInputLayout);
        birthday_et = view.findViewById(R.id.register_birthday_inputTextEdit);
        birthday_tl = view.findViewById(R.id.register_birthday_textInputLayout);
        email_et = view.findViewById(R.id.register_email_inputTextEdit);
        email_tl = view.findViewById(R.id.register_email_textInputLayout);
        phoneNumber_et = view.findViewById(R.id.register_phoneNumber_inputTextEdit);
        phoneNumber_tl = view.findViewById(R.id.register_phoneNumber_textInputLayout);
        gender_man_btn = view.findViewById(R.id.register_gender_man_btn);
        gender_woman_btn = view.findViewById(R.id.register_gender_woman_btn);
        submit_btn = view.findViewById(R.id.register_submit_btn);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        showActionBar("注册");

        eventBinding();
    }

    private void eventBinding() {


        mViewModel.getResponseLiveData().observe(getViewLifecycleOwner(), new Observer<ContentResponse<String>>() {
            @Override
            public void onChanged(ContentResponse<String> contentResponse) {
                new AlertDialog.Builder(getContext())
                        .setMessage(Html.fromHtml(contentResponse.getContent()))
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create()
                        .show();
            }
        });

        mViewModel.getErrorResponseMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ContentResponse<?>>() {
            @Override
            public void onChanged(ContentResponse<?> contentResponse) {
                new ResponseErrorHandlerDialog(getContext())
                        .addErrorResponse(contentResponse)
                        .setOnRetryButtonClickedListener(new BaseCustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                register();
                            }
                        })
                        .show();
            }
        });

        uid_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (uid_et.getText()!=null && TextValidator.isUidValid(uid_et.getText().toString())) {
                    uid_tl.setError(null);
                }
                return false;
            }
        });

        password_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (password_et.getText()!=null && TextValidator.isPasswordValid(password_et.getText().toString())) {
                    password_tl.setError(null);
                }
                return false;
            }
        });

        password2_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Editable password = password_et.getText();
                Editable password2 = password2_et.getText();
                if (password!=null && password2!=null && password.toString().equals(password2.toString())) {
                    password2_tl.setError(null);
                }
                return false;
            }
        });

        campusId_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (campusId_et.getText()!=null && TextValidator.isCampusIdValid(campusId_et.getText().toString())) {
                    campusId_tl.setError(null);
                }
                return false;
            }
        });

        idNumber_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (idNumber_et.getText()!=null && TextValidator.isIdNumberValid(idNumber_et.getText().toString())) {
                    idNumber_tl.setError(null);
                }
                return false;
            }
        });

        birthday_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String date = sdf.format(selection);
                        birthday_et.setText(date);
                    }
                });

                datePicker.show(getParentFragmentManager(), datePicker.toString());
            }
        });

        email_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (email_et.getText()!=null && TextValidator.isEmailValid(email_et.getText().toString())) {
                    email_tl.setError(null);
                }
                return false;
            }
        });

        phoneNumber_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (phoneNumber_et.getText()!=null && TextValidator.isPhoneNumberValid(phoneNumber_et.getText().toString())) {
                    phoneNumber_tl.setError(null);
                }
                return false;
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        Editable uid = uid_et.getText();
        if (uid==null || !TextValidator.isUidValid(uid.toString())) {
            uid_tl.setError("请填写符合要求的ID");
            return;
        }

        Editable password = password_et.getText();
        if (password==null || !TextValidator.isPasswordValid(password.toString())) {
            password_tl.setError("请填写符合要求的密码");
            return;
        }

        Editable password2 = password2_et.getText();
        if (password2==null || !password2.toString().equals(password.toString())) {
            password2_tl.setError("两次密码不匹配");
            return;
        }

        Editable nickname = nickname_et.getText();

        Editable realName = realName_et.getText();
        if (realName==null || realName.length()<2) {
            realName_tl.setError("请输入真实姓名");
            return;
        }

        Editable campusId = campusId_et.getText();
        if (campusId==null || !TextValidator.isCampusIdValid(campusId.toString())) {
            campusId_tl.setError("请输入有效的凭证");
            return;
        }

        Editable idNumber = idNumber_et.getText();
        if (idNumber==null || !TextValidator.isIdNumberValid(idNumber.toString())) {
            idNumber_tl.setError("请输入正确的身份证号");
            return;
        }

        Editable email = email_et.getText();
        if (email==null || !TextValidator.isEmailValid(email.toString())) {
            email_tl.setError("请输入有效邮箱");
            return;
        }

        Editable phoneNumber = phoneNumber_et.getText();
        if (phoneNumber==null || !TextValidator.isPhoneNumberValid(phoneNumber.toString())) {
            phoneNumber_tl.setError("请输入联系电话");
            return;
        }

        String gender;
        if (gender_man_btn.isChecked()) {
            gender = "男";
        } else {
            gender = "女";
        }

        Editable birthday = birthday_et.getText();
        String birthday_year = "";
        String birthday_month = "";
        String birthday_day = "";
        if (birthday!=null) {
            String[] date = birthday.toString().split("-");
            if (date.length == 3) {
                birthday_year = date[0];
                birthday_month = date[1];
                birthday_day = date[2];
            }
        }

        // userid=abc&pass1=&pass2=&username=&realname=&xh=&sfzh=&gender=&year=&month=&day=&reg_email=&phone=
        Map<String, String> form = new HashMap<>();
        form.put("userid", uid.toString());
        form.put("pass1", password.toString());
        form.put("pass2", password2.toString());
        form.put("username", nickname==null? "": nickname.toString());
        form.put("realname", realName.toString());
        form.put("xh", campusId.toString());
        form.put("sfzh", idNumber.toString());
        form.put("gender", gender);
        form.put("year", birthday_year);
        form.put("month", birthday_month);
        form.put("day", birthday_day);
        form.put("reg_email", email.toString());
        form.put("phone", phoneNumber.toString());

        mViewModel.register(form);
    }
}