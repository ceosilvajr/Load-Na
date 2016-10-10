package com.okspinoy.app.smartloadservices.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.okspinoy.app.smartloadservices.R;
import com.okspinoy.app.smartloadservices.objects.entities.FAQ;

import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ceosilvajr on 4/20/15.
 */
public class FAQAdapter extends ArrayAdapter<FAQ> {

    private Context mContext;
    private List<FAQ> faqs;

    public FAQAdapter(Context context, List<FAQ> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.faqs = objects;
    }

    static class ViewHolder {
        @InjectView(R.id.faq_question)
        TextView question;

        @InjectView(R.id.faq_answer)
        TextView answer;

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
            view = layoutInflater.inflate(R.layout.container_faq,
                    parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        FAQ faq = faqs.get(position);

        viewHolder.question.setText(faq.question.toUpperCase(Locale.ENGLISH));
        viewHolder.answer.setText(faq.answer);

        return view;
    }

    @Override
    public int getCount() {
        return faqs.size();
    }

    @Override
    public FAQ getItem(int position) {
        return faqs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
