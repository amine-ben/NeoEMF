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

package fr.inria.atlanmod.neoemf.eclipse.ui.editor;

import fr.inria.atlanmod.neoemf.data.blueprints.option.BlueprintsOptionsBuilder;
import fr.inria.atlanmod.neoemf.data.blueprints.util.BlueprintsURI;
import fr.inria.atlanmod.neoemf.data.mapdb.option.MapDbOptionsBuilder;
import fr.inria.atlanmod.neoemf.data.mapdb.util.MapDbURI;
import fr.inria.atlanmod.neoemf.logging.NeoLogger;
import fr.inria.atlanmod.neoemf.resource.PersistentResource;
import fr.inria.atlanmod.neoemf.resource.PersistentResourceFactory;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.ecore.presentation.EcoreEditorPlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.util.EditUIUtil;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.Tree;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class NeoEditor extends EcoreEditor {

    public static final String EDITOR_ID = NeoEditor.class.getName();

    public NeoEditor() {
        super();
        this.editingDomain.getResourceSet().getResourceFactoryRegistry().getProtocolToFactoryMap().put(BlueprintsURI.SCHEME, PersistentResourceFactory.getInstance());
        this.editingDomain.getResourceSet().getResourceFactoryRegistry().getProtocolToFactoryMap().put(MapDbURI.SCHEME, PersistentResourceFactory.getInstance());
    }

    @Override
    public void createModel() {
        URI resourceURI = EditUIUtil.getURI(getEditorInput());
        Resource resource = editingDomain.getResourceSet().createResource(resourceURI);
        editingDomain.getResourceSet().eAdapters().add(problemIndicationAdapter);

        // Create the store options depending of the backend
        Map<String, Object> options;
        if (Objects.equals(resource.getURI().scheme(), MapDbURI.SCHEME)) {
            options = MapDbOptionsBuilder.newBuilder()
//                  .log()
                    .directWriteCacheMany()
                    .asMap();
        }
        else if (Objects.equals(resource.getURI().scheme(), BlueprintsURI.SCHEME)) {
            options = BlueprintsOptionsBuilder.newBuilder()
//                  .log()
                    .directWriteCacheMany()
                    .asMap();
        }
        else {
            options = Collections.emptyMap();
        }

        try {
            resource.load(options);
        }
        catch (IOException e) {
            NeoLogger.error(e, "Unable to create model for the editor");
            for (Resource r : editingDomain.getResourceSet().getResources()) {
                NeoLogger.info(resource.getURI().toString());
                if (r instanceof PersistentResource) {
                    PersistentResource persistentResource = (PersistentResource) r;
                    persistentResource.close();
                }
                else {
                    r.unload();
                }
            }
        }
    }

    @Override
    public void createPages()
    {
        Instant begin = Instant.now();
        createModel();

        // Only creates the other pages if there is something that can be edited
        if (!getEditingDomain().getResourceSet().getResources().isEmpty()) {
            // Create a page for the selection tree view.
            Tree tree = new Tree(getContainer(), SWT.VIRTUAL | SWT.FULL_SELECTION);
//	        Tree tree = new Tree(getContainer(), SWT.VIRTUAL | SWT.MULTI);
            selectionViewer = new TreeViewer(tree);
            setCurrentViewer(selectionViewer);

//	        selectionViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
            selectionViewer.setContentProvider(new LazyAdapterFactoryContentProvider(adapterFactory));
//	        selectionViewer.setLabelProvider(new DecoratingColumLabelProvider(new AdapterFactoryLabelProvider(adapterFactory), new DiagnosticDecorator(editingDomain, selectionViewer, EcoreEditorPlugin.getPlugin().getDialogSettings())));
            selectionViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
            selectionViewer.setUseHashlookup(true);
            selectionViewer.setInput(editingDomain.getResourceSet());
//	        selectionViewer.setSelection(new StructuredSelection(editingDomain.getResourceSet().getResources().get(0)), true);

//	        new AdapterFactoryTreeEditor(selectionViewer.getTree(), adapterFactory);
//	        new ColumnViewerInformationControlToolTipSupport(selectionViewer, new DiagnosticDecorator.EditingDomainLocationListener(editingDomain, selectionViewer));

            createContextMenuFor(selectionViewer);
            int pageIndex = addPage(tree);
            setPageText(pageIndex, EcoreEditorPlugin.INSTANCE.getString("_UI_SelectionPage_label"));

            getSite().getShell().getDisplay().asyncExec(() -> setActivePage(0));
        }

        // Ensures that this editor will only display the page's tab area if there are more than one page
        getContainer().addControlListener(new ControlAdapter() {
            boolean guard = false;

            @Override
            public void controlResized(ControlEvent event) {
                if (!guard) {
                    guard = true;
                    hideTabs();
                    guard = false;
                }
            }
        });

        getSite().getShell().getDisplay().asyncExec(this::updateProblemIndication);

        Instant end = Instant.now();

        NeoLogger.info("NeoEMF Editor Opened in {0}", Duration.between(begin, end));
    }

    @Override
    public void dispose() {
        NeoLogger.info("Disposing NeoEditor");

        for (Resource resource : editingDomain.getResourceSet().getResources()) {
            if (resource instanceof PersistentResource) {
                ((PersistentResource) resource).close();
            }
            else {
                resource.unload();
            }
        }
        super.dispose();
    }
}