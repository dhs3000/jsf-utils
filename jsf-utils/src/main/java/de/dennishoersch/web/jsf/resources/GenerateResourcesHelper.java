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

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * Provides helper methods to the generation.
 *
 * @author hoersch
 */
public interface GenerateResourcesHelper {

    /**
     * Collects all resources (metadata) to be processed.
     * @param view
     * @param context
     * @return resources
     */
    public Collection<ResourceMetadata> collectResources(UIViewRoot view, FacesContext context);

    /**
     * Generates a new resource with the help of the given informations.
     * @param context
     * @param resources
     * @param generationKey
     * @param version
     * @param webbappPath
     * @return the metadata of the generated resource
     * @throws IOException
     */
    public GeneratedResourceMetadata generateResource(FacesContext context, Collection<ResourceMetadata> resources, String generationKey, String version, String webbappPath) throws IOException;

    /**
     * Re-locates the resources that are merged into a single one.
     * @param view
     * @param context
     * @param resources
     * @param generatedResource
     */
    public void replaceResources(UIViewRoot view, FacesContext context, Collection<ResourceMetadata> resources, GeneratedResourceMetadata generatedResource);
}
