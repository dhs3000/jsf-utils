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
package de.dennishoersch.web.jsf.resources.stylesheet.less;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Collection;

import javax.faces.component.UIComponent;
import javax.faces.event.PreRenderComponentEvent;

import org.apache.myfaces.shared.renderkit.html.util.ResourceUtils;
import org.junit.Test;

import com.google.common.collect.Iterables;

import de.dennishoersch.web.jsf.resources.AbstractGenerateResourcesTest;
import de.dennishoersch.web.jsf.resources.ResourceGenerationStrategy;
import de.dennishoersch.web.jsf.resources.stylesheet.ProcessingStylesheetGenerationStrategy;

/**
 * @author hoersch
 */
public class LessStylesheetBuilderTest extends AbstractGenerateResourcesTest {
    @Override
    protected boolean isAlreadyRenderedResource(String libraryName, String resourceName) {
        return ResourceUtils.isRenderedStylesheet(facesContext, libraryName, resourceName);
    }

    @Override
    protected ResourceGenerationStrategy createStrategy() {
        return ProcessingStylesheetGenerationStrategy.with(new LessStylesheetProcessor());
    }

    @Test
    public void test_two_less_stylesheets_result_in_one_normal_without_less() throws IOException {
        buildView("pages/test_include_2_less_stylesheets.xhtml");
        {
            Collection<UIComponent> headResources = getHeadResources();

            assertThat(headResources, size(2));
        }

        application.publishEvent(facesContext, PreRenderComponentEvent.class, view);

        assertThat(generateResourcesWrapper.wasCalled(), equalTo(Boolean.TRUE));

        {
            Collection<UIComponent> headResources = getHeadResources();

            assertThat(headResources, size(1));

            // Contains all definitions of both files and no less anymore
            String content = getContent(Iterables.get(headResources, 0));
            assertThat(content, containsString(".baseStyle"));
            assertThat(content, containsString(".otherStyle"));
            assertThat(content, not(containsString("myColor1")));
            assertThat(content, not(containsString("myColor2")));
        }
    }

    @Test
    public void test_less_stylesheet_with_import() throws IOException {
        buildView("pages/test_include_1_less_stylesheet_with_import.xhtml");
        {
            Collection<UIComponent> headResources = getHeadResources();

            assertThat(headResources, size(1));
        }

        application.publishEvent(facesContext, PreRenderComponentEvent.class, view);

        assertThat(generateResourcesWrapper.wasCalled(), equalTo(Boolean.TRUE));

        {
            Collection<UIComponent> headResources = getHeadResources();

            assertThat(headResources, size(1));

            // Contains all definitions of both files and no less anymore
            String content = getContent(Iterables.get(headResources, 0));
            assertThat(content, containsString(".baseStyle"));
            assertThat(content, containsString(".afterImportStyle"));

            assertThat(content, containsString(".importedStyle"));

            assertThat(content, not(containsString("myColor1")));
            assertThat(content, not(containsString("myColor2")));
        }
    }


}
