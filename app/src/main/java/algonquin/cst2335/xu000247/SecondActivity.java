package algonquin.cst2335.xu000247;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        TextView textView = findViewById(R.id.textView3);
        textView.setText("Welcome back " + emailAddress);

        Button button2 = findViewById(R.id.button2);

        EditText emailTextPhone = findViewById(R.id.editTextPhone);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String tel = prefs.getString("tel","");
        emailTextPhone.setText(tel);

        button2.setOnClickListener(  clk -> {
            Intent call = new Intent(Intent.ACTION_DIAL);
            EditText phoneNumber = findViewById(R.id.editTextPhone);
            call.setData(Uri.parse("tel:" + phoneNumber.getText().toString()));
            startActivityForResult(call, 5432); } );


        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(  clk -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 3456);} );

        File file = new File(getFilesDir().getAbsolutePath() + "/Picture.png");
        if(file.exists()){
            Bitmap theImage = BitmapFactory.decodeFile(getFilesDir().getAbsolutePath() + "/Picture.png");
            ImageView profileImage = findViewById(R.id.imageView);
            profileImage.setImageBitmap(theImage);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap thumbnail = data.getParcelableExtra("data");
        if(requestCode == 3456){
            if(resultCode == RESULT_OK){
                ImageView profileImage = findViewById(R.id.imageView);
                profileImage.setImageBitmap(thumbnail);
            }
        }

        FileOutputStream fOut = null;
        try {
            fOut = openFileOutput( "Picture.png", Context.MODE_PRIVATE);
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EditText phoneNumber = findViewById(R.id.editTextPhone);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("tel",phoneNumber.getText().toString());
        editor.apply();


    }
}
