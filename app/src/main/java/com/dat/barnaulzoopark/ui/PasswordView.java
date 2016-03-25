package com.dat.barnaulzoopark.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.dat.barnaulzoopark.R;

/**
 * Created by DAT on 20-Mar-16.
 */
public class PasswordView extends FrameLayout {

    protected EditText password;

    protected ImageButton reveal;

    public PasswordView(Context context) {
        super(context);
        init();
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.password_view, this);
        password = (EditText) findViewById(R.id.editTextPassword);
        password.setTypeface(Typeface.MONOSPACE);
        reveal = (ImageButton) findViewById(R.id.imageButtonShowPassword);
        reveal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                revealPasswordQuick();
            }
        });
    }

    private void revealPasswordPermanent() {
        if (!password.getText().toString().isEmpty()) {
            int cursorPos = password.getSelectionStart();
            if (password.getInputType() == (InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
                password.setInputType(
                        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            password.setTypeface(Typeface.MONOSPACE);
            password.setSelection(cursorPos);
        }
    }

    private void revealPasswordQuick() {
        if (!password.getText().toString().isEmpty()) {
            final int cursorPos = password.getSelectionStart();
            final Handler handler = new Handler();
            password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            password.setTypeface(Typeface.MONOSPACE);
            password.setSelection(cursorPos);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //turn password back to * mask after 250ms
                    password.setInputType(
                            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setTypeface(Typeface.MONOSPACE);
                    if (cursorPos < password.getText().length()) {
                        password.setSelection(cursorPos);
                    } else {
                        //handle IndexOutOfBoundsException setSpan(...)  ends beyond length ...
                        password.setSelection(password.getText().length());
                    }
                }
            }, 250);
        }
    }

}
