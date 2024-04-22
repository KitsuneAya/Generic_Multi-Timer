package app;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author <a href="https://steamcommunity.com/id/KitsuneAya/">Ayaya</a>
 * @version 1.2
 * @apiNote This class is for the instantiation of objects that can play a sound
 */
public class Audio {

    private Player audio;
    private Thread audioThread;
    private String filePath;

    /**
     * Instantiates a blank Audio object for use as a placeholder
     */
    public Audio() {
        this.audio = null;
    }

    /**
     * Instantiates a new Audio object for playing sounds
     *
     * @param filePath the relative file path + file name of the sound file
     */
    public Audio(String filePath) {

        this.filePath = filePath;
        setAudioPlayer(filePath);

    }

    public void setAudio(String filePath) {

        setAudioPlayer(filePath);

    }

    private void setAudioPlayer(String filePath) {

        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            this.audio = new Player(fileInputStream);
        } catch (JavaLayerException | FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void play() {

        if (this.audio != null) {

                // This beautiful piece of code will attempt to play the audio
                // and if not successful, will re-set the Player and try again
                this.audioThread = new Thread(() -> {
                    for (int i = 1; i <= 2; i++) {
                        try {
                            this.audio.play();
                            break;
                        } catch (JavaLayerException e) {
                            this.audio.close();
                            this.setAudio(this.filePath);
                            if (i < 2) {
                                continue;
                            }
                            e.printStackTrace(); // If not successful in the 2nd attempt, print the stack trace
                        }
                    }
                });

            this.audioThread.setDaemon(true);
            this.audioThread.start();


        }
    }

    public void stop() {

        if (this.audioThread != null) {
            this.audio.close();
            this.audioThread.interrupt();
        }

    }

    public boolean isPlaying() {
        return this.audio != null && !this.audio.isComplete();
    }

}