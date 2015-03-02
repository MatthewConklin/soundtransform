package org.toilelibre.libe.soundtransform;

import java.io.File;
import java.io.InputStream;

import org.junit.Test;
import org.toilelibre.libe.soundtransform.ioc.ApplicationInjector.$;
import org.toilelibre.libe.soundtransform.ioc.SoundTransformTest;
import org.toilelibre.libe.soundtransform.model.converted.sound.PlaySoundException;
import org.toilelibre.libe.soundtransform.model.exception.SoundTransformException;
import org.toilelibre.libe.soundtransform.model.inputstream.ConvertAudioFileService;
import org.toilelibre.libe.soundtransform.model.play.PlaySoundProcessor;

public class TestPlaySound extends SoundTransformTest {
    private final ClassLoader classLoader = Thread.currentThread ().getContextClassLoader ();
    private final File        input       = new File (this.classLoader.getResource ("before.wav").getFile ());

    @Test
    public void playBeforeWav () throws SoundTransformException {
        final PlaySoundProcessor ps = $.select (PlaySoundProcessor.class);
        final ConvertAudioFileService convertAudioFileService = $.create (ConvertAudioFileService.class);
        final InputStream ais = convertAudioFileService.callConverter (this.input);
        try {
            ps.play (ais);
        } catch (final java.lang.IllegalArgumentException iae) {
            if (!"No line matching interface Clip is supported.".equals (iae.getMessage ())) {
                throw iae;
            }
        } catch (final PlaySoundException e) {
            // javax.sound.sampled.LineUnavailableException for some JDK
            // versions
        } catch (final RuntimeException e) {
            if (!"Stub!".equals (e.getMessage ())) {
                throw e;
            }
        }
    }
}
