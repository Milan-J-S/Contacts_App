package m.people;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static android.net.Uri.parse;

/**
 * Created by Milan on 21-12-2016.
 */

public class ContactAdapter extends ArrayAdapter<Card> {




    public ContactAdapter(Context context, ArrayList<Card> card) {
        super(context,0, card);
    }

    @Override
    public View getView(int pos, View v, ViewGroup parent)
    {
       View  listView = v;
        if(listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_contacts, parent, false);
        }

        Card current = getItem(pos);


        TextView name = (TextView) listView.findViewById(R.id.textView2);
        //TextView number = (TextView) listView.findViewById(R.id.textView3);

        ImageView photo = (ImageView) listView.findViewById(R.id.thumbnail);


        if(current.getCardTwo()!=null)
        photo.setImageURI(Uri.parse(current.getCardTwo()));

        else
        photo.setImageResource(R.drawable.person);


        name.setText(current.getCardOne());
        //number.setText(current.getCardTwo());


        return listView;
    }

    public String getName(int pos)
    {
        Card current = getItem(pos);

        return(current.getCardOne());
    }

    public String getNum(int pos)
    {
        Card current = getItem(pos);

        return(current.getCardTwo());
    }

    public String getSrc(int pos)
    {
        Card current = getItem(pos);

        return(current.getSource());
    }




    @Nullable
    @Override
    public Card getItem(int position) {
        return super.getItem(position);
    }
}
