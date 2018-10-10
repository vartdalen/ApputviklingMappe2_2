package vica.apputviklingmappe2;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnTextChangedListener implements TextWatcher {

    private EditText editText;
    private EditText editText2;
    private TextView textView;
    private Pattern pattern;
    private String message;
    private String message2;

    public OnTextChangedListener(EditText editText, EditText editText2, TextView textView, String regex, String message, String message2) {
        super();
        this.editText = editText;
        this.editText2 = editText2;
        this.textView = textView;
        this.pattern = Pattern.compile(regex);
        this.message = message;
        this.message2 = message2;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(editText2 != null && editText2.getText().toString().length() > 0 && !editText.getText().toString().equals(editText2.getText().toString())) {
            textView.setText(message2);
            return;
        }

        Matcher matcher = pattern.matcher(editText.getText());
        boolean match = matcher.matches();

        if (match) {
            textView.setText("");
        } else {
            textView.setText(message);
        }
    }
}
