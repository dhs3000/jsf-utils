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

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import de.dennishoersch.web.jsf.resources.javascript.JavascriptGenerationStrategy;
import de.dennishoersch.web.jsf.resources.stylesheet.ProcessingStylesheetGenerationStrategy;
import de.dennishoersch.web.jsf.resources.stylesheet.cssutis.InlineImagesStylesheetProcessor;
import de.dennishoersch.web.jsf.resources.stylesheet.cssutis.NormalizeStylesheetProcessor;
import de.dennishoersch.web.jsf.resources.stylesheet.less.LessStylesheetProcessor;

/**
 * Listener that post-processes the view root and the registered stylesheets and scripts.
 *
 * <p>
 * Usage: Register the listener in faces-config.xml with
 *
 * <pre>
 * &lt;system-event-listener>
 *     &lt;system-event-listener-class>de.dennishoersch.web.jsf.resources.PostProcessResourcesPreRenderListener&lt;/system-event-listener-class>
 *     &lt;system-event-class>javax.faces.event.PreRenderComponentEvent&lt;/system-event-class>
 * &lt;/system-event-listener> *
 * </pre>
 *
 * </p>
 *
 * @author hoersch
 */
public class PostProcessResourcesPreRenderListener implements SystemEventListener {

    //@formatter:off
    private GenerateResources _stylesheetPostProcessor = new GenerateResources(
            ProcessingStylesheetGenerationStrategy.with(
                    new LessStylesheetProcessor(),
                    new NormalizeStylesheetProcessor(),
                    new InlineImagesStylesheetProcessor()),
            "0.1");
    //@formatter:on

    private GenerateResources _javascriptPostProcessor = new GenerateResources(new JavascriptGenerationStrategy(), "0.1");

    @Override
    public boolean isListenerForSource(Object source) {
        return UIViewRoot.class.isAssignableFrom(source.getClass());
    }

    @Override
    public void processEvent(SystemEvent event) {
        UIViewRoot view = (UIViewRoot) event.getSource();

        postProcessStyles(view);
        postProcessScripts(view);
    }

    private void postProcessStyles(UIViewRoot view) {
        _stylesheetPostProcessor.edit(view, FacesContext.getCurrentInstance());
    }

    private void postProcessScripts(UIViewRoot view) {
        _javascriptPostProcessor.edit(view, FacesContext.getCurrentInstance());
    }

}
