package algonquin.cst2335.xu000247;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/** This program is used to let user input password in the app and check the validation of the password.
 * @author Xiaomeng Xu
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {


    Bitmap image;
    /** This string represents the address of the server we will connect to */
    private String stringURL;
    /** This holds the text at the centre of the screen*/
    TextView tv = null;
    /**This holds the edit text for user input which is below the prompt text on the srceen*/
    EditText cityText = null;
    /**This holds the button below the line of input on the screen*/
    Button forecastBtn = null;

    ImageView iv = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityText = findViewById(R.id.cityTextField);
        forecastBtn = findViewById(R.id.forecastButton);

        //week9 code
        forecastBtn.setOnClickListener(clk ->{
            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute(() -> {
                //This runs on another thread
                String cityName = cityText.getText().toString();
                try{
                    stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                            + URLEncoder.encode(cityName, "UTF-8")
                            + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";

                    URL url = new URL(stringURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    String text = (new BufferedReader(
                            new InputStreamReader(in, StandardCharsets.UTF_8)))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    JSONObject theDocument = new JSONObject(text);

                    JSONArray weatherArray = theDocument.getJSONArray("weather");
                    JSONObject position0 = weatherArray.getJSONObject(0);

                    String description = position0.getString("description");
                    String iconName = position0.getString("icon");

                    JSONObject mainObject = theDocument.getJSONObject("main");
                    double current = mainObject.getDouble("temp");
                    double min = mainObject.getDouble("temp_min");
                    double max = mainObject.getDouble("temp_max");
                    int humitidy = mainObject.getInt("humidity");



                    File file = new File(getFilesDir(), iconName + ".png");

                    if(file.exists()){
                        image = BitmapFactory.decodeFile(getFilesDir() + "/" + iconName + ".png");
                    }else{
                        URL imgUrl = new URL("https://openweathermap.org/img/w/" + iconName + ".png");
                        HttpURLConnection imgConnection = (HttpURLConnection) imgUrl.openConnection();
                        imgConnection.connect();
                        int responseCode = imgConnection.getResponseCode();
                        if (responseCode == 200) {
                            image = BitmapFactory.decodeStream(imgConnection.getInputStream());
                            image.compress(Bitmap.CompressFormat.PNG, 100, openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                        }
                        FileOutputStream fOut = null;
                        try {
                            fOut = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                            fOut.flush();
                            fOut.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
/*
                    URL imgUrl = new URL("https://openweathermap.org/img/w/" + iconName + ".png");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(connection.getInputStream());
                        ImageView iv = findViewById(R.id.icon);
                        iv.setImageBitmap(image);
                        iv.setVisibility(View.VISIBLE);
                    }*/



                    runOnUiThread(( ) -> {
                        tv = findViewById(R.id.temp);
                        tv.setText("The current temperature is " + current+ " "+ iconName);
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.maxTemp);
                        tv.setText("The max temperature is " + max);
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.minTemp);
                        tv.setText("The min temperature is " + min);
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.humitidy);
                        tv.setText("The humitidy is " + humitidy + "%");
                        tv.setVisibility(View.VISIBLE);

                        iv = findViewById(R.id.icon);
                        iv.setImageBitmap(image);
                        iv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.description);
                        tv.setText(description);
                        tv.setVisibility(View.VISIBLE);
                    });




                }catch (IOException | JSONException ioe){
                    Log.e("Connection error:", ioe.getMessage());
                }
            });
        });



/*
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
*/
    /** This function is used to check whether the password contains an Upper Case letter,
     * a lower case letter, a number, and a special symbol. If the password is incorrect, the
     * screen will pop up some prompt, and show the message "You shall not pass!". Otherwise, it will
     * show the meassage "Your password meets the requirements"
     * @param pw The String object that we are checking if it meets the requirements.
     * @return Returns true if it meets the requirements. Otherwise, returens false.
     */
/*    boolean checkPasswordComplexity( String pw ){
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
        }   */
    }
}