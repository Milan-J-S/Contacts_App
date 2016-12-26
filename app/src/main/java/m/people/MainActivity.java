package m.people;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static java.lang.Long.getLong;

public class MainActivity extends AppCompatActivity {

    String[] arr = {};

    InputStream in;
    BufferedReader reader;
    String line;
    String name;
    LinearLayout s;
    ArrayList<Card> test= new ArrayList<Card>();
    String[] projection = {ContactsContract.RawContacts.CONTACT_ID,ContactsContract.Contacts.PHOTO_ID};
    Card temp = new Card("test","null",null);
    int flag=0;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        s =(LinearLayout) findViewById(R.id.searchBar);
        Drawable d = getDrawable(R.drawable.back);

        if(flag==0) {
            s.setVisibility(View.VISIBLE);
            TextView searchPhrase = (TextView) findViewById(R.id.search);
            searchPhrase.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchPhrase, InputMethodManager.SHOW_IMPLICIT);
            item.setIcon(d);
            flag =1;
        }

        else {
            s.setVisibility(View.INVISIBLE);
Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            flag = 0;

        }

        return true;


    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    ArrayList<String> del = new ArrayList<>();

        try {
            InputStream inputStream = openFileInput("deleted.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String deleted="";

                StringBuilder stringBuilder = new StringBuilder();

                while ( (deleted = bufferedReader.readLine()) != null ) {

                    del.add(deleted);

                }

                inputStream.close();

            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }






        Cursor phones = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        while (phones.moveToNext())
        {
            name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));


            if(!del.contains(name)) {
                Card temp = new Card(name, number, "con");

                test.add(temp);
            }
            //}

            /*
            Context context = getApplicationContext();



            String text =  String.valueOf("test");
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            */

        }


        phones.close();





        try {
            InputStream inputStream = openFileInput("contactInfo.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String newName = "";
                String newNumber="";
                String photo = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (newName = bufferedReader.readLine()) != null ) {
                    photo = bufferedReader.readLine();
                    newNumber = bufferedReader.readLine();


                    if(!del.contains(newName)) {
                        test.add(new Card(newName,photo,newNumber));
                    }

                }

                inputStream.close();

            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }




        SharedPreferences sh;
        sh = getPreferences(Context.MODE_PRIVATE);


        Set<String> con = new HashSet<String>();
        con = sh.getStringSet("ws",con);
        try
        {
            con.add("a");
        }
        catch(Exception e)
        {

        }





        sort(test);








       final ContactAdapter adapter1 = new ContactAdapter(this,test);



        final ListView listView = (ListView) findViewById(R.id.contacts);
        listView.setAdapter(adapter1);
        //listView.setAdapter(adapter2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

              String name =  adapter1.getName(position);

                String num = adapter1.getNum(position);

                String src = adapter1.getSrc(position);
                showCard(parent,name,num,src);

            }

        });

    }

    public void showCard(View v,String str,String num,String source)
    {
        Intent intent = new Intent(this,ContactCard.class);
        intent.putExtra("name",str);
        intent.putExtra("number",num);
        intent.putExtra("source",source);
        startActivity(intent);


    }

    public void add(View v)
    {
        Intent intent =new Intent (this,AddContact.class);
        startActivity(intent);
    }



    public void sort(ArrayList<Card> test)
    {
        for(int i=0;i<test.size();i++)
            for(int j=0;j<i;j++)
                if(test.get(i).getCardOne().compareToIgnoreCase(test.get(j).getCardOne())<0)
                {
                    Collections.swap(test,i,j);
                }
    }

    public void clean(ArrayList<Card> test)
    {
        for(int i=0;i<test.size()-2;i++)
        {
            String ph = test.get(i).getCardTwo();
            String match = test.get(i+1).getCardTwo();


            if(ph.substring(ph.length()-3).equals(match.substring(match.length()-3)))
                test.remove(i+1);
            match = test.get(i+2).getCardTwo();
            if(ph.substring(ph.length()-3).equals(match.substring(match.length()-3)))
                test.remove(i+2);


        }
    }

    public void search(View v)
    {

        TextView searchPhrase = (TextView) findViewById(R.id.search);
        searchPhrase.requestFocus();
        String nm = String.valueOf(searchPhrase.getText());
        ArrayList<Card> mod = new ArrayList<>();

        for(int i=0;i<test.size();i++) {
            String n = test.get(i).getCardOne();
            if(n.contains(nm))
                mod.add(test.get(i));


        }

        s.setVisibility(View.INVISIBLE);

        final ContactAdapter adapter1 = new ContactAdapter(this,mod);

        final ListView listView = (ListView) findViewById(R.id.contacts);
        listView.setAdapter(adapter1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name =  adapter1.getName(position);

                String num = adapter1.getNum(position);

                String src = adapter1.getSrc(position);
                showCard(parent,name,num,src);

            }

        });
    }





}
