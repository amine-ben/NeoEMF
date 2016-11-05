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

package fr.inria.atlanmod.neoemf.benchmarks.ase2015;

import fr.inria.atlanmod.neoemf.benchmarks.CdoQuery;
import fr.inria.atlanmod.neoemf.benchmarks.ase2015.queries.ASE2015Queries;
import fr.inria.atlanmod.neoemf.benchmarks.cdo.EmbeddedCDOServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.ecore.resource.Resource;

public class ASE2015CdoQuery extends CdoQuery {

    private static final Logger LOG = LogManager.getLogger();

    public void queryASE2015GetBranchStatements(String in) {
        try {
            org.eclipse.gmt.modisco.java.cdo.impl.JavaPackageImpl.init();

            try (EmbeddedCDOServer server = new EmbeddedCDOServer(in)) {
                server.run();
                CDOSession session = server.openSession();
                CDOTransaction transaction = session.openTransaction();
                Resource resource = transaction.getRootResource();

                ASE2015Queries.getCommentsTagContent(resource).callWithMemoryUsage();

                transaction.close();
                session.close();
            }
        }
        catch (Exception e) {
            LOG.error(e.toString());
        }
    }

    public void queryASE2015Grabats09(String in) {
        try {
            org.eclipse.gmt.modisco.java.cdo.impl.JavaPackageImpl.init();

            try (EmbeddedCDOServer server = new EmbeddedCDOServer(in)) {
                server.run();
                CDOSession session = server.openSession();
                CDOTransaction transaction = session.openTransaction();
                Resource resource = transaction.getRootResource();

                ASE2015Queries.grabats09(resource).callWithMemoryUsage();

                transaction.close();
                session.close();
            }
        }
        catch (Exception e) {
            LOG.error(e.toString());
        }
    }

    public void queryASE2015InvisibleMethodDeclarations(String in) {
        try {
            org.eclipse.gmt.modisco.java.cdo.impl.JavaPackageImpl.init();

            try (EmbeddedCDOServer server = new EmbeddedCDOServer(in)) {
                server.run();
                CDOSession session = server.openSession();
                CDOTransaction transaction = session.openTransaction();
                Resource resource = transaction.getRootResource();

                ASE2015Queries.getInvisibleMethodDeclarations(resource).callWithMemoryUsage();

                transaction.close();
                session.close();
            }
        }
        catch (Exception e) {
            LOG.error(e.toString());
        }
    }

    public void queryASE2015SpecificInvisibleMethodDeclarations(String in) {
        try {
            org.eclipse.gmt.modisco.java.cdo.impl.JavaPackageImpl.init();

            try (EmbeddedCDOServer server = new EmbeddedCDOServer(in)) {
                server.run();
                CDOSession session = server.openSession();
                CDOTransaction transaction = session.openTransaction();
                Resource resource = transaction.getRootResource();

                ASE2015Queries.getSpecificInvisibleMethodDeclarations(resource).callWithMemoryUsage();

                transaction.close();
                session.close();
            }
        }
        catch (Exception e) {
            LOG.error(e.toString());
        }
    }

    public void queryASE2015ThrownExceptions(String in) {
        try {
            org.eclipse.gmt.modisco.java.cdo.impl.JavaPackageImpl.init();

            try (EmbeddedCDOServer server = new EmbeddedCDOServer(in)) {
                server.run();
                CDOSession session = server.openSession();
                CDOTransaction transaction = session.openTransaction();
                Resource resource = transaction.getRootResource();

                ASE2015Queries.getThrownExceptions(resource).callWithMemoryUsage();

                transaction.close();
                session.close();
            }
        }
        catch (Exception e) {
            LOG.error(e.toString());
        }
    }
}
