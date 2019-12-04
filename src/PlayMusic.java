import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.Optional;

public abstract class PlayMusic
{

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
//                clip.
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
}