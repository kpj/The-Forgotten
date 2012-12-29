package user_interface.sound;
import java.applet.*;
import sun.audio.*;
import javax.sound.sampled.*;

public class Sound
{
    Clip song;
    String path;
    
    public Sound(String path2sound) {
        try {
            AudioInputStream sound = AudioSystem.getAudioInputStream(this.getClass().getResource("/"+path2sound));
        
            DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
            
            song = (Clip) AudioSystem.getLine(info);
            
            song.open(sound);
        }
        catch(Exception e){}
    }
    public void loop_sound() {
        song.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop_sound() {
        song.stop();
    }
    public void play_sound() {
        song.loop(0);
    }
    
    public boolean is_playing() {
        return song.isRunning();
    }
}
