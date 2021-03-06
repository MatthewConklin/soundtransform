package org.toilelibre.libe.soundtransform.infrastructure.service.audioformat.javax;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.toilelibre.libe.soundtransform.infrastructure.service.Processor;
import org.toilelibre.libe.soundtransform.model.exception.SoundTransformException;
import org.toilelibre.libe.soundtransform.model.inputstream.AudioFileHelper;

@Processor
final class JavazoomAudioFileHelper implements AudioFileHelper {

    private InputStream getFileInputStreamFromFile (final File readFile) throws SoundTransformException {
        try {
            return new BufferedInputStream (new FileInputStream (readFile));
        } catch (final FileNotFoundException e) {
            throw new SoundTransformException (AudioFileHelperErrorCode.NO_SOURCE_INPUT_STREAM, e, readFile.getPath ());
        }
    }

    @Override
    public InputStream getUnknownInputStreamFromFile (final File inputFile) throws SoundTransformException {
        return this.getFileInputStreamFromFile (inputFile);
    }

    @Override
    public InputStream getAudioInputStream (final InputStream rawInputStream) throws SoundTransformException {
        try {
            return AudioSystem.getAudioInputStream (rawInputStream);
        } catch (final UnsupportedAudioFileException e) {
            throw new SoundTransformException (AudioFileHelperErrorCode.WRONG_TYPE, e, rawInputStream.toString ());
        } catch (final IOException e) {
            throw new SoundTransformException (AudioFileHelperErrorCode.COULD_NOT_CONVERT, e, rawInputStream.toString ());
        }
    }

    @Override
    public InputStream toStream (final byte [] byteArray, final Object audioFormat1) throws SoundTransformException {
        if (! (audioFormat1 instanceof AudioFormat)) {
            throw new SoundTransformException (AudioFileHelperErrorCode.AUDIO_FORMAT_COULD_NOT_BE_READ, new IllegalArgumentException (audioFormat1 == null ? "null" : audioFormat1.toString ()));
        }
        final AudioFormat audioFormat = (AudioFormat) audioFormat1;
        final ByteArrayInputStream bais = new ByteArrayInputStream (byteArray);
        return new AudioInputStream (bais, audioFormat, byteArray.length / audioFormat.getFrameSize ());
    }

    @Override
    public void writeInputStream (final InputStream ais, final File fDest) throws SoundTransformException {
        if (! (ais instanceof AudioInputStream)) {
            throw new SoundTransformException (AudioFileHelperErrorCode.COULD_NOT_CONVERT, new IllegalArgumentException (ais == null ? "null" : ais.toString ()), ais);
        }
        try {
            AudioSystem.write ((AudioInputStream) ais, AudioFileFormat.Type.WAVE, fDest);
        } catch (final IOException e) {
            throw new SoundTransformException (AudioFileHelperErrorCode.COULD_NOT_CONVERT, e, fDest.getName ());
        }
    }
}
