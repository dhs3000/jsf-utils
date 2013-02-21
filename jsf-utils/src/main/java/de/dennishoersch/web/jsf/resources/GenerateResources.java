/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dennishoersch.web.jsf.resources;

import static com.google.common.base.Functions.toStringFunction;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.dennishoersch.web.jsf.utils.FacesConfig;

/**
 * Merges and relocates a set of resources with the helpers of the given {@link GenerateResourcesHelper}.
 *
 * @author hoersch
 */
public class GenerateResources {
    /** a joiner joining with comma and skipping null values. */
    private static final Joiner _COMMA_SEPARATED_JOINER = Joiner.on(",").skipNulls();

    static final String RESOURCES_FOLDER_NAME = "resources";

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final Map<String, GeneratedResourceMetadata> _generatedResources = Maps.newConcurrentMap();

    private final Map<String, CountDownLatch> _pendingGeneratedResources = Maps.newConcurrentMap();

    private final Lock _pendingLock = new ReentrantLock();

    private final GenerateResourcesHelper _helper;

    private final String _version;

    private String _resourcesFolder;

    /**
     *
     * @param helper
     */
    public GenerateResources(GenerateResourcesHelper helper, String version) {
        _helper = helper;
        _version = version;
    }

    public void edit(UIViewRoot view, FacesContext context) {
        if (context.getPartialViewContext().isAjaxRequest()) {
            return;
        }

        initWith(context);

        Collection<ResourceMetadata> resources = extractResources(view, context);
        if (resources.isEmpty()) {
            return;
        }

        String generationKey = asGenerationKey(resources);

        GeneratedResourceMetadata generatedResource = _generatedResources.get(generationKey);
        if (mustGenerate(generatedResource) || FacesConfig.isDevelopment(context)) {
            // To avoid multiple attempts of generating at the same time, set a
            // barrier for any latter incoming request until it is ready
            // generated.
            waitIfPending(generationKey);

            generatedResource = _generatedResources.get(generationKey);

            if (mustGenerate(generatedResource)) {
                try {

                    generatedResource = _helper.generateResource(context, resources, generationKey, _version, _resourcesFolder);
                    _generatedResources.put(generationKey, generatedResource);

                    awakeWaiters(generationKey);
                } catch (IOException e) {
                    Throwables.propagate(e);
                } finally {
                    removePending(generationKey);
                }
            }

        }

        _helper.replaceResources(view, context, resources, generatedResource);
    }

    private void waitIfPending(String generationKey) {
        CountDownLatch isPending = null;
        _pendingLock.lock();
        try {
            isPending = _pendingGeneratedResources.get(generationKey);
            if (isPending == null) {
                CountDownLatch pendingCountDown = new CountDownLatch(1);
                _pendingGeneratedResources.put(generationKey, pendingCountDown);
            }
        } finally {
            _pendingLock.unlock();
        }

        if (isPending != null) {
            try {
                boolean timeout = !isPending.await(15, TimeUnit.SECONDS);
                if (timeout) {
                    logger.log(Level.WARNING, "Timeout reached while waiting for merged resources! Reason is either because it took to long or the creator thread had an exception.");
                    throw new IllegalStateException("Timeout reached while waiting for merged resources! Reason is either because it took to long or the creator thread had an exception.");
                }
            } catch (InterruptedException e) {
                Throwables.propagate(e);
            }
        }

    }

    private void awakeWaiters(String generationKey) {
        _pendingLock.lock();
        try {
            _pendingGeneratedResources.get(generationKey).countDown();
        } finally {
            _pendingLock.unlock();
        }
    }

    private void removePending(String generationKey) {
        _pendingLock.lock();
        try {
            _pendingGeneratedResources.remove(generationKey);
        } finally {
            _pendingLock.unlock();
        }

    }

    private void initWith(FacesContext context) {
        if (_resourcesFolder == null) {
            _resourcesFolder = context.getExternalContext().getRealPath("/");
            if (!_resourcesFolder.endsWith("/")) {
                _resourcesFolder += "/";
            }
            _resourcesFolder += RESOURCES_FOLDER_NAME + "/";
        }
    }

    private Set<ResourceMetadata> extractResources(UIViewRoot view, FacesContext context) {
        return Sets.newLinkedHashSet(_helper.collectResources(view, context));
    }

    private String asGenerationKey(Collection<ResourceMetadata> resources) {
        return _COMMA_SEPARATED_JOINER.join(Iterables.transform(resources, toStringFunction()));
    }

    private boolean mustGenerate(GeneratedResourceMetadata generatedResource) {
        if (generatedResource == null) {
            return true;
        }
        return false;
    }
}
