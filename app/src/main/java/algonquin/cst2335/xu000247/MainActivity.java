package algonquin.cst2335.xu000247;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/** This program is used to let user input password in the app and check the validation of the password.
 * @author Xiaomeng Xu
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /** This holds the text at the centre of the screen*/
    TextView tv = null;
    /**This holds the edit text for user input which is below the prompt text on the srceen*/
    EditText et = null;
    /**This holds the button below the line of input on the screen*/
    Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        et = findViewById(R.id.editText);
        btn = findViewById(R.id.button);

        btn.setOnClickListener(clk -> {
            String password = et.getText().toString();
            checkPasswordComplexity( password );
            if(checkPasswordComplexity(password) == true){
                tv.setText("Your password meets the requirements.");
            }else {
                tv.setText("You shall not pass!");
            }
        });
    }

    /** This function is used to check whether the password contains an Upper Case letter,
     * a lower case letter, a number, and a special symbol. If the password is incorrect, the
     * screen will pop up some prompt, and show the message "You shall not pass!". Otherwise, it will
     * show the meassage "Your password meets the requirements"
     * @param pw The String object that we are checking if it meets the requirements.
     * @return Returns true if it meets the requirements. Otherwise, returens false.
     */
    boolean checkPasswordComplexity( String pw ){
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for(int i=0; i < pw.length(); i++) {
            char c = pw.charAt(i);
            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            }
            if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            }
            if (Character.isDigit(c)) {
                foundNumber = true;
            }
            if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }

        if(!foundUpperCase) {
            Context context = getApplicationContext();
            CharSequence text = "Missing an upper case letter.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);// Say that they are missing an upper case letter;
            toast.show();
            return false;
        }

        else if( ! foundLowerCase) {
            Context context = getApplicationContext();
            CharSequence text = "Missing a lower case letter.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);// Say that they are missing a lower case letter;
            toast.show();
            return false;
        }
        else if( ! foundNumber) {
            Context context = getApplicationContext();
            CharSequence text = "Missing a number.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return false;
        }
        else if(! foundSpecial) {
            Context context = getApplicationContext();
            CharSequence text = "Missing a special symbol.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return false;
        }
        else {
            return true; //only get here if they're all true
        }
    }

    boolean isSpecialCharacter(char c){
        switch (c){
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }
    }
}