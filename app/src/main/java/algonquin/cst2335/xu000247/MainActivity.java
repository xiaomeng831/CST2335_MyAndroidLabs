package algonquin.cst2335.xu000247;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.textView);
        EditText et = findViewById(R.id.editText);
        Button btn = findViewById(R.id.button);

        btn.setOnClickListener(clk -> {
            String password = et.getText().toString();

            checkPasswordComplexity( password );
        });
    }

    /** This function is used to check whether the password contains an Upper Case letter,
     * a lower case letter, a number, and a special symbol.
     *
     * @param pw The String object that we are checking
     * @return Returns true if....
     */
    boolean checkPasswordComplexity( String pw ){
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        if(!foundUpperCase) {
            Toast.makeText("Need a Uppercase letter") ;// Say that they are missing an upper case letter;
            return false;
        }

        else if( ! foundLowerCase) {
            Toast.makeText( ... ); // Say that they are missing a lower case letter;
            return false;
        }
        else if( ! foundNumber) { ....}
        else if(! foundSpecial) { ... }
        else
            return true; //only get here if they're all true
    }
}