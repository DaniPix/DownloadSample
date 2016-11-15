package com.dani2pix.downloadsample;


import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;


/**
 * Created by Domnica on 11/15/2016.
 */

public class Download {


    public static Observable<Double> doDownload(final String url) {


        return Observable.create(new Observable.OnSubscribe<Double>() {
            @Override
            public void call(Subscriber<? super Double> subscriber) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                try {
                    Call runningRequest = client.newCall(request);
                    Response response = runningRequest.execute();
                    Buffer buffer = new Buffer();
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        long contentLength = responseBody.contentLength();
                        BufferedSource bufferedSource = responseBody.source();
                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/myPicture.mkv");
                        BufferedSink sink = Okio.buffer(Okio.sink(file));
                        subscriber.onNext(0.0);
                        double total = 0;
                        double read;
                        while ((read = bufferedSource.read(sink.buffer(), 2048)) != -1) {
                            total += read;
                            subscriber.onNext((total / contentLength) * 100);
                        }
                        sink.writeAll(bufferedSource);
                        sink.flush();
                        sink.close();
                        responseBody.close();
                        subscriber.onNext(100.00);
                        subscriber.onCompleted();
                    }
                } catch (IOException e) {
                    subscriber.onError(e);
                    Log.e(getClass().getName(), e.getMessage(), e);
                }
            }
        }).sample(1000, TimeUnit.MILLISECONDS);
    }
}
