package com.wuda.bbs.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wuda.bbs.R;
import com.wuda.bbs.utils.validator.TextValidator;

import java.util.HashMap;
import java.util.Map;

public class FindPasswordByArtificialFragment extends FindPasswordFragment {

    private TextInputEditText uid_et;
    private TextInputLayout uid_tl;
    private TextInputEditText name_et;
    private TextInputLayout name_tl;
    private TextInputEditText idNumber_et;
    private TextInputLayout idNumber_tl;
    private TextInputEditText studentNumber_et;
    private TextInputLayout studentNumber_tl;
    private TextInputEditText email_et;
    private TextInputLayout email_tl;
    private TextInputEditText comment_et;
    private TextInputLayout comment_tl;
    Button submit_btn;

    private FindPasswordByArtificialViewModel mViewModel;

    public static FindPasswordByArtificialFragment newInstance() {
        return new FindPasswordByArtificialFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_password_by_artificial_fragment, container, false);

        uid_et = view.findViewById(R.id.findPasswordByArtificial_uid_inputEditText);
        uid_tl = view.findViewById(R.id.findPasswordByArtificial_uid_textInputLayout);
        name_et = view.findViewById(R.id.findPasswordByAuthInfo_uid_inputEditText);
        name_tl = view.findViewById(R.id.findPasswordByArtificial_name_textInputLayout);
        idNumber_et = view.findViewById(R.id.findPasswordByArtificial_idNumber_inputEditText);
        idNumber_tl = view.findViewById(R.id.findPasswordByArtificial_idNumber_textInputLayout);
        studentNumber_et = view.findViewById(R.id.findPasswordByArtificial_studentNumber_inputEditText);
        studentNumber_tl = view.findViewById(R.id.findPasswordByArtificial_studentNumber_textInputLayout);
        email_et = view.findViewById(R.id.findPasswordByArtificial_email_inputEditText);
        email_tl = view.findViewById(R.id.findPasswordByArtificial_email_textInputLayout);
        comment_et = view.findViewById(R.id.findPasswordByArtificial_comment_inputEditText);
        comment_tl = view.findViewById(R.id.findPasswordByArtificial_comment_textInputLayout);
        submit_btn = view.findViewById(R.id.findPasswdByArtificial_submit_button);

        eventBinding();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FindPasswordByArtificialViewModel.class);
    }

    private void eventBinding() {
        uid_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (uid_et.getText()!=null && TextValidator.isUidValid(uid_et.getText().toString())) {
                    uid_tl.setError(null);
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

        email_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (email_et.getText()!=null && TextValidator.isEmailValid(email_et.getText().toString())) {
                    email_tl.setError(null);
                }
                return false;
            }
        });
        // tgt=reqpwd&uid=abc&name=&sfzh=&xh=&email=abc%40abc.abc&bz=
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable uid = uid_et.getText();
                if (uid==null || uid.length()<2) {
                    uid_tl.setError("请填写正确的ID");
                    return;
                }

                Editable name = name_et.getText();
                Editable idNumber = idNumber_et.getText();
                Editable studentNumber = studentNumber_et.getText();

                Editable email = email_et.getText();
                if (email==null || !TextValidator.isEmailValid(email.toString())) {
                    email_tl.setError("请填写有效的邮箱地址");
                    return;
                }

                Editable comment = comment_et.getText();

                // tgt=reqpwd&uid=abc&name=&sfzh=&xh=&email=abc%40abc.abc&bz=
                Map<String, String> form = new HashMap<>();

                form.put("tgt", "reqpwd");
                form.put("uid", uid.toString());
                form.put("name", name==null? "": name.toString());
                form.put("sfzh", idNumber==null? "": idNumber.toString());
                form.put("xh", studentNumber==null? "": studentNumber.toString());
                form.put("email", email.toString());
                form.put("bz", comment==null? "": comment.toString());

                submit(form);
            }
        });
    }
}