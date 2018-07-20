package com.flipshope.app1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlTextView;

public class AboutFragment extends Fragment {
    View myView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_about, container, false);
        setHasOptionsMenu(true);
        HtmlTextView foo = myView.findViewById(R.id.aboutUs);
        foo.setHtml(getString(R.string.about_us));
        return myView;
    }
}
