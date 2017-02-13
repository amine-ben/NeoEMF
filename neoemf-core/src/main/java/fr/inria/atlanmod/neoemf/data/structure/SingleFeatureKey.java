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

package fr.inria.atlanmod.neoemf.data.structure;

import fr.inria.atlanmod.neoemf.core.Id;
import fr.inria.atlanmod.neoemf.core.PersistentEObject;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import java.io.Serializable;
import java.util.Objects;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple representation of a {@link EStructuralFeature} of a {@link PersistentEObject}.
 */
@ParametersAreNonnullByDefault
public class SingleFeatureKey implements Comparable<SingleFeatureKey>, Serializable {

    @SuppressWarnings("JavaDoc")
    private static final long serialVersionUID = 8486532626389142007L;

    /**
     * The identifier of the object.
     */
    @Nonnull
    protected final Id id;

    /**
     * The name of the feature of the object.
     */
    @Nonnull
    protected final String name;

    /**
     * Constructs a new {@code SingleFeatureKey} with the given {@code id} and the given {@code name}, which are used as
     * a simple representation of a feature of an object.
     *
     * @param id   the identifier of the {@link PersistentEObject}
     * @param name the name of the {@link EStructuralFeature} of the {@link PersistentEObject}
     */
    protected SingleFeatureKey(Id id, String name) {
        this.id = checkNotNull(id);
        this.name = checkNotNull(name);
    }

    /**
     * Creates a new {@code SingleFeatureKey} from the given {@code internalObject} and the given {@code feature}.
     * <p>
     * This method behaves like: {@code from(PersistentEObject.from(internalObject), feature)}.
     *
     * @param internalObject the {@link InternalEObject} that will be adapted as {@link PersistentEObject}
     * @param feature        the {@link EStructuralFeature} of the {@link PersistentEObject}
     *
     * @return a new {@code SingleFeatureKey}
     *
     * @see #from(PersistentEObject, EStructuralFeature)
     * @see PersistentEObject#from(Object)
     * @see EStructuralFeature#getName()
     */
    @Nonnull
    public static SingleFeatureKey from(InternalEObject internalObject, EStructuralFeature feature) {
        return from(PersistentEObject.from(internalObject), feature);
    }

    /**
     * Creates a new {@code SingleFeatureKey} from the given {@code object} and the given {@code feature}.
     * <p>
     * This method behaves like: {@code of(object.id(), feature.getName())}.
     *
     * @param object  the {@link PersistentEObject}
     * @param feature the {@link EStructuralFeature} of the {@link PersistentEObject}
     *
     * @return a new {@code SingleFeatureKey}
     *
     * @see #of(Id, String)
     * @see PersistentEObject#id()
     * @see EStructuralFeature#getName()
     */
    @Nonnull
    public static SingleFeatureKey from(PersistentEObject object, EStructuralFeature feature) {
        return of(object.id(), feature.getName());
    }

    /**
     * Creates a new {@code SingleFeatureKey} with the given {@code id} and the given {@code name}, which are used as a
     * simple representation of a feature of an object.
     *
     * @param id   the identifier of the {@link PersistentEObject}
     * @param name the name of the {@link EStructuralFeature} of the {@link PersistentEObject}
     *
     * @return a new {@code SingleFeatureKey}
     */
    @Nonnull
    public static SingleFeatureKey of(Id id, String name) {
        return new SingleFeatureKey(id, name);
    }

    /**
     * Returns the identifier of the {@link PersistentEObject}.
     *
     * @return the identifier of the object
     */
    @Nonnull
    public Id id() {
        return id;
    }

    /**
     * Returns the name of the {@link EStructuralFeature} of the {@link PersistentEObject}.
     *
     * @return the name of the feature
     */
    @Nonnull
    public String name() {
        return name;
    }

    /**
     * Creates a new {@link MultiFeatureKey} with the {@link Id} and the name of this {@code SingleFeatureKey}, and
     * adding the given {@code position}.
     *
     * @param position the position of the {@link EStructuralFeature}
     *
     * @return a new {@link MultiFeatureKey}
     *
     * @see MultiFeatureKey#of(Id, String)
     */
    @Nonnull
    public MultiFeatureKey withPosition(@Nonnegative int position) {
        return MultiFeatureKey.of(id, name, position);
    }

    @Override
    public int compareTo(SingleFeatureKey o) {
        final int EQUAL = 0;

        if (this == o) {
            return EQUAL;
        }
        int comparison = id.compareTo(o.id);

        if (comparison == EQUAL) {
            return name.compareTo(o.name);
        }
        else {
            return comparison;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SingleFeatureKey)) {
            return false;
        }

        SingleFeatureKey that = (SingleFeatureKey) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public String toString() {
        return "FK:{" + id + ", " + name + "}";
    }
}