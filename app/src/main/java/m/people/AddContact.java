package m.people;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.FileOutputStream;
import java.net.URI;

import static android.R.attr.data;

public class AddContact extends AppCompatActivity {

    Uri selectedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        setTitle("New Contact");


    }


    public void save(View v) {
        FileOutputStream outputStream;
        String filename = "contactInfo.txt";
        TextView t = (TextView) findViewById(R.id.editText);
        TextView n = (TextView) findViewById(R.id.editText2);

        String str = String.valueOf(t.getText()) + "\n";
        String num = String.valueOf(n.getText()) + "\n";

        if (str.equals("\n") || num.equals("\n")) {
            Context context = getApplicationContext();
            CharSequence text = "This contact has not been saved as one or more of the informational fields was missing information";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            back(v);
            return;
        }

        try {


            outputStream = openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write(str.getBytes());
            outputStream.write((String.valueOf(selectedImageUri)+"\n").getBytes());
            outputStream.write(num.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        back(v);


    }

    public void back(View v) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void pic(View v) {
        Intent intent = new Intent();
        int PICK_IMAGE_REQUEST = 1;

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                selectedImageUri = data.getData();


                ImageView im = (ImageView) findViewById(R.id.imageView5);
                im.setImageURI(selectedImageUri);

                
            }
        }
    }



}





