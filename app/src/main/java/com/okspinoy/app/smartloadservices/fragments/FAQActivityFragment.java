package com.okspinoy.app.smartloadservices.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.okspinoy.app.smartloadservices.R;
import com.okspinoy.app.smartloadservices.adapters.FAQAdapter;
import com.okspinoy.app.smartloadservices.objects.entities.FAQ;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A placeholder fragment containing a simple view.
 */
public class FAQActivityFragment extends Fragment {

    @InjectView(R.id.lv_faq)
    ListView mLVFaq;

    private FAQAdapter mFAQAdapter;
    private List<FAQ> mFaqs;

    public FAQActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faq, container, false);
        ButterKnife.inject(this, view);

        mFaqs = FAQ.getAllEnglish();
        mFAQAdapter = new FAQAdapter(getActivity(), mFaqs);
        mLVFaq.setAdapter(mFAQAdapter);

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_faq, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_translate) {
            mFAQAdapter.clear();
            mFaqs.clear();
            if (item.getTitle().equals("TAGALOG")) {
                item.setTitle("ENGLISH");
                mFaqs = FAQ.getAllTagalog();
            } else {
                item.setTitle("TAGALOG");
                mFaqs = FAQ.getAllEnglish();
            }
            mFAQAdapter = new FAQAdapter(getActivity(), mFaqs);
            mLVFaq.setAdapter(mFAQAdapter);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
