package org.toilelibre.libe.soundtransform.infrastructure.service.transforms;

import org.apache.commons.math3.complex.Complex;
import org.toilelibre.libe.soundtransform.model.converted.spectrum.SimpleFrequencySoundTransformation;
import org.toilelibre.libe.soundtransform.model.converted.spectrum.Spectrum;

public class GaussianEqualizerSoundTransformation extends SimpleFrequencySoundTransformation {

    public GaussianEqualizerSoundTransformation () {
    }

    private Complex function (final double x) {
        return new Complex (1 - Math.exp (-Math.pow (x - 3500, 2) / 1000) / 2);
    }

    @Override
    public Spectrum transformFrequencies (final Spectrum fs, final int offset, final int powOf2NearestLength, final int length) {
        final Complex [] newAmpl = new Complex [powOf2NearestLength];
        for (double j = 0; j < length; j++) {
            final double freq = j * fs.getSampleRate () / fs.getState ().length;
            newAmpl [(int) j] = fs.getState () [(int) j].multiply (this.function (freq));
        }
        for (int j = length; j < powOf2NearestLength; j++) {
            newAmpl [j] = new Complex (0, 0);
        }
        return new Spectrum (newAmpl, fs.getSampleRate (), fs.getNbBytes ());
    }
}
