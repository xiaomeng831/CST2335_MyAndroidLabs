package algonquin.cst2335.xu000247;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView mytext = findViewById(R.id.textview);
        Button mybutton = findViewById(R.id.mybutton);
        EditText myedit = findViewById(R.id.myedittext);
        CheckBox myCheckBox = findViewById(R.id.mycheckbox);
        Switch mySwitch = findViewById(R.id.myswitch);
        RadioButton myRadioButton = findViewById(R.id.myradiobutton);
        ImageView myimage = findViewById(R.id.logo_alqonquin);
        ImageButton imgbtn = findViewById(R.id.myimagebutton);

        if(mybutton != null)mybutton.setOnClickListener( vw -> {
            String editString = myedit.getText().toString();
            mytext.setText("Your edit text has: " + editString);});

        myCheckBox.setText("Do you drink coffee?");
        mySwitch.setText("Do you drink tea?");
        myRadioButton.setText("Do you drink beer?");

        myCheckBox.setOnCheckedChangeListener((btn,isChecked) -> {
            Context context = getApplicationContext();
            CharSequence text = "You clicked on the Checkbox and it is now " + isChecked;
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        });
        mySwitch.setOnCheckedChangeListener((btn,isChecked) -> {
            Context context = getApplicationContext();
            CharSequence text = "You clicked on the Switch and it is now " + isChecked;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        });
        myRadioButton.setOnCheckedChangeListener((btn,isChecked) -> {
            Context context = getApplicationContext();
            CharSequence text = "You clicked on the Radio Button and it is now " + isChecked;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        });

        imgbtn.setOnClickListener( vw -> {
            Context context = getApplicationContext();
            CharSequence text = "The width = " + imgbtn.getWidth() + " and height = " + imgbtn.getHeight();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        });





    }




}