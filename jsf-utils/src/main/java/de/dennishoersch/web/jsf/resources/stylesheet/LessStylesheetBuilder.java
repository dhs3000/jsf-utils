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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import org.lesscss.LessCompiler;
import org.lesscss.LessException;

import com.google.common.io.Files;

import de.dennishoersch.web.jsf.resources.GeneratedResourceMetadata;
import de.dennishoersch.web.jsf.resources.ResourceMetadata;

/**
 *
 * @author hoersch
 */
public class LessStylesheetBuilder {

    private final Logger logger = Logger.getLogger(getClass().getName());

    /** Library name of the generated resources. */
    public static final String GENERATED_LIB = "generated.css";

    private final Collection<ResourceMetadata> _stylesheets;

    private static final String _FILENAME_PREFIX = "g";

    private final String _resourcesFolder;

    private final String _fileExtension = ".css";

    private final String _version;

    /** the key of the generation. */
    private final String _generationKey;

    private final FacesContext _context;

    /**
     * @param generationKey
     * @param stylesheets
     * @param context
     * @param version
     * @param resourcesFolder
     */
    public LessStylesheetBuilder(String generationKey, Collection<ResourceMetadata> stylesheets, FacesContext context, String version, String resourcesFolder) {
        _generationKey = generationKey;
        _stylesheets = stylesheets;
        _context = context;
        _version = version;
        _resourcesFolder = resourcesFolder;
    }

    /**
     *
     * @return the resource metadata
     * @throws IOException
     */
    public GeneratedResourceMetadata build() throws IOException {

        String stylesheet = collectFileContents();

        stylesheet = lessify(stylesheet);

        String cssFilename = getGeneratedFilename();
        String resourceFileName = getResourceFile(cssFilename);

        Files.write(stylesheet, new File(resourceFileName), Charset.defaultCharset());

        GeneratedResourceMetadata generatedStylesheet = new GeneratedResourceMetadata(GENERATED_LIB, cssFilename);

        return generatedStylesheet;
    }

    private final String collectFileContents() throws IOException, FileNotFoundException {
        StringWriter output = new StringWriter();
        try {
            for (ResourceMetadata stylesheet : _stylesheets) {

                String css = new ImportInliner(stylesheet.resourceName, stylesheet.libraryName, _context).execute();

                output.write(css);
            }
        } finally {
            output.close();
        }

        String styles = output.toString();
        return styles;
    }

    private String lessify(String stylesheet) {
        // TODO: kann der als Klassen variable oder so mit lock benutzt werden?
        LessCompiler lessCompiler = new LessCompiler();
        lessCompiler.setCompress(true);

        try {
            String compiled = lessCompiler.compile(stylesheet);
            return compiled;
        } catch (LessException e) {
            throw new IllegalStateException(e);
        }
    }

    private final String getResourceFile(String resourceFilename) throws IOException {
        return createAndGetResourceFilename(asGeneratedResource(resourceFilename));
    }

    private final String asGeneratedResource(String resourceFilename) {
        return GENERATED_LIB + "/" + resourceFilename;
    }

    private final String getGeneratedFilename() {
        String cssFilename = _FILENAME_PREFIX + "-v" + _version + "-k" + _generationKey.hashCode() + _fileExtension;
        return cssFilename;
    }

    private final String createAndGetResourceFilename(String resourceName) throws IOException {
        File file = new File(_resourcesFolder + resourceName);
        Files.createParentDirs(file);
        file.createNewFile();
        return file.getAbsolutePath();
    }
}
