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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

import de.dennishoersch.web.jsf.resources.AbstractResourceBuilder;
import de.dennishoersch.web.jsf.resources.GeneratedResourceMetadata;
import de.dennishoersch.web.jsf.resources.ResourceMetadata;

/**
 * @author hoersch
 */
public class JavascriptBuilder extends AbstractResourceBuilder {
    private static final Logger logger = Logger.getLogger(JavascriptBuilder.class.getName());

    /** Library name of the generated resource. */
    private static final String GENERATED_LIB = "generated.js";

    /** file extension of the generated resource. */
    private static final String FILE_EXTENSION = ".js";

    private final ResourceHandler _resourceHandler;
    /**
     * @param generationKey
     * @param resources
     * @param context
     * @param version
     * @param resourcesFolder
     * @param libraryName
     * @param fileExtension
     */
    protected JavascriptBuilder(String generationKey, Collection<ResourceMetadata> scripts, FacesContext context, String version, String resourcesFolder) {
        super(generationKey, scripts, context, version, resourcesFolder, FILE_EXTENSION, GENERATED_LIB);
        _resourceHandler = context.getApplication().getResourceHandler();
    }

    /**
     *
     * @return the resource metadata
     * @throws IOException
     */
    public GeneratedResourceMetadata build() throws IOException {

        String scripts = readAndConcatFileContents();

        String scriptFilename = getGeneratedFilename();
        String resourceFileName = asAbsoluteResourceFileName(scriptFilename);

        Files.write(scripts, new File(resourceFileName), Charset.defaultCharset());

        return newGeneratedResourceMetadata(scriptFilename);
    }

    @Override
    protected String readSingleResource(ResourceMetadata resource) throws IOException {
        Resource r = _resourceHandler.createResource(resource.resourceName, resource.libraryName);
        if (r != null) {
            logger.log(Level.INFO, String.format("Read script resource: '%s:%s'", resource.libraryName, resource.resourceName));
            return new String(ByteStreams.toByteArray(r.getInputStream()));
        }
        throw new IllegalStateException("Could not read script '" + resource.libraryName + ":" + resource.resourceName + "!");
    }
}
