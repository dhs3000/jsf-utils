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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;

import javax.faces.context.FacesContext;

import com.google.common.io.Files;

/**
 * @author hoersch
 *
 */
public abstract class AbstractResourceBuilder {

    private static final String _FILENAME_PREFIX = "g";

    private final String _generationKey;

    protected final Collection<ResourceMetadata> _resources;

    protected final FacesContext _context;

    private final String _version;

    private final String _resourcesFolder;

    private final String _libraryName;

    private final String _fileExtension;

    protected AbstractResourceBuilder(String generationKey, Collection<ResourceMetadata> resources, FacesContext context, String version, String resourcesFolder, String libraryName, String fileExtension) {
        _generationKey = generationKey;
        _resources = resources;
        _context = context;
        _version = version;
        _resourcesFolder = resourcesFolder;
        _libraryName = libraryName;
        _fileExtension = fileExtension;
    }

    protected final String readAndConcatFileContents() throws IOException, FileNotFoundException {
        StringWriter output = new StringWriter();
        try {
            for (ResourceMetadata stylesheet : _resources) {

                String content = readSingleResource(stylesheet);

                output.write(content);
            }
        } finally {
            output.close();
        }

        String styles = output.toString();
        return styles;
    }

    /**
     * @param resource
     * @return the contents of a single resource
     */
    protected abstract String readSingleResource(ResourceMetadata resource) throws IOException;

    protected final String asAbsoluteResourceFileName(String resourceFilename) throws IOException {
        return createAndGetResourceFilename(asGeneratedResource(resourceFilename));
    }

    private final String asGeneratedResource(String resourceFilename) {
        return _libraryName + "/" + resourceFilename;
    }

    protected final String getGeneratedFilename() {
        String cssFilename = _FILENAME_PREFIX + "-v" + _version + "-k" + _generationKey.hashCode() + _fileExtension;
        return cssFilename;
    }

    private final String createAndGetResourceFilename(String resourceName) throws IOException {
        File file = new File(_resourcesFolder + resourceName);
        Files.createParentDirs(file);
        file.createNewFile();
        return file.getAbsolutePath();
    }

    protected final GeneratedResourceMetadata newGeneratedResourceMetadata(String resourceName) {
        return new GeneratedResourceMetadata(_libraryName, resourceName);
    }
}
