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

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import org.apache.myfaces.shared.resource.BaseResourceHandlerSupport;
import org.apache.myfaces.shared.resource.ResourceHandlerSupport;

import com.google.common.io.ByteStreams;

/**
 * @author hoersch
 */
public class ResourceUtils {
    private static ResourceHandlerSupport _resourceHandlerSupport;

    private ResourceUtils() {
    }

    public static boolean isResourceURL(String url) {
        return url.contains(ResourceHandler.RESOURCE_IDENTIFIER);
    }

    private static String getResourcePart(String url) {
        return url.substring(url.indexOf(ResourceHandler.RESOURCE_IDENTIFIER) + ResourceHandler.RESOURCE_IDENTIFIER.length() + 1);
    }

    public static String getResourceName(String url) {
        String resource = getResourcePart(url);

        String resourceName = resource.substring(0, resource.indexOf('?'));
        String mapping = getMapping();
        if (mapping != null) { // replace the mapping
            resourceName = resourceName.replace(mapping, "");
        }
        return resourceName.trim();
    }

    public static String getLibraryName(String url) {
        String resource = getResourcePart(url);

        String libraryName = resource.substring(resource.indexOf("ln=") + 3);
        int next = libraryName.indexOf('&');
        if (next >= 0) {
            libraryName = libraryName.substring(0, next);
        }
        return libraryName.trim();
    }

    public static Resource createResource(FacesContext context, String libraryName, String resourceName, String contentType) {
        return context.getApplication().getResourceHandler().createResource(resourceName, libraryName, contentType);
    }

    public static byte[] getResourceContent(FacesContext context, String libraryName, String resourceName) throws IOException {
        return getResourceContent(context, libraryName, resourceName, null);
    }

    public static byte[] getResourceContent(FacesContext context, String libraryName, String resourceName, String contentType) throws IOException {
        Resource resource = createResource(context, libraryName, resourceName, contentType);
        if (resource == null) {
            return null;
        }
        return ByteStreams.toByteArray(resource.getInputStream());
    }

    private static String getMapping() {
        if (_resourceHandlerSupport == null) {
            _resourceHandlerSupport = new BaseResourceHandlerSupport();
        }
        return _resourceHandlerSupport.getMapping();
    }
}
