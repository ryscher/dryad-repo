/*
 */
package org.datadryad.rest.storage;

import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;
import java.io.File;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Dan Leehr <dan.leehr@nescent.org>
 */
@Provider
public class ManuscriptStorageResolver extends SingletonTypeInjectableProvider<Context, AbstractManuscriptStorage> {
    private static final String PATH = "/tmp/dryad_rest_manuscripts";
    public ManuscriptStorageResolver() {
        super(AbstractManuscriptStorage.class, new ManuscriptJSONStorageImpl(new File(PATH)));
        File directory = new File(PATH);
        if(!directory.exists()) {
            directory.mkdir();
        }
    }
}
