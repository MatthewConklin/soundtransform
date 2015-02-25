package org.toilelibre.libe.soundtransform.infrastructure.service.appender;

import org.toilelibre.libe.soundtransform.model.converted.sound.Sound;
import org.toilelibre.libe.soundtransform.model.converted.sound.SoundAppender;
import org.toilelibre.libe.soundtransform.model.exception.SoundTransformException;
import org.toilelibre.libe.soundtransform.model.library.note.Note;

public class ConvertedSoundAppender implements SoundAppender {

    /*
     * (non-Javadoc)
     *
     * @see org.toilelibre.libe.soundtransform.infrastructure.service.appender.
     * SoundAppenderI
     * #append(org.toilelibre.libe.soundtransform.model.converted.sound.Sound,
     * int, org.toilelibre.libe.soundtransform.model.converted.sound.Sound)
     */
    @Override
    public void append (final Sound origin, final int usedarraylength, final Sound... otherSounds) {
        int offset = usedarraylength;
        for (final Sound otherSound : otherSounds) {
            offset = this.append (origin, offset, otherSound);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.toilelibre.libe.soundtransform.infrastructure.service.appender.
     * SoundAppenderI
     * #append(org.toilelibre.libe.soundtransform.model.converted.sound.Sound,
     * int, org.toilelibre.libe.soundtransform.model.converted.sound.Sound)
     */
    @Override
    public int append (final Sound origin, final int usedarraylength, final Sound otherSound) {
        final Sound resultBeforeResize = this.changeNbBytesPerSample (otherSound, origin.getNbBytesPerSample ());
        final Sound resultBeforeCopy = this.resizeToSampleRate (resultBeforeResize, origin.getSampleRate ());
        final int lastIndex = Math.min (origin.getSamplesLength (), usedarraylength + resultBeforeCopy.getSamplesLength ());
        System.arraycopy (resultBeforeCopy.getSamples (), 0, origin.getSamples (), usedarraylength, lastIndex - usedarraylength);
        return lastIndex;
    }

    @Override
    public Sound append (final Sound sound1, final Sound sound2) {
        final Sound sound2Ajusted = this.resizeToSampleRate (this.changeNbBytesPerSample (sound2, sound1.getNbBytesPerSample ()), sound1.getSampleRate ());
        final Sound result = new Sound (new long [sound1.getSamplesLength () + sound2.getSamplesLength ()], sound1.getNbBytesPerSample (), sound1.getSampleRate (), sound1.getChannelNum ());

        System.arraycopy (sound1.getSamples (), 0, result.getSamples (), 0, sound1.getSamplesLength ());
        System.arraycopy (sound2Ajusted.getSamples (), 0, result.getSamples (), sound1.getSamplesLength (), sound2Ajusted.getSamplesLength ());

        return result;
    }

    @Override
    public void appendNote (final Sound sound, final Note note, final double lastFreq, final int indexInSound, final int channelNum, final float lengthInSeconds) throws SoundTransformException {

        final Sound attack = note.getAttack ((int) lastFreq, channelNum, lengthInSeconds);
        final Sound decay = note.getDecay ((int) lastFreq, channelNum, lengthInSeconds);
        final Sound sustain = note.getSustain ((int) lastFreq, channelNum, lengthInSeconds);
        final Sound release = note.getRelease ((int) lastFreq, channelNum, lengthInSeconds);
        this.append (sound, indexInSound, attack, decay, sustain, release);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.toilelibre.libe.soundtransform.infrastructure.service.appender.
     * SoundAppender
     * #changeNbBytesPerSample(org.toilelibre.libe.soundtransform.model
     * .converted.sound.Sound, int)
     */
    @Override
    public Sound changeNbBytesPerSample (final Sound sound, final int newNbBytesPerSample) {
        final long [] newsamples = new long [sound.getSamplesLength ()];
        final int byteMaxValue = 1 >> Byte.SIZE;
        final long oldMax = (long) (Math.pow (byteMaxValue, sound.getNbBytesPerSample ()) / 2);
        final long newMax = (long) (Math.pow (byteMaxValue, newNbBytesPerSample) / 2);
        for (int j = 0 ; j < sound.getSamplesLength () ; j++) {
            newsamples [j] = (long) (sound.getSampleAt (j) * 1.0 * newMax / oldMax);
        }
        return new Sound (newsamples, newNbBytesPerSample, sound.getSampleRate (), sound.getChannelNum ());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.toilelibre.libe.soundtransform.infrastructure.service.appender.
     * SoundAppender
     * #downsampleWithRatio(org.toilelibre.libe.soundtransform.model
     * .converted.sound.Sound, float)
     */
    @Override
    public Sound downsampleWithRatio (final Sound sound, final float ratio) {
        float appendIfGreaterThanOrEqualsRatio = 0;
        int indexResult = 0;
        final long [] result = new long [(int) Math.ceil (sound.getSamplesLength () / ratio)];
        for (int i = 0 ; i < sound.getSamplesLength () ; i++) {
            if (appendIfGreaterThanOrEqualsRatio >= ratio) {
                appendIfGreaterThanOrEqualsRatio -= ratio;
                result [indexResult++] = sound.getSampleAt (i);
            }
            appendIfGreaterThanOrEqualsRatio += 1.0;
        }
        return new Sound (result, sound.getNbBytesPerSample (), (int) (sound.getSampleRate () / ratio), sound.getChannelNum ());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.toilelibre.libe.soundtransform.infrastructure.service.appender.
     * SoundAppender
     * #resizeToSampleRate(org.toilelibre.libe.soundtransform.model
     * .converted.sound.Sound, int)
     */
    @Override
    public Sound resizeToSampleRate (final Sound sound, final int newfreq) {
        final float ratio = (float) (newfreq * 1.0 / sound.getSampleRate ());
        if (ratio > 1) {
            return this.upsampleWithRatio (sound, ratio);
        }
        if (ratio < 1) {
            return this.downsampleWithRatio (sound, (float) (1.0 / ratio));
        }
        return sound;
    }

    private Sound upsampleWithRatio (final Sound sound, final float ratio) {
        float appendWhileLessThanOrEqualsRatio = 0;
        int indexResult = 0;
        final long [] result = new long [(int) Math.ceil (sound.getSamplesLength () * (ratio + 1))];
        for (int i = 0 ; i < sound.getSamplesLength () ; i++) {
            while (appendWhileLessThanOrEqualsRatio <= ratio) {
                result [indexResult++] = sound.getSampleAt (i);
                appendWhileLessThanOrEqualsRatio++;
            }
            appendWhileLessThanOrEqualsRatio -= ratio;
        }
        long [] outputLongArray = new long [0];
        if (indexResult > 0) {
            outputLongArray = new long [indexResult];
            System.arraycopy (result, 0, outputLongArray, 0, indexResult);
        }
        return new Sound (outputLongArray, sound.getNbBytesPerSample (), (int) (sound.getSampleRate () * ratio), sound.getChannelNum ());
    }
}
