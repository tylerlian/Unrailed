import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.Optional;

public abstract class PlayMusic
{

    private Optional<Clip> clipSong;

    public PlayMusic(Optional<Clip> clipSong){
        this.clipSong = Optional.empty();
    }
    public static Optional<Clip> playMusic(String musicLocation, Optional<Clip> song) {

        try
        {
            File musicPath = new File(musicLocation);


            if(musicPath.exists() && !song.isPresent()){
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(10000000);
                return Optional.of(clip);
            }
            else if(song.isPresent()) {
                song.get().stop();
                return Optional.empty();
            } else {
                System.out.println("Can't find file");
                return Optional.empty();
            }

        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Clip> getClipSong() {
        return clipSong;
    }
}