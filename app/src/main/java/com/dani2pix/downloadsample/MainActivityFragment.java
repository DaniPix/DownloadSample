package com.dani2pix.downloadsample;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
         final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);

        Download.doDownload("http://downloads.4ksamples.com/downloads/sample-Elysium.2013.2160p.mkv")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Double>() {
                    @Override
                    public void onCompleted() {
                        Log.d("onCompleted", "DONE");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError", e.getMessage());
                    }

                    @Override
                    public void onNext(Double o) {
                        progressBar.setProgress(o.intValue());
                        Log.d("onNext", o.toString());
                    }
                });


        return view;
    }
}
