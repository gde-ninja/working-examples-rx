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

```java
```