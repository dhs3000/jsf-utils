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
package de.dennishoersch.web.jsf.resources.stylesheet.cssutis;

import static de.dennishoersch.web.css.images.ImagesInliner.with;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import de.dennishoersch.web.css.Normalizer;
import de.dennishoersch.web.css.images.resolver.HttpPathResolver;
import de.dennishoersch.web.css.images.resolver.URLPathResolver;
import de.dennishoersch.web.jsf.resources.ResourceUtils;
import de.dennishoersch.web.jsf.resources.stylesheet.StylesheetProcessor;

/**
 * See {@link Normalizer}.
 *
 * @author hoersch
 */
public class InlineImagesStylesheetProcessor implements StylesheetProcessor {
    private static final Logger logger = Logger.getLogger(InlineImagesStylesheetProcessor.class.getName());

    @Override
    public String process(String stylesheet, FacesContext context) {
        try {
            return with(new JSFPathResolver(context), new HttpPathResolver()).process(stylesheet);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Exception while trying to inline images into a stylesheet", e);
        }
        return stylesheet;
    }

    private static class JSFPathResolver implements URLPathResolver {

        private final FacesContext _context;

        public JSFPathResolver(FacesContext context) {
            _context = context;
        }

        @Override
        public Path resolve(String url) throws IOException {
            if (ResourceUtils.isResourceURL(url)) {

                String resourceName = ResourceUtils.getResourceName(url);
                String libraryName = ResourceUtils.getLibraryName(url);

                byte[] content = ResourceUtils.getResourceContent(_context, libraryName, resourceName);

                Path tempFile = Files.createTempFile("__" + JSFPathResolver.class.getSimpleName() + "_", ".tmp");
                Files.write(tempFile, content);

                return tempFile;
            }

            return null;
        }

    }
}
