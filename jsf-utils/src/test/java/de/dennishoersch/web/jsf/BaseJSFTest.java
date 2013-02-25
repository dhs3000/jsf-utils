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
package de.dennishoersch.web.jsf;

import java.io.IOException;
import java.io.StringWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.test.mock.MockResponseWriter;
import org.apache.myfaces.view.facelets.FaceletTestCase;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.google.common.collect.Iterables;

/**
 * @author hoersch
 */
public abstract class BaseJSFTest extends FaceletTestCase {

    /**
     *
     * @param component
     * @param facesContext
     * @return the encoded html
     * @throws IOException
     */
    public static String getHtml(UIComponent component, FacesContext facesContext) throws IOException {
        StringWriter sw = new StringWriter();
        MockResponseWriter mrw = new MockResponseWriter(sw);
        facesContext.setResponseWriter(mrw);

        component.encodeAll(facesContext);
        sw.flush();
        return sw.toString();
    }


    protected static <T extends Iterable<?>> Size<T> size(int size) {
        return new Size<T>(size);
    }

    protected static class Size<T extends Iterable<?>> extends TypeSafeMatcher<T> {
        private final int size;

        public Size(int size) {
            this.size = size;
        }

        @Override
        public boolean matchesSafely(T item) {
            int size_ = Iterables.size(item);
            return size_ == size;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("size of collection is " + size);
        }

    }
}
