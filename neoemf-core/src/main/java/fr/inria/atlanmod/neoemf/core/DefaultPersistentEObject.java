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

package fr.inria.atlanmod.neoemf.core;

import fr.inria.atlanmod.neoemf.datastore.store.OwnedTransientStore;
import fr.inria.atlanmod.neoemf.datastore.store.PersistentStore;
import fr.inria.atlanmod.neoemf.resource.PersistentResource;
import fr.inria.atlanmod.neoemf.util.NeoEContentsEList;

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EStoreEObjectImpl;
import org.eclipse.emf.ecore.impl.MinimalEStoreEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Internal;
import org.eclipse.emf.ecore.util.EcoreEMap;

import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DefaultPersistentEObject extends MinimalEStoreEObjectImpl implements PersistentEObject {

    private static final int UNSETTED_FEATURE_ID = -1;

    private Id id;

    private Resource.Internal resource;

    private boolean isMapped;

    /**
     * The internal cached value of the eContainer. This information should be
     * also maintained in the underlying {@link EStore}.
     */
    private InternalEObject eContainer;

    private int eContainerFeatureId;

    private EStore eStore;

    public DefaultPersistentEObject() {
        this(StringId.generate());
    }

    protected DefaultPersistentEObject(Id id) {
        this.id = id;
        this.eContainerFeatureId = UNSETTED_FEATURE_ID;
        this.isMapped = false;
    }

    @Override
    public Id id() {
        return id;
    }

    @Override
    public void id(Id id) {
        this.id = id;
    }

    @Override
    public boolean isMapped() {
        return isMapped;
    }

    @Override
    public void setMapped(boolean mapped) {
        this.isMapped = mapped;
    }

    @Override
    public Internal resource() {
        return resource;
    }

    @Override
    public void resource(Internal resource) {
        this.resource = resource;
        EStore oldStore = eStore;
        // Set the new EStore
        if (resource instanceof PersistentResource) {
            eStore = ((PersistentResource) resource).eStore();
        }
        else {
            eStore = new OwnedTransientStore(this);
        }
        // Move contents from oldStore to eStore
        if (nonNull(oldStore) && nonNull(eStore) && eStore != oldStore) {
            // If the new store is different, initialize the new store with the data stored in the old store
            for (EStructuralFeature feature : eClass().getEAllStructuralFeatures()) {
                if (oldStore.isSet(this, feature)) {
                    if (!feature.isMany()) {
                        Object value = getAdaptedValue(oldStore, feature, PersistentStore.NO_INDEX);
                        if (nonNull(value)) {
                            eStore.set(this, feature, PersistentStore.NO_INDEX, value);
                        }
                    }
                    else {
                        eStore.clear(this, feature);
                        for (int i = 0; i < oldStore.size(this, feature); i++) {
                            Object value = getAdaptedValue(oldStore, feature, i);
                            if (nonNull(value)) {
                                eStore.add(this, feature, i, value);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public EObject eContainer() {
        EObject returnValue;
        if (resource instanceof PersistentResource) {
            InternalEObject container = eStore().getContainer(this);
            eBasicSetContainer(container);
            eBasicSetContainerFeatureID(eContainerFeatureID());
            returnValue = container;
        }
        else {
            returnValue = super.eContainer();
        }
        return returnValue;
    }

    @Override
    public Resource eResource() {
        return isNull(resource) ? super.eResource() : resource;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(getClass().getName());
        result.append('@');
        result.append(Integer.toHexString(hashCode()));

        if (eIsProxy()) {
            result.append(" (eProxyURI: ");
            result.append(eProxyURI());
            if (nonNull(eDynamicClass())) {
                result.append(" eClass: ");
                result.append(eDynamicClass());
            }
            result.append(')');
        }
        else if (nonNull(eDynamicClass())) {
            result.append(" (eClass: ");
            result.append(eDynamicClass());
            result.append(')');
        }
        else if (nonNull(eStaticClass())) {
            result.append(" (eClass: ");
            result.append(eStaticClass());
            result.append(')');
        }
        return result.toString();
    }

    @Override
    protected void eBasicSetContainer(InternalEObject newContainer) {
        eContainer = newContainer;
        if (nonNull(newContainer) && newContainer.eResource() != resource) {
            resource((Resource.Internal) eContainer.eResource());
        }
    }

    @Override
    protected void eBasicSetContainerFeatureID(int newContainerFeatureId) {
        eContainerFeatureId = newContainerFeatureId;
    }

    @Override
    public EList<EObject> eContents() {
        return NeoEContentsEList.createNeoEContentsEList(this);
    }

    private Object getAdaptedValue(EStore store, EStructuralFeature feature, int index) {
        Object value = store.get(this, feature, index);
        if (nonNull(value)) {
            if (feature instanceof EReference) {
                EReference eRef = (EReference) feature;
                if (eRef.isContainment()) {
                    PersistentEObject internalElement = PersistentEObject.from(value);
                    if (internalElement.resource() != resource()) {
                        internalElement.resource(resource());
                    }
                }
            }
        }
        return value;
    }

    @Override
    public EStore eStore() {
        if (isNull(eStore)) {
            eStore = new OwnedTransientStore(this);
        }
        return eStore;
    }

    @Override
    protected boolean eIsCaching() {
        return false;
    }

    @Override
    public Object dynamicGet(int dynamicFeatureId) {
        Object returnValue;
        final EStructuralFeature feature = eDynamicFeature(dynamicFeatureId);
        final EClassifier eType = feature.getEType();
        if (feature.isMany()) {
            if (Objects.equals(eType.getInstanceClassName(), "java.util.Map$Entry")) {
                returnValue = new EStoreEcoreEMap(eType, feature);
            }
            else {
                returnValue = new EStoreEcoreEList(feature);
            }
        }
        else {
            returnValue = eStore().get(this, feature, PersistentStore.NO_INDEX);
        }
        return returnValue;
    }

    @Override
    public void dynamicSet(int dynamicFeatureId, Object value) {
        EStructuralFeature feature = eDynamicFeature(dynamicFeatureId);
        if (feature.isMany()) {
            /*
             * TODO This operation should be atomic.
		     * Reset the old value in case the operation fails in the middle
		     */
            eStore().unset(this, feature);
            @SuppressWarnings("rawtypes")
            EList collection = (EList) value;
            for (int index = 0; index < collection.size(); index++) {
                eStore().set(this, feature, index, collection.get(index));
            }
        }
        else {
            eStore().set(this, feature, PersistentStore.NO_INDEX, value);
        }
    }

    @Override
    public void dynamicUnset(int dynamicFeatureId) {
        EStructuralFeature feature = eDynamicFeature(dynamicFeatureId);
        eStore().unset(this, feature);
    }

    /**
     * Returns the container of the {@link PersistentEObject}.
     * <p/>
     * Do not return the same value as standard EMF implementation if the container has not been accessed with the
     * public method {@link #eContainer()} before.
     *
     * @return the container of the {@link PersistentEObject}.
     */
    @Override
    public InternalEObject eInternalContainer() {
        // Do not load the container from the eStore here: it creates an important overhead and performance loss
        return isNull(eContainer) ? super.eInternalContainer() : eContainer;
    }

    @Override
    public int eContainerFeatureID() {
        if (eContainerFeatureId == UNSETTED_FEATURE_ID && resource instanceof PersistentResource) {
            EReference containingFeature = (EReference) eStore().getContainingFeature(this);
            if (nonNull(containingFeature)) {
                EReference oppositeFeature = containingFeature.getEOpposite();
                if (nonNull(oppositeFeature)) {
                    eBasicSetContainerFeatureID(eClass().getFeatureID(oppositeFeature));
                }
                else {
                    eBasicSetContainerFeatureID(
                            InternalEObject.EOPPOSITE_FEATURE_BASE
                                    - eInternalContainer().eClass().getFeatureID(containingFeature));
                }
            }
        }
        return eContainerFeatureId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (isNull(obj) || getClass() != obj.getClass()) {
            return false;
        }
        PersistentEObject other = (PersistentEObject) obj;
        return Objects.equals(id, other.id());
    }

    private class EStoreEcoreEMap extends EcoreEMap<Object, Object> {

        private static final long serialVersionUID = 1L;

        public EStoreEcoreEMap(EClassifier eType, EStructuralFeature feature) {
            super((EClass) eType, BasicEMap.Entry.class, null);
            delegateEList = new EntryBasicEStoreEList(feature);
            size = delegateEList.size();
        }

        private class EntryBasicEStoreEList extends EStoreEObjectImpl.BasicEStoreEList<Entry<Object, Object>> {

            private static final long serialVersionUID = 1L;

            public EntryBasicEStoreEList(EStructuralFeature feature) {
                super(DefaultPersistentEObject.this, feature);
            }

            @Override
            protected void didSet(int index, Entry<Object, Object> newObject, Entry<Object, Object> oldObject) {
                didRemove(index, oldObject);
                didAdd(index, newObject);
            }

            @Override
            protected void didAdd(int index, Entry<Object, Object> newObject) {
                doPut(newObject);
            }

            @Override
            protected void didRemove(int index, Entry<Object, Object> oldObject) {
                EStoreEcoreEMap.this.doRemove(oldObject);
            }

            @Override
            protected void didClear(int size, Object[] oldObjects) {
                EStoreEcoreEMap.this.doClear();
            }

            @Override
            protected void didMove(int index, Entry<Object, Object> movedObject, int oldIndex) {
                EStoreEcoreEMap.this.doMove(movedObject);
            }
        }
    }

    private class EStoreEcoreEList extends EStoreEObjectImpl.BasicEStoreEList<Object> {

        private static final long serialVersionUID = 1L;

        public EStoreEcoreEList(EStructuralFeature feature) {
            super(DefaultPersistentEObject.this, feature);
        }

        @Override
        public boolean contains(Object object) {
            return delegateContains(object);
        }

        /**
         * Override the default implementation which relies on size() to compute the insertion index by providing a
         * custom NO_INDEX features, meaning that the back-end has to append the result to the existing list.
         * <p>
         * This behavior allows fast write operation on back-ends which would otherwise need to deserialize the
         * underlying list to add the element at the specified index.
         */
        @Override
        public boolean add(Object object)
        {
            if (isUnique() && contains(object)) {
                return false;
            }
            else {
                if (eStructuralFeature instanceof EAttribute) {
                    addUnique(object);
                }
                else {
                    int index = size() == 0 ? 0 : PersistentStore.NO_INDEX;
                    addUnique(index, object);
                }
                return true;
            }
        }
    }
}