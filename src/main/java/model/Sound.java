package model;

import static constants.Constants.*;

import java.io.File;
import java.net.URL;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;

public class Sound{

	private String name; //nom du fichier
	private Clip clip; //clip qui va être joué

    //constructeurs
	public Sound(String name){
		this.name = name;
	}

    //créer un son à joué
	private void create () {
		try{
            URL url = new File( DIR_AUDIO + name ).toURI().toURL();
			AudioInputStream audio = AudioSystem.getAudioInputStream(url);
			this.clip= AudioSystem.getClip();
			this.clip.open(audio);
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	//Joue le son
	public void play(){
		this.create();
		this.clip.start();
	}

    //Stop le son
    public void stop(){
		this.clip.stop();
	}

    //Joue un son en boucle
	public void loop(){
		this.create();
		this.clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
}