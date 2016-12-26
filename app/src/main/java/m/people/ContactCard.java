package m.people;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.ArrayList;


public class ContactCard extends AppCompatActivity{


    //String n;
    String phoneNumber;
    String phoneNumber2="";
    String m ="";
    LinearLayout t;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.contactcard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {






        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        delete();


                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this contact?")
                .setIcon(R.drawable.delete)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();






        return true;

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_card);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        Bundle bundle = getIntent().getExtras();
        CharSequence str = bundle.getCharSequence("name");
        ArrayList<String> numbers = new ArrayList<String>();
        CharSequence num = bundle.getCharSequence("number");
        String src = bundle.getString("source");

       // n=String.valueOf(num);

        String ret = null;

        if(src.equals("con"))
        {

            String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + str +"%'";
            String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor c = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection, selection, null, null);
            while (c.moveToNext()) {
                ret = c.getString(0);
                numbers.add(ret);
            }
            c.close();
            if(ret==null)
                ret = "Unsaved";}

        else
        {
            numbers.add(String.valueOf(src));
        }

        t = (LinearLayout) this.findViewById(R.id.mailLayout);



        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + str +"%'";
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Email.ADDRESS};
        Cursor c = this.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                projection, selection, null, null);
        if(c.moveToFirst()) {
            m = c.getString(0);
            while (c.moveToNext()) {
                m = c.getString(0);
            }
        }

        if(!m.equals("")) {
            TextView mail = (TextView) findViewById(R.id.mailId);
            mail.setText(m);

            t.setVisibility(View.VISIBLE);
        }








        TextView name = (TextView) findViewById(R.id.contactName);
        name.setText(str);
        
         phoneNumber = numbers.get(numbers.size()-1);


        TextView number = (TextView) findViewById(R.id.number);
        number.setText(phoneNumber);

        if(numbers.size()>1)
        if(!numbers.get(numbers.size()-2).equals(null))
        {
            phoneNumber2=numbers.get(numbers.size()-2);
            TextView number2 = (TextView) findViewById(R.id.number2);
            number2.setText(phoneNumber2);

            LinearLayout p2 = (LinearLayout) findViewById(R.id.phone2);
            p2.setVisibility(View.VISIBLE);


            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);




            p.addRule(RelativeLayout.BELOW, R.id.phone2);

            p.setMargins(0,20,0,0);

            t.setLayoutParams(p);



        }

        ImageView pic = (ImageView) findViewById(R.id.contactPhoto);


        if(num!=null)
            pic.setImageURI(Uri.parse(String.valueOf(num)));

        else
            pic.setImageResource(R.drawable.person);




    }


    public void call(View v)
    {
        String ph;
        if(v.getId()==R.id.imageView3)
            ph = phoneNumber;
        else
            ph = phoneNumber2;

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+ph));
        startActivity(intent);

    }

    public void sms(View v) {
        String ph;
        if(v.getId()==R.id.imageView)
            ph = phoneNumber;
        else
            ph = phoneNumber2;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        intent.putExtra("address",ph);
        startActivity(intent);

    }

    public void whatsapp(View v)
    {
        
        Intent sendIntent = new Intent("android.intent.action.MAIN");
        sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
        String ph;
        if(v.getId()==R.id.imageView4)
            ph = phoneNumber;
        else
            ph = phoneNumber2;

        if(ph.startsWith("+"))
        {
            ph=ph.substring(3,ph.length());
            if(ph.startsWith(" "))
                ph=ph.substring(1,ph.length());
        }

        sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators("91"+ph)+"@s.whatsapp.net");

        startActivity(sendIntent);

    }

    public void mail(View v)
    {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/html");
        intent.setData(Uri.parse("mailto:"));
        String[] t ={m};
        intent.putExtra(Intent.EXTRA_EMAIL,t);
        startActivity(intent);
    }

public void delete()
{
    TextView nameV = (TextView) findViewById(R.id.contactName);
    String name = String.valueOf(nameV.getText())+"\n";

    try {


        OutputStream outputStream = openFileOutput("deleted.txt", Context.MODE_APPEND);
        outputStream.write(name.getBytes());

        outputStream.close();
    } catch (Exception e) {
        e.printStackTrace();
    }


    Intent intent = new Intent(this,MainActivity.class);
    startActivity(intent);
}




}
