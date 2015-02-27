package org.toilelibre.libe.soundtransform.actions.play;

import java.io.InputStream;
import java.io.Serializable;

import org.toilelibre.libe.soundtransform.actions.Action;
import org.toilelibre.libe.soundtransform.model.converted.sound.PlaySoundService;
import org.toilelibre.libe.soundtransform.model.converted.sound.Sound;
import org.toilelibre.libe.soundtransform.model.converted.spectrum.Spectrum;
import org.toilelibre.libe.soundtransform.model.exception.SoundTransformException;

public class PlaySound extends Action {

    public void play (final InputStream is) throws SoundTransformException {
        this.playSound.play (is);
    }

    public void play (final Sound [] channels) throws SoundTransformException {
        this.playSound.play (channels);
    }

    @SuppressWarnings ("unchecked")
    public void play (final Spectrum<?> spectrum1) throws SoundTransformException {
        final Spectrum<Serializable> spectrum = (Spectrum<Serializable>) spectrum1;
        ((PlaySoundService<Serializable>) this.playSound).play (spectrum);
    }
}
