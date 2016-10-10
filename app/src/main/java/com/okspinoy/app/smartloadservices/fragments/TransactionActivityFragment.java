package com.okspinoy.app.smartloadservices.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.okspinoy.app.smartloadservices.R;
import com.okspinoy.app.smartloadservices.adapters.TransactionAdapter;
import com.okspinoy.app.smartloadservices.objects.entities.Transaction;
import com.okspinoy.app.smartloadservices.helpers.TransactionConstants;
import com.okspinoy.app.smartloadservices.swipelistview.SwipeMenu;
import com.okspinoy.app.smartloadservices.swipelistview.SwipeMenuCreator;
import com.okspinoy.app.smartloadservices.swipelistview.SwipeMenuItem;
import com.okspinoy.app.smartloadservices.swipelistview.SwipeMenuListView;

import io.realm.Realm;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A placeholder fragment containing a simple view.
 */
public class TransactionActivityFragment extends Fragment {

    @InjectView(R.id.history_list)
    SwipeMenuListView mLVHistory;

    @InjectView(R.id.empty_view)
    LinearLayout mLLEmpty;

    private TransactionAdapter mAdtTransactionAdapter;
    private List<Transaction> mTransactions;

    private Activity mContext;

    public TransactionActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        ButterKnife.inject(this, view);

        mContext = getActivity();
        mTransactions = Transaction.getTransactions();
        mAdtTransactionAdapter = new TransactionAdapter(mContext, mTransactions);

        mLVHistory.setAdapter(mAdtTransactionAdapter);
        mLVHistory.setMenuCreator(new CustomSwipeMenuCreator());
        mLVHistory.setOnMenuItemClickListener(new OnSwipeMenuListViewClicked());
        mLVHistory.setOnItemClickListener(new OnTransactionItemClickedListener());

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View emptyView = layoutInflater.inflate(R.layout.container_empty_transactions, mLLEmpty);
        mLVHistory.setEmptyView(emptyView);

        return view;
    }

    private class OnTransactionItemClickedListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            displayTransactionDetails(mTransactions.get(position));
        }
    }

    private void displayTransactionDetails(Transaction bigBroTransaction) {
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle(getString(R.string.app_name));
        if (!bigBroTransaction.transactionType.equals(TransactionConstants.REGISTRATION.toString())) {
            alertDialog.setMessage(bigBroTransaction.message);
        } else {
            alertDialog.setMessage(bigBroTransaction.name);
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.button_close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private class OnSwipeMenuListViewClicked implements SwipeMenuListView.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
            switch (index) {
                case 0:
                    deleteTransaction(mTransactions.get(position), position);
                    break;
            }
            return false;
        }
    }

    private class CustomSwipeMenuCreator implements SwipeMenuCreator {
        @Override
        public void create(SwipeMenu menu) {

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(dp2px(90));
            // set a icon
            deleteItem.setIcon(R.drawable.ic_action_delete);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    }

    private void deleteTransaction(final Transaction bigBroTransaction, final int position) {
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(getString(R.string.delete_transaction_message));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes_big), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bigBroTransaction.deleteFromRealm();
                mTransactions.remove(position);
                mAdtTransactionAdapter.notifyDataSetChanged();

                alertDialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel_big), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


}
