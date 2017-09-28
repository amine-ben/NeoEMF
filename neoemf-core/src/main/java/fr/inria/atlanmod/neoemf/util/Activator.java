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

package fr.inria.atlanmod.neoemf.util;

import fr.inria.atlanmod.commons.annotation.VisibleForReflection;
import fr.inria.atlanmod.neoemf.bind.Bindings;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * The OSGi activator of this module.
 */
@VisibleForReflection
@ParametersAreNonnullByDefault
public final class Activator implements BundleActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        Bindings.withContext(context);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        // Do nothing
    }
}