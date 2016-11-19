/*
 * Copyright (c) 2013-2016 Atlanmod INRIA LINA Mines Nantes.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Atlanmod INRIA LINA Mines Nantes - initial API and implementation
 */

package fr.inria.atlanmod.neoemf.io.hash.impl;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import fr.inria.atlanmod.neoemf.io.hash.HashCode;
import fr.inria.atlanmod.neoemf.io.hash.Hasher;

import static java.util.Objects.isNull;

/**
 * A {@link Hasher hasher} using the {@code MD5} algorithm.
 */
public class Md5Hasher implements Hasher {

    private static final HashFunction hashFunction = Hashing.md5();

    private static Hasher INSTANCE;

    private Md5Hasher() {
    }

    public static Hasher getInstance() {
        if (isNull(INSTANCE)) {
            INSTANCE = new Md5Hasher();
        }
        return INSTANCE;
    }

    @Override
    public HashCode hash(String value) {
        String hash = hashFunction.newHasher().putString(value, Charsets.UTF_8).hash().toString();

        return new StringHashCode(hash);
    }
}