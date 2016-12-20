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

package fr.inria.atlanmod.neoemf.data.berkeleydb;

import fr.inria.atlanmod.neoemf.core.StringId;
import fr.inria.atlanmod.neoemf.data.AbstractPersistenceBackendFactoryTest;
import fr.inria.atlanmod.neoemf.data.InvalidDataStoreException;
import fr.inria.atlanmod.neoemf.data.PersistenceBackend;
import fr.inria.atlanmod.neoemf.data.PersistenceBackendFactory;
import fr.inria.atlanmod.neoemf.data.berkeleydb.option.BerkeleyDBOptionsBuilder;
import fr.inria.atlanmod.neoemf.data.berkeleydb.store.DirectWriteBerkeleyDBCacheManyStore;
import fr.inria.atlanmod.neoemf.data.berkeleydb.store.DirectWriteBerkeleyDBIndicesStore;
import fr.inria.atlanmod.neoemf.data.berkeleydb.store.DirectWriteBerkeleyDBListsStore;
import fr.inria.atlanmod.neoemf.data.berkeleydb.store.DirectWriteBerkeleyDBStore;
import fr.inria.atlanmod.neoemf.data.store.AutocommitStoreDecorator;
import fr.inria.atlanmod.neoemf.data.store.PersistentStore;
import fr.inria.atlanmod.neoemf.data.structure.FeatureKey;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BerkeleyDBPersistenceBackendFactoryTest extends AbstractPersistenceBackendFactoryTest implements BerkeleyDBTest {

    @Test
    public void testCreateTransientBackend() {
        PersistenceBackend transientBackend = context().persistenceBackendFactory().createTransientBackend();
        assertThat(transientBackend).isInstanceOf(BerkeleyDBPersistenceBackend.class); // "Invalid backend created"

        // TODO Need to test further the nature of the BerkeleyDB engine
    }

    @Test
    public void testCreateTransientEStore() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        PersistenceBackend transientBackend = context().persistenceBackendFactory().createTransientBackend();

        PersistentStore eStore = context().persistenceBackendFactory().createTransientStore(null, transientBackend);
        assertThat(eStore).isInstanceOf(DirectWriteBerkeleyDBStore.class); // "Invalid EStore created"

        assertHasInnerBackend(eStore, transientBackend);
    }

    @Test
    public void testCreatePersistentBackendNoOption() throws InvalidDataStoreException {
        PersistenceBackend persistentBackend = context().persistenceBackendFactory().createPersistentBackend(file(), BerkeleyDBOptionsBuilder.newBuilder().asMap());
        assertThat(persistentBackend).isInstanceOf(BerkeleyDBPersistenceBackend.class); // "Invalid backend created"

        // TODO Need to test further the nature of the BerkeleyDB engine
    }

    @Test
    public void testCreatePersistentEStoreNoOption() throws InvalidDataStoreException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        PersistenceBackend persistentBackend = context().persistenceBackendFactory().createPersistentBackend(file(), BerkeleyDBOptionsBuilder.newBuilder().asMap());

        PersistentStore eStore = context().persistenceBackendFactory().createPersistentStore(null, persistentBackend, BerkeleyDBOptionsBuilder.newBuilder().asMap());
        assertThat(eStore).isInstanceOf(DirectWriteBerkeleyDBStore.class); // "Invalid EStore created"

        assertHasInnerBackend(eStore, persistentBackend);
    }

    @Test
    public void testCreatePersistentEStoreDirectWriteOption() throws InvalidDataStoreException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Map<String, Object> options = BerkeleyDBOptionsBuilder.newBuilder()
                .directWrite()
                .asMap();

        PersistenceBackend persistentBackend = context().persistenceBackendFactory().createPersistentBackend(file(), BerkeleyDBOptionsBuilder.newBuilder().asMap());

        PersistentStore eStore = context().persistenceBackendFactory().createPersistentStore(null, persistentBackend, options);
        assertThat(eStore).isInstanceOf(DirectWriteBerkeleyDBStore.class); // "Invalid EStore created"

        assertHasInnerBackend(eStore, persistentBackend);
    }

    @Test
    public void testCreatePersistentEStoreDirectWriteWithListsOption() throws InvalidDataStoreException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Map<String, Object> options = BerkeleyDBOptionsBuilder.newBuilder()
                .directWriteLists()
                .asMap();

        PersistenceBackend persistentBackend = context().persistenceBackendFactory().createPersistentBackend(file(), BerkeleyDBOptionsBuilder.newBuilder().asMap());

        PersistentStore eStore = context().persistenceBackendFactory().createPersistentStore(null, persistentBackend, options);
        assertThat(eStore).isInstanceOf(DirectWriteBerkeleyDBListsStore.class); // "Invalid EStore created"

        assertHasInnerBackend(eStore, persistentBackend);
    }

    @Test
    public void testCreatePersistentEStoreDirectWriteWithIndexesOption() throws InvalidDataStoreException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Map<String, Object> options = BerkeleyDBOptionsBuilder.newBuilder()
                .directWriteIndices()
                .asMap();

        PersistenceBackend persistentBackend = context().persistenceBackendFactory().createPersistentBackend(file(), BerkeleyDBOptionsBuilder.newBuilder().asMap());

        PersistentStore eStore = context().persistenceBackendFactory().createPersistentStore(null, persistentBackend, options);
        assertThat(eStore).isInstanceOf(DirectWriteBerkeleyDBIndicesStore.class); // "Invalid EStore created"

        assertHasInnerBackend(eStore, persistentBackend);
    }

    @Test
    public void testCreatePersistentEStoreAutocommitOption() throws InvalidDataStoreException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Map<String, Object> options = BerkeleyDBOptionsBuilder.newBuilder()
                .autocommit()
                .asMap();

        PersistenceBackend persistentBackend = context().persistenceBackendFactory().createPersistentBackend(file(), BerkeleyDBOptionsBuilder.newBuilder().asMap());

        PersistentStore eStore = context().persistenceBackendFactory().createPersistentStore(null, persistentBackend, options);
        assertThat(eStore).isInstanceOf(AutocommitStoreDecorator.class); // "Invalid EStore created"

        assertHasInnerBackend(eStore, persistentBackend);
    }

    @Test
    public void testCreatePersistentEStoreCachedManyOption() throws InvalidDataStoreException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Map<String, Object> options = BerkeleyDBOptionsBuilder.newBuilder()
                .directWriteCacheMany()
                .asMap();

        PersistenceBackend persistentBackend = context().persistenceBackendFactory().createPersistentBackend(file(), BerkeleyDBOptionsBuilder.newBuilder().asMap());

        PersistentStore eStore = context().persistenceBackendFactory().createPersistentStore(null, persistentBackend, options);
        assertThat(eStore).isInstanceOf(DirectWriteBerkeleyDBCacheManyStore.class); // "Invalid EStore created"

        assertHasInnerBackend(eStore, persistentBackend);
    }

    /**
     * Test if {@link PersistenceBackendFactory#copyBackend} creates the persistent data stores from the transient ones.
     * Only empty backends are tested.
     */
    //@Test
    public void testCopyBackend() throws InvalidDataStoreException {
        PersistenceBackend transientBackend = context().persistenceBackendFactory().createTransientBackend();
        assertThat(transientBackend).isInstanceOf(BerkeleyDBPersistenceBackend.class); // "Transient backend is not an instance of BerkeleyDBPersistenceBackend"
        BerkeleyDBPersistenceBackend transientMap = (BerkeleyDBPersistenceBackend) transientBackend;

        PersistenceBackend persistentBackend = context().persistenceBackendFactory().createPersistentBackend(file(), BerkeleyDBOptionsBuilder.newBuilder().asMap());
        assertThat(persistentBackend).isInstanceOf(BerkeleyDBPersistenceBackend.class); // "Persistent backend is not an instance of BerkeleyDBPersistenceBackend"

        BerkeleyDBPersistenceBackend persistentMap = (BerkeleyDBPersistenceBackend) persistentBackend;

        context().persistenceBackendFactory().copyBackend(transientMap, persistentMap);
        for (String tKey : transientMap.getAll().keySet()) {
            assertThat(persistentMap.getAll()).containsKey(tKey); // "Persistent backend does not contain the key"
            assertThat(persistentMap.getAll().get(tKey)).isEqualTo(transientMap.get(tKey)); // "Persistent backend structure %s is not equal to transient one"
        }
    }

    @Test
    public void testTransientBackend() {
        BerkeleyDBPersistenceBackend backend = (BerkeleyDBPersistenceBackend) context()
                .persistenceBackendFactory().createTransientBackend();

        for (int i = 0; i < 1000; i++) {
            FeatureKey key = FeatureKey.of(new StringId("object" + i), "name" + i);
            assertThat(backend.storeValue(key, "value" + i)).isNotNull();
        }

        backend.close();

        BerkeleyDBPersistenceBackend other = (BerkeleyDBPersistenceBackend) context()
                .persistenceBackendFactory().createTransientBackend();


        for (int i = 0; i < 1000; i++) {
            assertThat(other.isFeatureSet(FeatureKey.of(new StringId("object" + i), "name" + i))).isFalse();
        }

        other.close();

    }

}
