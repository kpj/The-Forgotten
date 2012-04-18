import java.applet.*;

public class Sound
{
    AudioClip song;
    
    Sound(String path2sound) {
        try {
            song = Applet.newAudioClip(this.getClass().getResource("/"+path2sound));
        }
        catch(Exception e){}
    }
    public void play_sound() {
        song.loop();
    }
    public void stop_sound() {
        song.stop();
    }
    public void play_sound_once() {
        song.play();
    }
}
