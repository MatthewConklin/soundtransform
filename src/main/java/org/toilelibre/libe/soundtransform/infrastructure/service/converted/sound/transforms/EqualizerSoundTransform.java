package org.toilelibre.libe.soundtransform.infrastructure.service.converted.sound.transforms;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.complex.Complex;
import org.toilelibre.libe.soundtransform.model.converted.FormatInfo;
import org.toilelibre.libe.soundtransform.model.converted.sound.transform.SimpleFrequencySoundTransform;
import org.toilelibre.libe.soundtransform.model.converted.spectrum.Spectrum;

/**
 *
 * Change the volume of each frequencies range at each step of the sound
 *
 */
public class EqualizerSoundTransform extends SimpleFrequencySoundTransform<Complex []> {

    private final double [] ranges;
    private final double [] amplification;

    /**
     * Default constructor. A mathematical representation of a curve
     * amplification/freqs is asked in the parameters
     *
     * @param ranges1
     *            the frequencies, in abscissa [0..20000]
     * @param amplification1
     *            the amplification, in ordinate [0..1]
     */
    public EqualizerSoundTransform (final double [] ranges1, final double [] amplification1) {
        super ();
        this.ranges = ranges1.clone ();
        this.amplification = amplification1.clone ();
    }

    @Override
    public Spectrum<Complex []> transformFrequencies (final Spectrum<Complex []> fs, final int offset, final int powOf2NearestLength, final int length) {
        final SplineInterpolator reg = new SplineInterpolator ();

        final PolynomialSplineFunction psf = reg.interpolate (this.ranges, this.amplification);
        final Complex [] newAmpl = new Complex [powOf2NearestLength];
        for (double j = 0 ; j < length ; j++) {
            final double freq = j * fs.getSampleRate () / fs.getState ().length;
            newAmpl [(int) j] = fs.getState () [(int) j].multiply (this.valueOrZero (psf, freq));
        }
        for (int j = length ; j < powOf2NearestLength ; j++) {
            newAmpl [j] = new Complex (0, 0);
        }
        return new Spectrum<Complex []> (newAmpl, new FormatInfo (fs.getSampleSize (), fs.getSampleRate ()));
    }

    private double valueOrZero (final PolynomialSplineFunction psf, final double freq) {
        return psf.isValidPoint (freq) ? psf.value (freq) : 0;
    }
}
