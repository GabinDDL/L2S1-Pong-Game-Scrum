package gui;

import java.io.InputStream;
import java.io.BufferedInputStream;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;

import model.MediaHandler;

public class Sound {
	private Clip clip; // clip qui va être joué
	private boolean first; // First repond à la question : Est ce la première fois que le morceau est joué
							// ?

	// constructeurs
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

	// Joue le son
	public void play() {
		if (first) {
			this.clip.loop(0);
			first = false;
		} else {
			this.clip.loop(1);
		}
	}

	// Stop le son
	public void stop() {
		this.clip.stop();
	}

	// Joue un son en boucle
	public void loop() {
		this.clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
}