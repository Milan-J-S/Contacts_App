package m.people;

import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Milan on 21-12-2016.
 */
public class Card implements Adapter {

    private String one;
    private String two;
    private String source;

    public Card(String a, String b,String p)
    {
        one = a;
        two = b;
        source = p;
    }

    public ArrayList<Card> sort(ArrayList<Card> con)
    {
        Card temp;
        for(int i=0;i<con.size();i++)
            for(int j=0;j<i;j++)
                if(con.get(i).getCardOne().compareToIgnoreCase(con.get(j).getCardOne())<0)
                {
                    Collections.swap(con,i,j);
                }




        return con;
    }

    public String getCardOne()
    {
        return one;
    }


    public String getCardTwo()
    {
        return two;
    }

    public String getSource(){return source;}

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public String getItem(int position) {
        return (one);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
