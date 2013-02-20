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

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.apache.myfaces.shared.renderkit.JSFAttr;
import org.apache.myfaces.shared.renderkit.html.util.ResourceUtils;

import com.google.common.collect.Collections2;

import de.dennishoersch.web.jsf.resources.GenerateResourcesHelper;
import de.dennishoersch.web.jsf.resources.GeneratedResourceMetadata;
import de.dennishoersch.web.jsf.resources.ResourceMetadata;

/**
 * Reads all registered stylesheets and lessifies them before registering a new single stylesheet resource instead of them.
 * @author hoersch
 */
public class LessStylesheetGenerateHelper implements GenerateResourcesHelper {

    @Override
    public Collection<ResourceMetadata> collectResources(final UIViewRoot view, final FacesContext context) {
        List<UIComponent> resources = view.getComponentResources(context, "head");
        return Collections2.transform(Collections2.filter(resources, ResourceMetadata.isStylesheet()), ResourceMetadata.toResourceMetadata());
    }

    @Override
    public GeneratedResourceMetadata generateResource(FacesContext context, Collection<ResourceMetadata> resources, String generationKey, String version, String resourcesFolder) throws IOException {
        return new LessStylesheetBuilder(generationKey, resources, context, version, resourcesFolder).build();
    }

    @Override
    public final void relocateResources(UIViewRoot view, FacesContext context, Collection<ResourceMetadata> resources, GeneratedResourceMetadata generatedResource) {
        for (ResourceMetadata resource : resources) {
            // Die Resource nicht entfernen, da manche nur beim initialen
            // Request einer View hinzugefügt werden und somit bei folgenden
            // Requests verschwunden sind
            // bspw. HtmlPicklistRenderer und picklist.js
            ResourceUtils.markStylesheetAsRendered(context, resource.libraryName, resource.resourceName);
        }
        addOutputStylesheetResource(context, generatedResource.libraryName, generatedResource.resourceName, "all");
    }

    private static void addOutputStylesheetResource(final FacesContext facesContext, final String libraryName, final String resourceName, String media) {

        UIOutput outputStylesheet = (UIOutput) facesContext.getApplication().createComponent(facesContext, ResourceUtils.JAVAX_FACES_OUTPUT_COMPONENT_TYPE, ResourceUtils.DEFAULT_STYLESHEET_RENDERER_TYPE);
        outputStylesheet.getAttributes().put(JSFAttr.LIBRARY_ATTR, libraryName);
        outputStylesheet.getAttributes().put(JSFAttr.NAME_ATTR, resourceName);
        outputStylesheet.getAttributes().put("media", media);
        outputStylesheet.setTransient(true);
        outputStylesheet.setId(facesContext.getViewRoot().createUniqueId());
        facesContext.getViewRoot().addComponentResource(facesContext, outputStylesheet);
    }
}
