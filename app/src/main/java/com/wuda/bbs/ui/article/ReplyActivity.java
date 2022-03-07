package com.wuda.bbs.ui.article;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.DetailArticle;
import com.wuda.bbs.logic.bean.WebResult;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.utils.networkResponseHandler.WebResultHandler;

import java.util.HashMap;
import java.util.Map;

public class ReplyActivity extends AppCompatActivity {

    private DetailArticle repliedArticle;
    private String groupId;
    private String boardId;

    ImageView close_btn;
    ImageView send_btn;
    TextInputEditText content_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repliedArticle = (DetailArticle) getIntent().getSerializableExtra("article");
        groupId = getIntent().getStringExtra("groupId");
        boardId = getIntent().getStringExtra("boardId");
        if (repliedArticle == null)
            finish();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reply);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        setFinishOnTouchOutside(true);

        close_btn = findViewById(R.id.reply_close_imageButton);
        send_btn = findViewById(R.id.reply_send_imageButton);

        content_et = findViewById(R.id.reply_content_inputEditText);

        // 需要加延时，不然无法弹出
        new Handler().postDelayed(new Runnable(){
            public void run() {
                content_et.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(content_et, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);

        eventBinding();
    }

    private void eventBinding() {
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reply();
            }
        });
    }

    private void reply(){
        Editable content = content_et.getText();
        if (content==null) {
            return;
        }

        if (content.length() == 0) {
            new AlertDialog.Builder(ReplyActivity.this)
                    .setTitle("内容不可为空")
                    .setMessage("把你心里的想法告诉我吧！")
                    .setPositiveButton("确定", null)
                    .create()
                    .show();
            return;
        }

        String title = "@" + repliedArticle.getAuthor();
        StringBuilder builder = new StringBuilder();
        builder.append(content);
        builder.append("\n【 在 ").append(repliedArticle.getAuthor()).append(" 的大作中提到: 】\n");
        String[] replyContent = repliedArticle.getContent().split("\\\\n");
        builder.append(": ").append(replyContent[0]);


        // board=&reID=0&font=&subject=&Content=&signature=
        Map<String, String> form = new HashMap<>();
        form.put("board", boardId);
        form.put("reID", repliedArticle.getId());
        form.put("groupID", groupId);
        form.put("reToWho", repliedArticle.getAuthor());
        form.put("font", "");
        form.put("subject", title);
        form.put("Content", builder.toString());
        form.put("signature", "");

        NetworkEntry.postArticle(form, new WebResultHandler() {
            @Override
            public void onResponseHandled(ContentResponse<WebResult> response) {
                if (response.isSuccessful()) {
                    finish();
                }
            }
        });
    }
}