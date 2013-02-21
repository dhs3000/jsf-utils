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
package de.dennishoersch.web.jsf.resources.javascript;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static de.dennishoersch.web.jsf.resources.ResourceMetadata.asResourceMetadata;
import static de.dennishoersch.web.jsf.resources.ResourceMetadata.isScript;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.apache.myfaces.shared.renderkit.html.util.ResourceUtils;

import de.dennishoersch.web.jsf.resources.GenerateResourcesHelper;
import de.dennishoersch.web.jsf.resources.GeneratedResourceMetadata;
import de.dennishoersch.web.jsf.resources.ResourceMetadata;

/**
 * @author hoersch
 */
public class JavascriptGenerateHelper implements GenerateResourcesHelper {

    @Override
    public Iterable<ResourceMetadata> collectResources(UIViewRoot view, FacesContext context) {
        List<UIComponent> headResources = view.getComponentResources(context, "head");
        List<UIComponent> bodyResources = view.getComponentResources(context, "body");
        return transform(filter(concat(headResources, bodyResources), isScript()), asResourceMetadata());
    }

    @Override
    public GeneratedResourceMetadata generateResource(FacesContext context, Collection<ResourceMetadata> scripts, String generationKey, String version, String resourcesFolder) throws IOException {
        return new JavascriptBuilder(generationKey, scripts, context, version, resourcesFolder).build();
    }

    @Override
    public void replaceResources(UIViewRoot view, FacesContext context, Collection<ResourceMetadata> resources, GeneratedResourceMetadata generatedResource) {
        for (ResourceMetadata resource : resources) {
            ResourceUtils.markScriptAsRendered(context, resource.libraryName, resource.resourceName);
        }

        GenerateResourcesHelper.Util.addOutputResource(context, ResourceUtils.DEFAULT_SCRIPT_RENDERER_TYPE, generatedResource.libraryName, generatedResource.resourceName, "body");

    }
}
