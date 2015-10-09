package org.toilelibre.libe.soundtransform.actions.notes;

import java.io.InputStream;

import org.toilelibre.libe.soundtransform.actions.Action;
import org.toilelibre.libe.soundtransform.model.exception.SoundTransformException;
import org.toilelibre.libe.soundtransform.model.library.pack.Pack;
import org.toilelibre.libe.soundtransform.model.logging.Observer;

public class ImportAPackIntoTheLibrary extends Action {

    public ImportAPackIntoTheLibrary (final Observer... observers) {
        super (observers);
    }

    public Pack getPack (final String title) {
        return this.importPack.getAPack (this.library, title);
    }

    public void importAPack (final String title, final InputStream jsonStream) throws SoundTransformException {
        this.importPack.importPack (this.library, title, jsonStream);
    }

    public void importAPack (final String title, final Object context, final Class<?> rClass, final int packJsonId) throws SoundTransformException {
        this.importPack.importPack (this.library, title, context, rClass, packJsonId);

    }

    public void importAPack (final String title, final String jsonContent) throws SoundTransformException {
        this.importPack.importPack (this.library, title, jsonContent);
    }
}
