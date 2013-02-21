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

import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.apache.myfaces.shared.renderkit.JSFAttr;
import org.apache.myfaces.shared.renderkit.html.util.ResourceUtils;

/**
 * Provides helper methods to the generation.
 *
 * @author hoersch
 */
public interface GenerateResourcesHelper {

    /**
     * Collects all resources to be processed.
     * @param view
     * @param context
     * @return resources
     */
    public Iterable<ResourceMetadata> collectResources(UIViewRoot view, FacesContext context);

    /**
     * Generates a new resource with the help of the given resources.
     * @param context
     * @param resources
     * @param generationKey
     * @param version
     * @param resourcesFolder
     * @return the metadata of the generated resource
     * @throws IOException
     */
    public GeneratedResourceMetadata generateResource(FacesContext context, Collection<ResourceMetadata> resources, String generationKey, String version, String resourcesFolder) throws IOException;

    /**
     * Replaces the resources with the given single one.
     * @param view
     * @param context
     * @param resources
     * @param generatedResource
     */
    public void replaceResources(UIViewRoot view, FacesContext context, Collection<ResourceMetadata> resources, GeneratedResourceMetadata generatedResource);

    public class Util {
        public static void addOutputResource(final FacesContext facesContext, final String rendererType, final String libraryName, final String resourceName, String target) {

            UIOutput outputResource = (UIOutput) facesContext.getApplication().createComponent(facesContext, ResourceUtils.JAVAX_FACES_OUTPUT_COMPONENT_TYPE, rendererType);
            outputResource.getAttributes().put(JSFAttr.LIBRARY_ATTR, libraryName);
            outputResource.getAttributes().put(JSFAttr.NAME_ATTR, resourceName);
            outputResource.setTransient(true);
            outputResource.setId(facesContext.getViewRoot().createUniqueId());
            facesContext.getViewRoot().addComponentResource(facesContext, outputResource, target);

        }
    }
}
