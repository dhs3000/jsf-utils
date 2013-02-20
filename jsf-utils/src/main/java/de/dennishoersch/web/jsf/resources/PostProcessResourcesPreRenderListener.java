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

import de.dennishoersch.web.jsf.resources.stylesheet.LessStylesheetGenerateHelper;
/**
 * @author hoersch
 *
 */
public class PostProcessResourcesPreRenderListener  implements SystemEventListener {

    private GenerateResources _stylesheetPostProcessor = new GenerateResources(new LessStylesheetGenerateHelper(), "0.1");

    @Override
    public boolean isListenerForSource(Object source) {
        return UIViewRoot.class.isAssignableFrom(source.getClass());
    }

    @Override
    public void processEvent(SystemEvent event) {
        UIViewRoot view = (UIViewRoot) event.getSource();

        postPocessStyles(view);

    }

    private void postPocessStyles(UIViewRoot view) {
        _stylesheetPostProcessor.edit(view, FacesContext.getCurrentInstance());
    }

}
