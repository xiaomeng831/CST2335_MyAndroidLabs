package algonquin.cst2335.xu000247;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
        button2.setOnClickListener(  clk -> {
            Intent call = new Intent(Intent.ACTION_DIAL);
            EditText phoneNumber = findViewById(R.id.editTextPhone);
            call.setData(Uri.parse("tel:" + phoneNumber));
            startActivityForResult(call, 5432);} );

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(  clk -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 3456);} );

        File file = new File("Picture.png");
        if(file.exists()){
            Bitmap theImage = BitmapFactory.decodeFile("Picture.png");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
