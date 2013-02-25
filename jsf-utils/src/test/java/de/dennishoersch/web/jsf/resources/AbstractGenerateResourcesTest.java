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

import java.io.IOException;
import java.util.Collection;

import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.PreRenderComponentEvent;

import org.apache.commons.io.IOUtils;
import org.apache.myfaces.application.ResourceHandlerImpl;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import de.dennishoersch.web.jsf.BaseJSFTest;

/**
 * @author hoersch
 *
 */
public abstract class AbstractGenerateResourcesTest extends BaseJSFTest {

    /**
     * The current view.
     */
    protected UIViewRoot view;

    /**
     * The current wrapped editor
     */
    protected GenerateResourcesWrapper generateResourcesWrapper;

    @Override
    protected void setUpView() throws Exception {
        super.setUpView();

        view = facesContext.getViewRoot();

        GenerateResources generateResources = new GenerateResources(createStrategy(), "test");

        generateResourcesWrapper = new GenerateResourcesWrapper(generateResources);

        view.subscribeToEvent(PreRenderComponentEvent.class, toListener(generateResourcesWrapper));
    }

    /**
     * @return the helper for this tests
     */
    protected abstract ResourceGenerationStrategy createStrategy();

    @Override
    protected void setUpServletObjects() throws Exception {
        super.setUpServletObjects();

        servletContext.addInitParameter(ResourceHandlerImpl.INIT_PARAM_STRICT_JSF_2_ALLOW_SLASH_LIBRARY_NAME, "true");
    }

    /**
     * @return all resources for the body that are not rendered until now
     */
    protected Collection<UIComponent> getBodyResources() {
        return Collections2.filter(view.getComponentResources(facesContext, "body"), isRenderedResource());
    }

    /**
     * @return all resources for the head that are not rendered until now
     */
    protected Collection<UIComponent> getHeadResources() {
        return Collections2.filter(view.getComponentResources(facesContext, "head"), isRenderedResource());
    }

    /**
     * Builds the view for the given JSF page.
     * @param file
     * @throws IOException
     */
    protected void buildView(String file) throws IOException {
        vdl.buildView(facesContext, view, file);
    }

    private Predicate<UIComponent> isRenderedResource() {
        return new Predicate<UIComponent>() {

            @Override
            public boolean apply(UIComponent input) {
                String libraryName = (String) input.getAttributes().get("library");
                String resourceName = (String) input.getAttributes().get("name");
                return !isAlreadyRenderedResource(libraryName, resourceName);
            }

        };
    }

    /**
     *
     * @param libraryName
     * @param resourceName
     * @return whether the given resources is already marked as rendered
     */
    protected abstract boolean isAlreadyRenderedResource(String libraryName, String resourceName);

    /**
     * @param resource
     * @return the content of the given resource
     * @throws IOException
     */
    protected String getContent(UIComponent resource) throws IOException {
        String libraryName = (String) resource.getAttributes().get("library");
        String resourceName = (String) resource.getAttributes().get("name");
        return getContent(libraryName, resourceName);
    }

    /**
     *
     * @param libraryName
     * @param resourceName
     * @return the content of the given resource
     * @throws IOException
     */
    protected String getContent(String libraryName, String resourceName) throws IOException {
        Resource resource = application.getResourceHandler().createResource(resourceName, libraryName);
        if (resource == null) {
            throw new RuntimeException(String.format("Could not load resource %s : %s", libraryName, resourceName));
        }
        return IOUtils.toString(resource.getInputStream());
    }

    private static ComponentSystemEventListener toListener(GenerateResourcesWrapper wrapper) {
        return new WrapperToListener(wrapper);
    }

    private static class WrapperToListener implements ComponentSystemEventListener {

        private final GenerateResourcesWrapper _wrapper;

        WrapperToListener(GenerateResourcesWrapper editor) {
            _wrapper = editor;
        }

        @Override
        public void processEvent(ComponentSystemEvent event) {
            UIViewRoot target = (UIViewRoot) event.getSource();
            _wrapper.edit(target, FacesContext.getCurrentInstance());
        }
    }

    protected static class GenerateResourcesWrapper {

        private final GenerateResources _delegate;

        private boolean _wasCalled = false;

        private int _calls = 0;

        GenerateResourcesWrapper(GenerateResources delegate) {
            _delegate = delegate;
        }

        public void edit(UIViewRoot component, FacesContext context) {
            _calls++;
            _delegate.edit(component, context);
            _wasCalled = true;
        }

        /**
         * @return whether the editor was called
         */
        public Boolean wasCalled() {
            return Boolean.valueOf(_wasCalled);
        }

        /**
         * @return count of calls on this editor
         */
        protected Integer calls() {
            return Integer.valueOf(_calls);
        }
    }
}
