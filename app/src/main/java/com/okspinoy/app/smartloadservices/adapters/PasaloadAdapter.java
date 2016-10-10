package com.okspinoy.app.smartloadservices.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.okspinoy.app.smartloadservices.R;
import com.okspinoy.app.smartloadservices.objects.entities.Pasaload;

import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ceosilvajr on 4/20/15.
 */
public class PasaloadAdapter extends ArrayAdapter<Pasaload> {

    private Context mContext;
    private List<Pasaload> pasaloads;

    public PasaloadAdapter(Context context, List<Pasaload> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.pasaloads = objects;
    }

    static class ViewHolder {
        @InjectView(R.id.pasaload_description)
        TextView description;

        @InjectView(R.id.pasaload_srp)
        TextView srp;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (ViewHolder) view.getTag();
        } else {
            LayoutInflater layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.container_pasaload,
                    parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        Pasaload pasaload = pasaloads.get(position);

        viewHolder.description.setText(pasaload.description.toUpperCase(Locale.ENGLISH));
        viewHolder.srp.setText("SRP: Php " + pasaload.srp + ".00");

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (ViewHolder) view.getTag();
        } else {
            LayoutInflater layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.container_pasaload,
                    parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        Pasaload pasaload = pasaloads.get(position);

        viewHolder.description.setText(pasaload.description.toUpperCase(Locale.ENGLISH));
        viewHolder.srp.setText("SRP: Php " + pasaload.srp + ".00");

        return view;
    }

    @Override
    public int getCount() {
        return pasaloads.size();
    }

    @Override
    public Pasaload getItem(int position) {
        return pasaloads.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
