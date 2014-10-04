package com.kanawish.labs.workingexamples;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kanawish.labs.workingexamples.model.Lorem;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		final EditText editText = (EditText)findViewById(R.id.editText);
		// To avoid getting red-underlined words in the text, add this input type flag.
		int inputType = editText.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
		// Re-assign the updated input type flag.
		editText.setInputType(inputType);

		Button loadButton = (Button) findViewById(R.id.loadButton);
		// On click of the button, triggers the editText loading process.
		loadButton.setOnClickListener(new View.OnClickListener() {
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
		});

		Button clearButton = (Button) findViewById(R.id.clearButton);
		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editText.setText("");
			}
		});
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
