package com.kanawish.labs.workingexamples.model;

import android.content.Context;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by etiennecaron on 2014-10-02.
 */
public class Lorem {

	private String loremText ;

	/**
	 * Returns an Observable that will load one file off the assets directory. (lorem.txt)
	 *
	 * Note below how the Observable is configured to be subscribed on the io thread.
	 *
	 * @param context
	 * @return
	 */
	public static Observable<Lorem> loadLorem(final Context context) {
        Observable.OnSubscribe<Lorem> onSubscribe = new Observable.OnSubscribe<Lorem>() {
            @Override
            public void call(Subscriber<? super Lorem> subscriber) {
                // Not a fan of the double-try-catch here.
                // TODO: Change example to use apache commons-io.
                try {
                    // Get an input stream that points to the asset.
                    InputStream is = context.getAssets().open("lorem.txt");
                    try {
                        // Create lorem instance.
                        Lorem lorem = new Lorem();
                        // Load the text from the input stream.
                        lorem.loremText = slurp(is, 2048);

                        // Call the subscriber with the new lorem.
                        subscriber.onNext(lorem);

                    } catch (IOException e) {
                        // Forward exceptions to onError()
                        subscriber.onError(e);
                    } finally {
                        // Making sure to close the input stream.
                        is.close();
                    }
                } catch (IOException e) {
                    // Forward exceptions to onError()
                    subscriber.onError(e);
                }

                // Our job is done.
                subscriber.onCompleted();
            }
        };
        return Observable.create(onSubscribe)
            // We subscribe on io schedulers since we're I/O limited. (i.e. loading content on disk)
			.subscribeOn(Schedulers.io());
	}

	public String getLoremText() {
		return loremText;
	}

	/**
	 * From http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
	 *
	 * @param is
	 * @param bufferSize
	 * @return the read String
	 */
	public static String slurp(final InputStream is, final int bufferSize)
			throws UnsupportedEncodingException, IOException
	{
		final char[] buffer = new char[bufferSize];
		final StringBuilder out = new StringBuilder();
		final Reader in = new InputStreamReader(is, "UTF-8");
		try {
			for (;;) {
				int rsz = in.read(buffer, 0, buffer.length);
				if (rsz < 0)
					break;
				out.append(buffer, 0, rsz);
			}
		}
		finally {
			in.close();
		}
		return out.toString();
	}
}
