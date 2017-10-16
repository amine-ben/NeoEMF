/*
 * Copyright (c) 2013-2017 Atlanmod, Inria, LS2N, and IMT Nantes.
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v2.0 which accompanies
 * this distribution, and is available at https://www.eclipse.org/legal/epl-2.0/
 */

package fr.inria.atlanmod.neoemf.context;

import fr.inria.atlanmod.neoemf.config.Config;
import fr.inria.atlanmod.neoemf.data.BackendFactory;
import fr.inria.atlanmod.neoemf.data.im.InMemoryBackendFactory;
import fr.inria.atlanmod.neoemf.data.im.config.InMemoryConfig;
import fr.inria.atlanmod.neoemf.resource.PersistentResource;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A specific {@link Context} for the core.
 */
@ParametersAreNonnullByDefault
public abstract class CoreContext extends AbstractContext {

    /**
     * Returns the instance of this class.
     *
     * @return the instance of this class.
     */
    @Nonnull
    public static Context get() {
        return new CoreContext() {
        };
    }

    @Nonnull
    @Override
    public String name() {
        return "Core";
    }

    @Nonnull
    @Override
    public BackendFactory factory() {
        return InMemoryBackendFactory.getInstance();
    }

    @Nonnull
    @Override
    public Config config() {
        return InMemoryConfig.newConfig();
    }

    /**
     * {@inheritDoc}
     * <p>
     * This {@code Context} doesn't support the {@link PersistentResource} loading.
     *
     * @throws UnsupportedOperationException every time
     */
    @Nonnull
    @Override
    public PersistentResource loadResource(File file) throws IOException {
        throw new UnsupportedOperationException();
    }
}
