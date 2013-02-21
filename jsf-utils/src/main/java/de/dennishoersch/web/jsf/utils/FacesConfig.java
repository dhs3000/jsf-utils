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
package de.dennishoersch.web.jsf.utils;

import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;

/**
 * @author hoersch
 */
public final class FacesConfig {
    private FacesConfig() {
    }

    /**
     *
     * @param context
     * @return whether the production mode is activated
     */
    public static boolean isProduction(FacesContext context) {
        return context.getApplication().getProjectStage().equals(ProjectStage.Production);
    }

    /**
     *
     * @param context
     * @return whether the production mode is disabled
     */
    public static boolean isDevelopment(FacesContext context) {
        return !isProduction(context);
    }
}
