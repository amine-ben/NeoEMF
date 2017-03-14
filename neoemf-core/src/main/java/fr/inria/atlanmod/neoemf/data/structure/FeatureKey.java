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

import fr.inria.atlanmod.neoemf.annotations.VisibleForTesting;
import fr.inria.atlanmod.neoemf.core.Id;
import fr.inria.atlanmod.neoemf.core.PersistentEObject;
import fr.inria.atlanmod.neoemf.core.StringId;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import static fr.inria.atlanmod.neoemf.util.Preconditions.checkNotNull;

/**
 * A simple representation of a {@link EStructuralFeature} of a {@link PersistentEObject}.
 */
@Immutable
@ParametersAreNonnullByDefault
public class FeatureKey implements Comparable<FeatureKey>, Externalizable {

    @SuppressWarnings("JavaDoc")
    private static final long serialVersionUID = -2197099155190693261L;

    /**
     * The identifier of the object.
     */
    @Nonnull
    protected Id id;

    /**
     * The name of the feature of the object.
     */
    @Nonnull
    protected String name;

    /**
     * Constructs a new {@code FeatureKey}.
     * <p>
     * <b>WARNING:</b> This constructor is intend to be used for serialization and tests.
     */
    @VisibleForTesting
    public FeatureKey() {
        this(new StringId(), "");
    }

    /**
     * Constructs a new {@code FeatureKey} with the given {@code id} and the given {@code name}, which are used as
     * a simple representation of a feature of an object.
     *
     * @param id   the identifier of the {@link PersistentEObject}
     * @param name the name of the {@link EStructuralFeature} of the {@link PersistentEObject}
     */
    protected FeatureKey(Id id, String name) {
        this.id = checkNotNull(id);
        this.name = checkNotNull(name);
    }

    /**
     * Creates a new {@code FeatureKey} from the given {@code internalObject} and the given {@code feature}.
     * <p>
     * This method behaves like: {@code from(PersistentEObject.from(internalObject), feature)}.
     *
     * @param internalObject the {@link InternalEObject} that will be adapted as {@link PersistentEObject}
     * @param feature        the {@link EStructuralFeature} of the {@link PersistentEObject}
     *
     * @return a new {@code FeatureKey}
     *
     * @see #from(PersistentEObject, EStructuralFeature)
     * @see PersistentEObject#from(Object)
     * @see EStructuralFeature#getName()
     */
    @Nonnull
    public static FeatureKey from(InternalEObject internalObject, EStructuralFeature feature) {
        return from(PersistentEObject.from(internalObject), feature);
    }

    /**
     * Creates a new {@code FeatureKey} from the given {@code object} and the given {@code feature}.
     * <p>
     * This method behaves like: {@code of(object.id(), feature.getName())}.
     *
     * @param object  the {@link PersistentEObject}
     * @param feature the {@link EStructuralFeature} of the {@link PersistentEObject}
     *
     * @return a new {@code FeatureKey}
     *
     * @see #of(Id, String)
     * @see PersistentEObject#id()
     * @see EStructuralFeature#getName()
     */
    @Nonnull
    public static FeatureKey from(PersistentEObject object, EStructuralFeature feature) {
        return of(object.id(), feature.getName());
    }

    /**
     * Creates a new {@code FeatureKey} with the given {@code id} and the given {@code name}, which are used as a
     * simple representation of a feature of an object.
     *
     * @param id   the identifier of the {@link PersistentEObject}
     * @param name the name of the {@link EStructuralFeature} of the {@link PersistentEObject}
     *
     * @return a new {@code FeatureKey}
     */
    @Nonnull
    public static FeatureKey of(Id id, String name) {
        return new FeatureKey(id, name);
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
     * Creates a new {@link ManyFeatureKey} with the {@link Id} and the name of this {@code FeatureKey}, and
     * adding the given {@code position}.
     *
     * @param position the position of the {@link EStructuralFeature}
     *
     * @return a new {@link ManyFeatureKey}
     *
     * @see ManyFeatureKey#of(Id, String)
     */
    @Nonnull
    public ManyFeatureKey withPosition(@Nonnegative int position) {
        return ManyFeatureKey.of(id, name, position);
    }

    @Override
    public int compareTo(FeatureKey o) {
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FeatureKey that = (FeatureKey) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public String toString() {
        return String.format("%s {%s # %s}", getClass().getSimpleName(), id, name);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(id);
        out.writeUTF(name);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        id = (Id) in.readObject();
        name = in.readUTF();
    }
}
