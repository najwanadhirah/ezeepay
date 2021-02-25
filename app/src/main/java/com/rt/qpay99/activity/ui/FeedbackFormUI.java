package com.rt.qpay99.activity.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rt.qpay99.R;
import com.rt.qpay99.util.SharedPreferenceUtil;

import java.net.URLEncoder;

public class FeedbackFormUI extends AppCompatActivity {

    EditText edFeedback;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form_ui);
        setTitle("Feedback Form");
        edFeedback  =  findViewById(R.id.edFeedback);
        btnSubmit = findViewById(R.id.btnSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(edFeedback.getText().length()>10){
                        String text ="Feedback FROM \nLoginMSISDN: " + SharedPreferenceUtil.getsClientUserName() + " \n\n " +  edFeedback.getText().toString();
                        String url = "https://api.whatsapp.com/send?phone=60166572577&text=" + URLEncoder.encode(text, "UTF-8");
                        Intent i2 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i2);
                    }

                }catch (Exception ec){

                }
            }
        });
    }
}
