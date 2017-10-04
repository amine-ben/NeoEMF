/*
 * Copyright (c) 2013-2017 Atlanmod INRIA LINA Mines Nantes.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Atlanmod INRIA LINA Mines Nantes - initial API and implementation
 */

package fr.inria.atlanmod.neoemf.data.mapdb;

import fr.inria.atlanmod.neoemf.data.LocalPersistentBackend;
import fr.inria.atlanmod.neoemf.data.PersistentBackend;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A {@link PersistentBackend} that is responsible of low-level access to a MapDB database.
 * <p>
 * It wraps an existing MapDB database and provides facilities to create and retrieve elements.
 * <p>
 * <b>Note:</b> Instances of {@code MapDbBackend} are created by {@link MapDbBackendFactory} that provides an usable
 * database that can be manipulated by this wrapper.
 *
 * @see MapDbBackendFactory
 */
@ParametersAreNonnullByDefault
public interface MapDbBackend extends LocalPersistentBackend {
}
