.subscribe(
    // onNext will be called for each lorem loaded. (1 only in this example)
    new Action1<Lorem>() {
        @Override
        public void call(Lorem lorem) {
            editText.setText(lorem.getLoremText());
        }
    },
    // onError will be called if an error occurs.
    new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            String msg = getString(R.string.error_loading_lorem);
            Log.v(TAG, msg, throwable);
            editText.setText(msg);
        }
    }
);


return Observable.create(onSubscribe)
// We subscribe on io schedulers since we're I/O limited. (i.e. loading content on disk)
.subscribeOn(Schedulers.io());

// This makes sure onNext,onError and onComplete are called from the main thread.
.observeOn(AndroidSchedulers.mainThread())
// Subscribing will start the loading process.
.subscribe(

```java
/**
 * Returns an Observable that will load one file off the assets directory. (lorem.txt)
 *
 * Note below how the Observable is configured to be subscribed on the io thread.
 *
 * @param context
 * @return
 */
public static Observable<Lorem> loadLorem(final Context context) {
	return Observable.create(
		new Observable.OnSubscribe<Lorem>() {
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
		})
		// We subscribe on io schedulers since we're I/O limited. (i.e. loading content on disk)
		.subscribeOn(Schedulers.io());
}
```

```java
@Override
public void onClick(View v) {
	/*
	 * The thing of note here is how
	 * - We're assigning the desired 'observing' thread
	 * - We're handling onNext/onError
	 */
	Lorem.loadLorem(MainActivity.this)
		// This makes sure onNext,onError and onComplete are called from the main thread.
		.observeOn(AndroidSchedulers.mainThread())
		// Subscribing will start the loading process.
		.subscribe(
				// onNext will be called for each lorem loaded. (1 only in this example)
				new Action1<Lorem>() {
					@Override
					public void call(Lorem lorem) {
						editText.setText(lorem.getLoremText());
					}
				},
				// onError will be called if an error occurs.
				new Action1<Throwable>() {
					@Override
					public void call(Throwable throwable) {
						String msg = getString(R.string.error_loading_lorem);
						Log.v(TAG, msg, throwable);
						editText.setText(msg);
					}
				}
		);
}
```


```java
```