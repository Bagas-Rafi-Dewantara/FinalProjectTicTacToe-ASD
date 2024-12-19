package Connect4;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
public enum SoundEffect {
    BACKGROUND("TicTacToe/audio/game_music.wav"),
    CROSS_SOUND("TicTacToe/audio/cross_placed.wav"),
    NOUGH_SOUND("TicTacToe/audio/nough_placed.wav"),
    CROSSWIN_SOUND ("TicTacToe/audio/cross_win.wav"),
    NOUGHWIN_SOUND("TicTacToe/audio/nough_win.wav"),
    DRAW_SOUND("TicTacToe/audio/draw.wav");

    /** Nested enumeration for specifying volume */
    public static enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public static Volume volume = Volume.LOW;

    /** Each sound effect has its own clip, loaded with its own sound file. */
    private Clip clip;

    /** Private Constructor to construct each element of the enum with its own sound file. */
    private SoundEffect(String soundFileName) {
        try {
            // Use URL (instead of File) to read from disk and JAR.
            URL url = this.getClass().getClassLoader().getResource(soundFileName);
            if (url == null) {
                throw new IllegalArgumentException("Couldn't find file: " + soundFileName);
            }
            // Set up an audio input stream piped from the sound file.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            // Get a clip resource.
            clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /** Play or Re-play the sound effect from the beginning, by rewinding. */
    public void play() {
        if (volume != Volume.MUTE) {
            if (clip.isRunning())
                clip.stop();   // Stop the player if it is still running
            clip.setFramePosition(0); // rewind to the beginning
            clip.start();     // Start playing
        }
    }

    // Metode untuk memutar suara dalam loop
    public void loop() {
        if (volume != Volume.MUTE) {
            if (clip.isRunning())
                clip.stop();
            // Hentikan klip jika masih berjalan
            clip.setFramePosition(0);
            // Kembali ke awal
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            // Memutar dalam loop
        }
    }

    // Metode untuk menghentikan suara
    public void stop() {
        if (clip.isRunning()) {
            clip.stop();
            // Hentikan klip jika masih berjalan
        }
    }

    /** Optional static method to pre-load all the sound files. */
    static void initGame() {
        values(); // calls the constructor for all the elements
    }
}
