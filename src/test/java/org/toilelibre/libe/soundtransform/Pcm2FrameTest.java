package org.toilelibre.libe.soundtransform;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.toilelibre.libe.soundtransform.infrastructure.service.frames.ByteArrayFrameProcessor;
import org.toilelibre.libe.soundtransform.infrastructure.service.observer.Slf4jObserver;
import org.toilelibre.libe.soundtransform.ioc.ApplicationInjector.$;
import org.toilelibre.libe.soundtransform.ioc.SoundTransformTest;
import org.toilelibre.libe.soundtransform.model.converted.sound.Sound;
import org.toilelibre.libe.soundtransform.model.exception.SoundTransformException;
import org.toilelibre.libe.soundtransform.model.inputstream.InputStreamInfo;
import org.toilelibre.libe.soundtransform.model.inputstream.TransformInputStreamService;
import org.toilelibre.libe.soundtransform.model.observer.LogEvent.LogLevel;

public class Pcm2FrameTest extends SoundTransformTest {

    @Test
    public void testReversibleData () throws SoundTransformException {
        final RandomDataGenerator rdg = new RandomDataGenerator ();
        final byte [] data = new byte [256];
        for (int i = 0 ; i < data.length ; i++) {
            data [i] = (byte) rdg.nextInt (Byte.MIN_VALUE, Byte.MAX_VALUE);
        }
        new Slf4jObserver ().notify (Arrays.toString (data));
        final TransformInputStreamService ts = $.create (TransformInputStreamService.class, new Slf4jObserver (LogLevel.PARANOIAC));
        final InputStream bais = new ByteArrayInputStream (data);
        final Sound [] channels = ts.fromInputStream (bais, new InputStreamInfo (2, data.length / 4, 2, 44100.0, false, true));

        final byte [] out = new ByteArrayFrameProcessor ().framesToByteArray (channels, 2, false, true);
        new Slf4jObserver ().notify (Arrays.toString (out));
        Assert.assertArrayEquals (data, out);
    }
}
