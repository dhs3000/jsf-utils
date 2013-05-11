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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import com.google.common.io.Files;

import de.dennishoersch.web.jsf.resources.AbstractResourceBuilder;
import de.dennishoersch.web.jsf.resources.GeneratedResourceMetadata;
import de.dennishoersch.web.jsf.resources.ResourceMetadata;

/**
 *
 * @author hoersch
 */
public class ProcessingStylesheetBuilder extends AbstractResourceBuilder {
    private static final Logger logger = Logger.getLogger(ProcessingStylesheetBuilder.class.getName());

    /** Library name of the generated resource. */
    private static final String GENERATED_LIB = "generated.css";

    /** file extension of the generated resource. */
    private static final String FILE_EXTENSION = ".css";

    private static final String CONTENT_TYPE = "text/css";

    private final StylesheetProcessor _processor;

    /**
     * @param processor
     * @param generationKey
     * @param stylesheets
     * @param context
     * @param version
     * @param resourcesFolder
     */
    public ProcessingStylesheetBuilder(StylesheetProcessor processor, String generationKey, Collection<ResourceMetadata> stylesheets, FacesContext context, String version, String resourcesFolder) {
        super(generationKey, stylesheets, context, version, resourcesFolder, GENERATED_LIB, FILE_EXTENSION);
        _processor = processor;
    }

    /**
     *
     * @return the resource metadata
     * @throws IOException
     */
    public GeneratedResourceMetadata build() throws IOException {

        String stylesheet = readAndConcatFileContents();

        stylesheet = _processor.process(stylesheet);

        String cssFilename = getGeneratedFilename();
        String resourceFileName = asAbsoluteResourceFileName(cssFilename);

        Files.write(stylesheet, new File(resourceFileName), Charset.defaultCharset());

        return newGeneratedResourceMetadata(cssFilename);
    }

    @Override
    protected String readSingleResource(ResourceMetadata stylesheet) throws IOException {
        logger.log(Level.INFO, String.format("Read style resource: '%s:%s'", stylesheet.libraryName, stylesheet.resourceName));
        return new ImportInliner(stylesheet.resourceName, stylesheet.libraryName, CONTENT_TYPE, _context).execute();
    }
}
