package org.toilelibre.libe.soundtransform.model.inputstream;

import java.util.Locale;

public class InputStreamInfo {
    private final int     channels;
    private final long    frameLength;
    private final int     sampleSize;
    private final double  sampleRate;
    private final boolean bigEndian;
    private final boolean pcmSigned;
    private final String  soundInfo;

    public InputStreamInfo (final int channels, final long frameLength, final int sampleSize, final double sampleRate, final boolean bigEndian, final boolean pcmSigned) {
        super ();
        this.channels = channels;
        this.frameLength = frameLength;
        this.sampleSize = sampleSize;
        this.sampleRate = sampleRate;
        this.bigEndian = bigEndian;
        this.pcmSigned = pcmSigned;
        this.soundInfo = null;
    }

    public InputStreamInfo (final int channels, final long frameLength, final int sampleSize, final double sampleRate, final boolean bigEndian, final boolean pcmSigned, String soundInfo) {
        super ();
        this.channels = channels;
        this.frameLength = frameLength;
        this.sampleSize = sampleSize;
        this.sampleRate = sampleRate;
        this.bigEndian = bigEndian;
        this.pcmSigned = pcmSigned;
        this.soundInfo = soundInfo;
    }

    public int getChannels () {
        return this.channels;
    }

    public long getFrameLength () {
        return this.frameLength;
    }

    public double getSampleRate () {
        return this.sampleRate;
    }

    public int getSampleSize () {
        return this.sampleSize;
    }

    public boolean isBigEndian () {
        return this.bigEndian;
    }

    public boolean isPcmSigned () {
        return this.pcmSigned;
    }

    public String getSoundInfo () {
        return soundInfo;
    }

    @Override
    public String toString () {
        return String.format (Locale.ENGLISH, "%1s %6.1f Hz, %1d bit, %1s, %1d bytes/frame, %6s", this.isPcmSigned () ? "PCM_SIGNED" : "PCM_UNSIGNED", this.getSampleRate (), this.getSampleSize () * 8, this.getChannels () == 1 ? "mono" : "stereo", this.getSampleSize () * this.getChannels (),
                this.isBigEndian () ? "big-endian" : "little-endian");
    }
}
