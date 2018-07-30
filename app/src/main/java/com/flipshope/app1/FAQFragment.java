package com.flipshope.app1;

import android.content.Loader;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

public class FAQFragment extends Fragment {
    View myView;
    PDFView pdfView;
    String pdfFileName;
    Integer pageNumber = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_faq, container, false);
        setHasOptionsMenu(true);
        HtmlTextView foo = myView.findViewById(R.id.faq);
        foo.setHtml(getString(R.string.faq));
        return myView;
    }


}
