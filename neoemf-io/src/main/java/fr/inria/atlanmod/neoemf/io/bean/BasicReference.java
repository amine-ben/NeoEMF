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

package fr.inria.atlanmod.neoemf.io.bean;

import fr.inria.atlanmod.neoemf.core.Id;

import org.eclipse.emf.ecore.EReference;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A {@link AbstractBasicFeature} representing a reference, which links several {@link BasicElement}s.
 */
@ParametersAreNonnullByDefault
public class BasicReference extends AbstractBasicFeature<EReference, Id> {

    /**
     * Whether this reference is a containment.
     */
    private boolean isContainment = false;

    /**
     * Returns whether this reference is a containment.
     *
     * @return {@code true} if this reference is a containment
     */
    public boolean isContainment() {
        return isContainment;
    }

    /**
     * Defines whether this reference is a containment.
     *
     * @param isContainment {@code true} if this reference is a containment
     */
    public void isContainment(boolean isContainment) {
        this.isContainment = isContainment;
    }
}
