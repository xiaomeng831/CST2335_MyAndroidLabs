package algonquin.cst2335.xu000247;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.text.BreakIterator;

public class MainActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );
        Button button = findViewById(R.id.button);
        EditText emailEditText = findViewById(R.id.editText);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("LoginName","");
        emailEditText.setText(emailAddress);
        SharedPreferences.Editor editor = prefs.edit();
        button.setOnClickListener(  clk -> {
            Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);
            nextPage.putExtra("EmailAddress",emailEditText.getText().toString());
            startActivity(nextPage);
            editor.putString("LoginName",emailEditText.getText().toString());
            editor.apply();
        } );


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w( "MainActivity", "In onStart() - making app visible" );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w( "MainActivity", "In onResume() - making app respond to user input" );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w( "MainActivity", "In onPause() - making app no longer respond to user input " );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w( "MainActivity", "In onStop() - making app no longer visible" );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w( "MainActivity", "In onDestroy() - freeing memory " );
    }
}