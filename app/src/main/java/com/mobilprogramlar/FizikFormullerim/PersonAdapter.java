package com.mobilprogramlar.FizikFormullerim;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class PersonAdapter extends ArrayAdapter<Person> {
    public PersonAdapter(Context context, int resource, List<Person> items) {
        super(context, resource, items);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.formuller_listview, null);
        }
        Person p = getItem(position);
        if (p != null) {
            ImageView personImage = (ImageView)v.findViewById(R.id.imageView_personpic);
            TextView personName = (TextView)v.findViewById(R.id.textView_personname);
            TextView personAdress = (TextView)v.findViewById(R.id.textView_address);
            //gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            if(personImage != null && personName != null && personAdress != null){
                personAdress.setText(p.getAddress()+" ("+p.getResimSayisi()+" sayfa)");
                personName.setText(p.getName());
                personImage.setImageResource(p.getPictureResourceID());
            }
        }
        return v;
    }
}