package com.flipshope.app1;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlAssetsImageGetter;
import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

public class HowToUseFragment extends Fragment {
    View myView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_how_to_use, container, false);
        setHasOptionsMenu(true);
        HtmlTextView foo = myView.findViewById(R.id.how_to_use);
        foo.setHtml(getString(R.string.how_to_use), new HtmlResImageGetter1(foo));
        return myView;
    }

    public class HtmlResImageGetter1 implements Html.ImageGetter {
        TextView container;

        public HtmlResImageGetter1(TextView textView) {
            this.container = textView;
        }

        public Drawable getDrawable(String source) {
            Context context = container.getContext();
            int id = context.getResources().getIdentifier(source, "drawable", context.getPackageName());

            if (id == 0) {
                // the drawable resource wasn't found in our package, maybe it is a stock android drawable?
                id = context.getResources().getIdentifier(source, "drawable", "android");
            }

            if (id == 0) {
                // prevent a crash if the resource still can't be found
                Log.e(HtmlTextView.TAG, "source could not be found: " + source);
                return null;
            } else {
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0, 0, 700, 1450);
                return d;
            }
        }

    }
}
