/*******************************************************************************
 * Copyright (c) 2013 Atlanmod INRIA LINA Mines Nantes
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Atlanmod INRIA LINA Mines Nantes - initial API and implementation
 *******************************************************************************/
package fr.inria.atlanmod.neoemf.tests;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.inria.atlanmod.neoemf.resources.PersistentResource;
import fr.inria.atlanmod.neoemf.test.commons.models.mapSample.MapSampleFactory;
import fr.inria.atlanmod.neoemf.test.commons.models.mapSample.MapSamplePackage;
import fr.inria.atlanmod.neoemf.test.commons.models.mapSample.Pack;
import fr.inria.atlanmod.neoemf.test.commons.models.mapSample.PackContent;

public class AllInstancesTest extends AllBackendTest {

    protected MapSampleFactory factory;
    // These variables should be updated if createResourceContent is changed
    protected int packCount = 6;
    protected int packContentCount = 50;
    
    @Before
    public void setUp() throws Exception {
        factory = MapSampleFactory.eINSTANCE;
        this.ePackage = MapSamplePackage.eINSTANCE;
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
    
    /**
     * Creates a Pack hierarchy containing 1 root, 5 sub-pack elements, and 10
     * PackContent elements in each sub-pack
     * @param r the PersistentResource to fill with the created model
     */
    protected void createResourceContent(PersistentResource r) {
        Pack rootPack = factory.createPack();
        rootPack.setName("root");
        for(int i = 0; i < 5; i++) {
            Pack newPack = factory.createPack();
            newPack.setName("pack"+i);
            rootPack.getPacks().add(newPack);
            for(int j = 0; j < 10; j++) {
                PackContent newPackContent = factory.createPackContent();
                newPackContent.setName("pContent"+i+ "-" + j);
                newPack.getOwnedContents().add(newPackContent);
            }
        }
        r.getContents().add(rootPack);
    }
    
}