package com.shaikhhamadali.blogspot.texttospeech;

import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;

public class TextToSpeak extends Activity implements TextToSpeech.OnInitListener{
	//Create variables
	double pitch=0.0f,speechRate=0.0f;
	//declare views/controls	
	private TextToSpeech tts;
	SeekBar sBSpeechRate,sBPitchRate;
	EditText eTPronounce;
	Button btnSpeak;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_to_speech);
		initializeControls();
		/*Initialize the Text to speech engine using the default TTS engine.
		 *This will also initialize the associated TextToSpeech engine if it isn't already running.
		 */
		tts = new TextToSpeech(this, this);
	}
	private void initializeControls() {
		//get reference of the UI Controls
		sBSpeechRate=(SeekBar)findViewById(R.id.sBSpeechRate);
		sBPitchRate=(SeekBar)findViewById(R.id.sBPitchRate);
		eTPronounce=(EditText)findViewById(R.id.eTPronounce);
		btnSpeak=(Button)findViewById(R.id.btnSpeak);
		/*initialize seek bar change listener to listen every change on seekbar
		 * either increment or decrement*/
		sBSpeechRate.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				//divide progress by 10 to get speech rate in float values like 0.1
				speechRate=((double)progress+1)/10;
			}
		});

		sBPitchRate.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				//divide progress by 10 to get pitch in float values like 0.1
				pitch=((double)progress+1)/10;
			}
		});
		//set default text as Welcome to shaikhhamadali.blogspot.com
		eTPronounce.setText("Welcome to shaikhhamadali.blogspot.com");
		//set on click listener to button speak call speakOut Method to speak text
		btnSpeak.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				speakOut();
			}
		});
	}
	@Override
	public void onInit(int status) {
		//check the status
		if (status == TextToSpeech.SUCCESS) {
			//set language Locale to US
			int result = tts.setLanguage(Locale.US);
			//check that is language locale available on device or supported
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
			} else {
				//then enable button to listen for listener
				btnSpeak.setEnabled(true);
				//and speak by calling speakOut
				speakOut();
			}

		} else {
			//show toast if initialization failed
			Toast.makeText(getBaseContext(), "TTS Engine Initilization Failed!",Toast.LENGTH_SHORT).show();
		}

	}

	private void speakOut() {
		//get entered text to speak
		String text = eTPronounce.getText().toString();
		//set pitch rate adjusted by user
		tts.setPitch((float)pitch);
		//set speech rate adjusted by user
		tts.setSpeechRate((float)speechRate);
		/*pass text to speak using engine and pass Queue mode as QUEUE_FLUSH where all entries in the playback queue 
		 *(media to be played and text to be synthesized) are dropped and
		 *replaced by the new entry. Queues are flushed with respect to
		 *a given calling app. Entries in the queue from other callers are not discarded*/
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);

	}
	@Override
	public void onDestroy() {
		// Don't forget to stop and shutdown text to speech engine!
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

}
