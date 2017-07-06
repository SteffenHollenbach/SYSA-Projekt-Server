package multiagent;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundClip  {

	public SoundClip(String clipTitle) { 
		this(clipTitle, 0);
	}
	
	
   // Constructor
   public SoundClip(String clipTitle, int milliseconds) {      
      try {	          
          URL url = getClass().getResource("./../resources/" + clipTitle + ".wav");
          File soundFile = new File(url.getPath());
          	          	          
          AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);     
          
          //Sleep until file is over
          if (milliseconds == -1){
        	  AudioFormat format = audioIn.getFormat();
        	  long frames = audioIn.getFrameLength();
        	  milliseconds = (int) ((frames+0.0) / format.getFrameRate() * 1000 - 100);   
          }
          
          
         // Get a sound clip resource.
         Clip clip = AudioSystem.getClip();
         // Open audio clip and load samples from the audio input stream.
         clip.open(audioIn);
         clip.start();
         
         Thread.sleep(milliseconds);
         
         
         
      } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      } catch (InterruptedException e) {
             e.printStackTrace();
      }
      
   }
}