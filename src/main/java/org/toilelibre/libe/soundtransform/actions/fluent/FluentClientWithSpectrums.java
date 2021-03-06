package org.toilelibre.libe.soundtransform.actions.fluent;

import java.io.Serializable;
import java.util.List;

import org.toilelibre.libe.soundtransform.model.converted.spectrum.Spectrum;
import org.toilelibre.libe.soundtransform.model.exception.SoundTransformException;

public interface FluentClientWithSpectrums extends FluentClientCommon {

    /**
     * Uses the current available spectrums objects to convert them into a sound
     *
     * @return the client, with a sound imported
     * @throws SoundTransformException
     *             if the spectrums are in an invalid format, or if the
     *             transform to sound does not work
     */
    FluentClientSoundImported extractSound () throws SoundTransformException;

    /**
     * Plays the current audio data and (if needed) converts it temporarily to a
     * sound
     *
     * @return the client, with the spectrums list
     * @throws SoundTransformException
     *             could not play the current audio data
     */
    FluentClientWithSpectrums playIt () throws SoundTransformException;
    
    /**
     * Plays the current audio data and convert it temporarily into a sound
     *
     * @param stopMonitor calling notifyAll stops the player
     * @return the client, with a sound
     * @throws SoundTransformException
     *             could not play the current audio data
     */
    FluentClientWithSpectrums playIt (Object stopMonitor) throws SoundTransformException;
    
    /**
     * Plays the current audio data and convert it temporarily into a sound
     *
     * @param stopMonitor calling notifyAll stops the player
     * @param skipMilliSeconds starts playing at 'skipMilliSeconds' ms from the begining of the sound
     * @return the client, with a sound
     * @throws SoundTransformException
     *             could not play the current audio data
     */
    FluentClientWithSpectrums playIt (Object stopMonitor, int skipMilliSeconds) throws SoundTransformException;

    /**
     * Stops the client pipeline and returns the obtained spectrums
     *
     * @return a list of spectrums for each channel
     */
    List<Spectrum<Serializable> []> stopWithSpectrums ();

}
