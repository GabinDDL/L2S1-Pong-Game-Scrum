package gui;

import static constants.Constants.DIR_AUDIO;

import java.io.File;

import java.net.URL;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;

public class Sound {
	private Clip clip; // clip qui va être joué
	private boolean first; // First repond à la question : Est ce la première fois que le morceau est joué
							// ?

	// constructeurs
	public Sound(String name) {
		try {
			first = true;
			URL url = new File(DIR_AUDIO + name).toURI().toURL();
			AudioInputStream audio = AudioSystem.getAudioInputStream(url);
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