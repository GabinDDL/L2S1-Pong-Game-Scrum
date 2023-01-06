package gui;

import java.io.InputStream;
import java.io.BufferedInputStream;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;

import model.MediaHandler;

public class Sound {
	private Clip clip; // clip to be played
	private boolean first; // First answers the question : Is it the first time the Clip is played ?

	// Constructors
	public Sound(String name) {
		try {
			first = true;
			InputStream audioSrc = MediaHandler.getAudioInputStream(name);
			InputStream bufferedIn = new BufferedInputStream(audioSrc);
			AudioInputStream audio = AudioSystem.getAudioInputStream(bufferedIn);
			this.clip = AudioSystem.getClip();
			this.clip.open(audio);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Plays the sound
	 */
	public void play() {
		if (first) {
			this.clip.loop(0);
			first = false;
		} else {
			this.clip.loop(1);
		}
	}

	/**
	 * Stops the sound
	 */
	public void stop() {
		this.clip.stop();
	}

	/**
	 * Play the sound in loop
	 */
	public void loop() {
		this.clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
}