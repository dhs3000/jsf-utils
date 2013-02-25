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

/**
 * @author hoersch
 *
 */
public class JavascriptBuilderTest  extends AbstractGenerateResourcesTest {
    @Override
    protected boolean isAlreadyRenderedResource(String libraryName, String resourceName) {
        return ResourceUtils.isRenderedScript(facesContext, libraryName, resourceName);
    }

    @Override
    protected ResourceGenerationStrategy createStrategy() {
        return new JavascriptGenerationStrategy();
    }

    @Test
    public void test_two_less_stylesheets_result_in_one_normal_without_less() throws IOException {
        buildView("pages/test_include_2_javascripts.xhtml");
        {
            Collection<UIComponent> headResources = getHeadResources();

            assertThat(headResources, size(2));
        }

        application.publishEvent(facesContext, PreRenderComponentEvent.class, view);

        assertThat(generateResourcesWrapper.wasCalled(), equalTo(Boolean.TRUE));

        // All javascripts are in the end of the body
        {
            Collection<UIComponent> headResources = getHeadResources();

            assertThat(headResources, size(0));
        }
        {
            Collection<UIComponent> bodyResources = getBodyResources();

            assertThat(bodyResources, size(1));

            // Contains all definitions of both files
            // bla={simpleFunc:function(){alert("HELP!")}};bla.simpleFunc();
            String content = getContent(Iterables.get(bodyResources, 0));
            assertThat(content, containsString("alert("));
            assertThat(content, containsString("bla.simpleFunc()"));

            assertThat(content, not(containsString("{}")));
        }
    }
}
