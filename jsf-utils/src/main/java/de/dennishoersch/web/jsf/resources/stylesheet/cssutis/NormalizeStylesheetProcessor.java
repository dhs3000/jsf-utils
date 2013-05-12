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

import javax.faces.context.FacesContext;

import de.dennishoersch.web.css.Normalizer;
import de.dennishoersch.web.css.parser.Parser;
import de.dennishoersch.web.jsf.resources.stylesheet.StylesheetProcessor;

/**
 * See {@link Normalizer}.
 * @author hoersch
 */
public class NormalizeStylesheetProcessor implements StylesheetProcessor {

    @Override
    public String process(String stylesheet, FacesContext context) {
        return Parser.parse(stylesheet).toString();
    }
}