package com.okspinoy.app.smartloadservices.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.okspinoy.app.smartloadservices.R;
import com.okspinoy.app.smartloadservices.objects.entities.Transaction;
import com.okspinoy.app.smartloadservices.helpers.TransactionConstants;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ceosilvajr on 4/20/15.
 */
public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private Context mContext;
    private List<Transaction> mHistories;

    public TransactionAdapter(Context context, List<Transaction> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.mHistories = objects;
    }

    static class ViewHolder {
        @InjectView(R.id.history_summary)
        TextView historySummary;

        @InjectView(R.id.history_date)
        TextView historyDate;

        @InjectView(R.id.history_status)
        ImageView historyStatus;

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
            view = layoutInflater.inflate(R.layout.container_transaction,
                    parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        Transaction bigBroTransaction = mHistories.get(position);

        viewHolder.historySummary.setText(bigBroTransaction.name);
        viewHolder.historyDate.setText(bigBroTransaction.getHumanReadableTime());

        if (bigBroTransaction.transactionType.equals(TransactionConstants.REGISTRATION.toString())) {
            viewHolder.historyStatus.setImageResource(R.drawable.btn_request_off);
        }

        if (bigBroTransaction.transactionType.equals(TransactionConstants.BORROWED.toString())) {
            viewHolder.historyStatus.setImageResource(R.drawable.btn_requested_off);
        }

        if (bigBroTransaction.transactionType.equals(TransactionConstants.PAID.toString())) {
            viewHolder.historyStatus.setImageResource(R.drawable.btn_paid_off);
        }

        return view;
    }

    @Override
    public int getCount() {
        return mHistories.size();
    }

    @Override
    public Transaction getItem(int position) {
        return mHistories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
