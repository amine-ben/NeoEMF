/*
 * Copyright (c) 2013 Atlanmod INRIA LINA Mines Nantes.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Atlanmod INRIA LINA Mines Nantes - initial API and implementation
 */

package fr.inria.atlanmod.neoemf.graph.blueprints.datastore;

import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import fr.inria.atlanmod.neoemf.AllTest;
import fr.inria.atlanmod.neoemf.datastore.InvalidDataStoreException;
import fr.inria.atlanmod.neoemf.datastore.PersistenceBackend;
import fr.inria.atlanmod.neoemf.datastore.PersistenceBackendFactory;
import fr.inria.atlanmod.neoemf.datastore.PersistenceBackendFactoryRegistry;
import fr.inria.atlanmod.neoemf.datastore.estores.DirectWriteResourceEStore;
import fr.inria.atlanmod.neoemf.datastore.estores.SearcheableResourceEStore;
import fr.inria.atlanmod.neoemf.datastore.estores.impl.AutocommitEStoreImpl;
import fr.inria.atlanmod.neoemf.graph.blueprints.datastore.estores.impl.DirectWriteBlueprintsResourceEStoreImpl;
import fr.inria.atlanmod.neoemf.graph.blueprints.resources.BlueprintsResourceOptions;
import fr.inria.atlanmod.neoemf.graph.blueprints.util.NeoBlueprintsURI;
import fr.inria.atlanmod.neoemf.logger.NeoLogger;
import fr.inria.atlanmod.neoemf.resources.PersistentResourceOptions;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BlueprintsPersistenceBackendFactoryTest extends AllTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private static final String TEST_FILENAME = "graphPersistenceBackendFactoryTestFile";

    private PersistenceBackendFactory persistenceBackendFactory;
    private File testFile;
    private Map<Object, Object> options = new HashMap<>();
    private List<PersistentResourceOptions.StoreOption> storeOptions = new ArrayList<>();

    @Before
    public void setUp() {
        persistenceBackendFactory = BlueprintsPersistenceBackendFactory.getInstance();
        PersistenceBackendFactoryRegistry.register(NeoBlueprintsURI.NEO_GRAPH_SCHEME, persistenceBackendFactory);
        testFile = temporaryFolder.getRoot().toPath().resolve(TEST_FILENAME + new Date().getTime()).toFile();
        options.put(PersistentResourceOptions.STORE_OPTIONS, storeOptions);
    }

    @After
    public void tearDown() {
        PersistenceBackendFactoryRegistry.unregisterAll();

        temporaryFolder.delete();

        if (temporaryFolder.getRoot().exists()) {
            try {
                FileUtils.forceDeleteOnExit(temporaryFolder.getRoot());
            } catch (IOException e) {
                NeoLogger.warn(e);
            }
        }

        testFile = null;
    }

    @Test
    public void testCreateTransientBackend() {
        PersistenceBackend transientBackend = persistenceBackendFactory.createTransientBackend();
        assertThat(transientBackend).isInstanceOf(BlueprintsPersistenceBackend.class); // "Invalid backend created"

        BlueprintsPersistenceBackend graph = (BlueprintsPersistenceBackend) transientBackend;
        assertThat(graph.getBaseGraph()).isInstanceOf(TinkerGraph.class); // "The base graph is not a TinkerGraph"
    }

    @Test
    public void testCreateTransientEStore() {
        PersistenceBackend transientBackend = persistenceBackendFactory.createTransientBackend();

        SearcheableResourceEStore eStore = persistenceBackendFactory.createTransientEStore(null, transientBackend);
        assertThat(eStore).isInstanceOf(DirectWriteBlueprintsResourceEStoreImpl.class); // "Invalid EStore created"

        assertHasInnerBackend(eStore, transientBackend);
    }

    @Test
    public void testCreatePersistentBackendNoOptionNoConfigFile() throws InvalidDataStoreException {
        PersistenceBackend persistentBackend = persistenceBackendFactory.createPersistentBackend(testFile, Collections.emptyMap());
        assertThat(persistentBackend).isInstanceOf(BlueprintsPersistenceBackend.class); // "Invalid backend created"

        BlueprintsPersistenceBackend graph = (BlueprintsPersistenceBackend) persistentBackend;
        assertThat(graph.getBaseGraph()).isInstanceOf(TinkerGraph.class); // "The base graph is not the default TinkerGraph"
    }

    @Test
    public void testCreatePersistentEStoreNoOption() throws InvalidDataStoreException {
        PersistenceBackend persistentBackend = persistenceBackendFactory.createPersistentBackend(testFile, Collections.emptyMap());

        SearcheableResourceEStore eStore = persistenceBackendFactory.createPersistentEStore(null, persistentBackend, Collections.emptyMap());
        assertThat(eStore).isInstanceOf(DirectWriteBlueprintsResourceEStoreImpl.class); // "Invalid EStore created"

        assertHasInnerBackend(eStore, persistentBackend);
    }

    @Test
    public void testCreatePersistentEStoreDirectWriteOption() throws InvalidDataStoreException {
        storeOptions.add(BlueprintsResourceOptions.EStoreGraphOption.DIRECT_WRITE);

        PersistenceBackend persistentBackend = persistenceBackendFactory.createPersistentBackend(testFile, Collections.emptyMap());

        SearcheableResourceEStore eStore = persistenceBackendFactory.createPersistentEStore(null, persistentBackend, options);
        assertThat(eStore).isInstanceOf(DirectWriteBlueprintsResourceEStoreImpl.class); // "Invalid EStore created"

        assertHasInnerBackend(eStore, persistentBackend);
    }

    @Test
    public void testCreatePersistentEStoreAutocommitOption() throws InvalidDataStoreException {
        storeOptions.add(BlueprintsResourceOptions.EStoreGraphOption.AUTOCOMMIT);

        PersistenceBackend persistentBackend = persistenceBackendFactory.createPersistentBackend(testFile, Collections.emptyMap());

        SearcheableResourceEStore eStore = persistenceBackendFactory.createPersistentEStore(null, persistentBackend, options);
        assertThat(eStore).isInstanceOf(AutocommitEStoreImpl.class); // "Invalid EStore created";

        assertHasInnerBackend(eStore, persistentBackend);
    }

    private PersistenceBackend getInnerBackend(DirectWriteResourceEStore store) {
        return store.getPersistenceBackend();
    }

    private void assertHasInnerBackend(SearcheableResourceEStore store, PersistenceBackend expectedInnerBackend) {
        PersistenceBackend innerBackend = getInnerBackend((DirectWriteResourceEStore) store);
        assertThat(innerBackend).isSameAs(expectedInnerBackend); // "The backend in the EStore is not the created one"
    }
}
