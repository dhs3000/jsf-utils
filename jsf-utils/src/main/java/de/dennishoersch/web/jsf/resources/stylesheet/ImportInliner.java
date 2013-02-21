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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;

/**
 * Inlines all possible imported stylesheets into one stylesheet.
 *
 * Import statements must be closed by a semicolon!
 *
 * @author hoersch
 */
public class ImportInliner {

    private static final Logger logger = Logger.getLogger(ImportInliner.class.getName());

    private static final String IMPORT_TAG = "@import";

    // @import url("erweitert.css");
    private static final Pattern _IMPORT_WITH_URL = Pattern.compile("@import url\\(['\"]([^;\n\r\n]*)['\"]\\);");

    // @import "allgemein.css";
    private static final Pattern _IMPORT_WITHOUT_URL = Pattern.compile("@import ['\"]([^;\n\r\n]*)['\"];");

    private static final Joiner JOINER = Joiner.on("\r\n");

    private final String _resourceName;

    private final String _libraryName;

    private final ResourceHandler _resourceHandler;

    /**
     * @param resourceName
     * @param libraryName
     * @param context
     */
    public ImportInliner(String resourceName, String libraryName, FacesContext context) {
        this._resourceName = resourceName;
        this._libraryName = libraryName;
        _resourceHandler = context.getApplication().getResourceHandler();
    }

    /**
     * @return the complete stylesheet with inlined imported stylesheets as possible
     * @throws IOException
     */
    public String execute() throws IOException {
        Resource resource = _resourceHandler.createResource(_resourceName, _libraryName);
        if (resource == null) {
            throw new IllegalStateException("Stylesheet '" + _libraryName + ":" + _resourceName + "' not found!");
        }
        return inlineImportsOfStylesheet(new InputStreamReader(resource.getInputStream()));
    }

    private String inlineImportsOfStylesheet(Reader stylesheet) throws IOException {
        BufferedReader reader = new BufferedReader(stylesheet);
        List<String> lines = Lists.newArrayList();
        String line;
        while ((line = reader.readLine()) != null) {
            String lineOrReplaced = inlineImportIfNeccessary(line);
            lines.add(lineOrReplaced);
        }
        return JOINER.join(lines);
    }

    private String inlineImportIfNeccessary(String line) throws IOException {
        if (!line.contains(IMPORT_TAG) || !removeCssComments(line).contains(IMPORT_TAG)) {
            return line;
        }

        // extract the URL
        String result = line;
        result = replaceImports(result, _IMPORT_WITH_URL);
        result = replaceImports(result, _IMPORT_WITHOUT_URL);

        if (line.equals(result)) {
            return line;
        }

        return inlineImportsOfStylesheet(new StringReader(result));
    }

    private String replaceImports(String line, Pattern withUrl) throws IOException {
        String result = line;
        Matcher matcher = withUrl.matcher(line);
        while (matcher.find()) {
            String importTag = matcher.group(1);
            String importedStylesheet = inlineImport(importTag);
            if (importedStylesheet != null) {
                result = StringUtils.replaceOnce(result, matcher.group(), importedStylesheet);
            }
        }
        return result;
    }

    private String inlineImport(String importTag) throws IOException {
        if (importTag.contains(ResourceHandler.RESOURCE_IDENTIFIER)) {
            // JSF resource URLs look like 'bla.css.faces?ln=css/... & xxx'
            String resource = importTag.substring(importTag.indexOf(ResourceHandler.RESOURCE_IDENTIFIER) + ResourceHandler.RESOURCE_IDENTIFIER.length() + 1);

            String resourceName = getResourceName(resource);
            String libraryName = getLibraryName(resource);

            return inlineByJSFResource(libraryName, resourceName);
        }
        throw new IllegalStateException("Stylesheet '" + _libraryName + ":" + _resourceName + "' contains an @import '" + importTag + "', which can't be inlined (seams to be no JSF resource)!");
    }

    private String inlineByJSFResource(String libraryName, String resourceName) throws IOException {
        Resource resource = _resourceHandler.createResource(resourceName, libraryName);
        if (resource != null) {
            logger.log(Level.INFO, String.format("Inlined resource: '%s:%s'", libraryName, resourceName));
            return new String(ByteStreams.toByteArray(resource.getInputStream()));
        }
        throw new IllegalStateException("Stylesheet '" + _libraryName + ":" + _resourceName + "' contains an @import of resource '" + libraryName + ":" + resourceName + "', which can't be handled / located by JSF resource handler!");
    }

    private String getResourceName(String resource) {
        String resourceName = resource.substring(0, resource.indexOf('?'));
        resourceName = resourceName.replace(".xhtml", "").replace(".faces", "").replace(".jsf", ""); // replace the standard-jsf extensions
        return resourceName.trim();
    }

    private String getLibraryName(String resource) {
        String libraryName = resource.substring(resource.indexOf("ln=") + 3);
        int next = libraryName.indexOf('&');
        if (next >= 0) {
            libraryName = libraryName.substring(0, next);
        }
        return libraryName.trim();
    }

    static String removeCssComments(String s) {
        String result = s.replaceAll("(?s)/\\*.*?\\*/", ""); // Mehrzeilige Java-Kommentare entfernen, dann einzeilige (falls '//' im Kommentar)
        result = result.replaceAll("(?)//.*", ""); // Einzeilige Java-Kommentare entfernen
        return result;
    }
}
