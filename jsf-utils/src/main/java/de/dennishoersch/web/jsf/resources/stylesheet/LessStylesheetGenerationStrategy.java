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
package de.dennishoersch.web.jsf.resources.stylesheet;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static de.dennishoersch.web.jsf.resources.ResourceMetadata.asResourceMetadata;
import static de.dennishoersch.web.jsf.resources.ResourceMetadata.isStylesheet;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.apache.myfaces.shared.renderkit.html.util.ResourceUtils;

import de.dennishoersch.web.jsf.resources.ResourceGenerationStrategy;
import de.dennishoersch.web.jsf.resources.GeneratedResourceMetadata;
import de.dennishoersch.web.jsf.resources.ResourceMetadata;

/**
 * Reads all registered stylesheets and lessifies them before registering a new single stylesheet resource instead of them.
 * @author hoersch
 */
public class LessStylesheetGenerationStrategy implements ResourceGenerationStrategy {

    @Override
    public Iterable<ResourceMetadata> collectResources(final UIViewRoot view, final FacesContext context) {
        List<UIComponent> resources = view.getComponentResources(context, "head");
        return transform(filter(resources, isStylesheet()), asResourceMetadata());
    }

    @Override
    public GeneratedResourceMetadata generateResource(FacesContext context, Collection<ResourceMetadata> resources, String generationKey, String version, String resourcesFolder) throws IOException {
        return new LessStylesheetBuilder(generationKey, resources, context, version, resourcesFolder).build();
    }

    @Override
    public final void replaceResources(UIViewRoot view, FacesContext context, Collection<ResourceMetadata> resources, GeneratedResourceMetadata generatedResource) {
        for (ResourceMetadata resource : resources) {
            ResourceUtils.markStylesheetAsRendered(context, resource.libraryName, resource.resourceName);
        }
        ResourceGenerationStrategy.Util.addOutputResource(context, ResourceUtils.DEFAULT_STYLESHEET_RENDERER_TYPE, generatedResource.libraryName, generatedResource.resourceName, "head");
    }
}
