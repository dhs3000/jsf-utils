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

import javax.faces.application.ProjectStage;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.myfaces.config.annotation.DefaultAnnotationProvider;
import org.apache.myfaces.view.facelets.FaceletViewDeclarationLanguage;
import org.apache.myfaces.webapp.StartupServletContextListener;

import de.dennishoersch.web.servlet.ServletContextWrapper;

/**
 * @author hoersch
 */
public class StartupServletContextListenerWrapper extends StartupServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(new ServletContextEvent(new ConfigDependentInitParamServletContext(event.getServletContext())));
    }

    private static class ConfigDependentInitParamServletContext extends ServletContextWrapper {

        protected ConfigDependentInitParamServletContext(ServletContext wrapped) {
            super(wrapped);
        }

        @Override
        public String getInitParameter(String name) {
            // if (Config.isDevelopmentMode()) {
            if (name.equals(/* ApplicationImpl.PROJECT_STAGE_PARAM_NAME */"javax.faces.PROJECT_STAGE")) {
                return ProjectStage.Development.toString();
            }
            if (name.equals(FaceletViewDeclarationLanguage.PARAM_REFRESH_PERIOD)) {
                return "1";
            }
            if (name.equals(/* MyFacesConfig.INIT_PARAM_PRETTY_HTML */"org.apache.myfaces.PRETTY_HTML")) {
                return "true";
            }
            if (name.equals(FaceletViewDeclarationLanguage.PARAM_BUFFER_SIZE)) {
                return "16384";
            }
            // }
            if (name.equals(DefaultAnnotationProvider.SCAN_PACKAGES)) {
                return "de.dennishoersch.web.jsf";
            }
            return super.getInitParameter(name);
        }
    }
}
