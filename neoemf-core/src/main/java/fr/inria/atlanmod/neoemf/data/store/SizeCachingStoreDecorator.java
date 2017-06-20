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

package fr.inria.atlanmod.neoemf.data.store;

import fr.inria.atlanmod.neoemf.core.Id;
import fr.inria.atlanmod.neoemf.data.structure.ManyFeatureKey;
import fr.inria.atlanmod.neoemf.data.structure.SingleFeatureKey;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A {@link Store} wrapper that caches the size data.
 */
@ParametersAreNonnullByDefault
public class SizeCachingStoreDecorator extends AbstractCachingStoreDecorator<SingleFeatureKey, Optional<Integer>> {

    /**
     * Constructs a new {@code SizeCachingStoreDecorator}.
     *
     * @param store the inner store
     */
    @SuppressWarnings("unused") // Called dynamically
    public SizeCachingStoreDecorator(Store store) {
        super(store);
    }

    @Override
    public <V> void unsetValue(SingleFeatureKey key) {
        cacheSize(key, 0);
        super.unsetValue(key);
    }

    @Override
    public void unsetReference(SingleFeatureKey key) {
        cacheSize(key, 0);
        super.unsetReference(key);
    }

    @Override
    public <V> void addValue(ManyFeatureKey key, V value) {
        cacheSize(key.withoutPosition(), sizeOfValue(key.withoutPosition()).orElse(0) + 1);
        super.addValue(key, value);
    }

    @Override
    public <V> void addAllValues(ManyFeatureKey key, List<? extends V> collection) {
        cacheSize(key.withoutPosition(), sizeOfValue(key.withoutPosition()).orElse(0) + collection.size());
        super.addAllValues(key, collection);
    }

    @Nonnegative
    @Override
    public <V> int appendValue(SingleFeatureKey key, V value) {
        int position = super.appendValue(key, value);
        cacheSize(key, position + 1);
        return position;
    }

    @Nonnegative
    @Override
    public <V> int appendAllValues(SingleFeatureKey key, List<? extends V> collection) {
        int firstPosition = super.appendAllValues(key, collection);
        cacheSize(key, firstPosition + collection.size());
        return firstPosition;
    }

    @Nonnull
    @Override
    public <V> Optional<V> removeValue(ManyFeatureKey key) {
        sizeOfValue(key.withoutPosition()).ifPresent(s -> cacheSize(key.withoutPosition(), s - 1));
        return super.removeValue(key);
    }

    @Override
    public <V> void removeAllValues(SingleFeatureKey key) {
        cacheSize(key, 0);
        super.removeAllValues(key);
    }

    @Nonnull
    @Nonnegative
    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public <V> Optional<Integer> sizeOfValue(SingleFeatureKey key) {
        return cache.get(key, super::sizeOfValue);
    }

    @Override
    public void addReference(ManyFeatureKey key, Id reference) {
        cacheSize(key.withoutPosition(), sizeOfReference(key.withoutPosition()).orElse(0) + 1);
        super.addReference(key, reference);
    }

    @Override
    public void addAllReferences(ManyFeatureKey key, List<Id> collection) {
        cacheSize(key.withoutPosition(), sizeOfReference(key.withoutPosition()).orElse(0) + collection.size());
        super.addAllReferences(key, collection);
    }

    @Nonnegative
    @Override
    public int appendReference(SingleFeatureKey key, Id reference) {
        int position = super.appendReference(key, reference);
        cacheSize(key, position + 1);
        return position;
    }

    @Nonnegative
    @Override
    public int appendAllReferences(SingleFeatureKey key, List<Id> collection) {
        int firstPosition = super.appendAllReferences(key, collection);
        cacheSize(key, firstPosition + collection.size());
        return firstPosition;
    }

    @Nonnull
    @Override
    public Optional<Id> removeReference(ManyFeatureKey key) {
        sizeOfReference(key.withoutPosition()).ifPresent(s -> cacheSize(key.withoutPosition(), s - 1));
        return super.removeReference(key);
    }

    @Override
    public void removeAllReferences(SingleFeatureKey key) {
        cacheSize(key, 0);
        super.removeAllReferences(key);
    }

    @Nonnull
    @Nonnegative
    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public Optional<Integer> sizeOfReference(SingleFeatureKey key) {
        return cache.get(key, super::sizeOfReference);
    }

    /**
     * Defines the number of elements of the given {@code key}.
     *
     * @param key  the key to define the size
     * @param size the size
     */
    private void cacheSize(SingleFeatureKey key, @Nonnegative int size) {
        cache.put(key, size != 0 ? Optional.of(size) : Optional.empty());
    }
}
