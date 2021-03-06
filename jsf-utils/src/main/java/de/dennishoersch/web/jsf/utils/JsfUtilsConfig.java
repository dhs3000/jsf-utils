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

import javax.faces.context.FacesContext;

import org.apache.myfaces.shared.util.WebConfigParamUtils;

/**
 * @author hoersch
 */
public final class JsfUtilsConfig {

    public static final String CACHE_GENERATED_RESOURCES_DISABLED_PARAM = "de.dennishoersch.web.jsf.resources.CACHE_GENERATED_RESOURCES_DISABLED";
    private static Boolean _CACHE_GENERATED_RESOURCES_DISABLED;

    private JsfUtilsConfig() {
    }

    /**
     *
     * @param context
     * @return whether the production mode is activated
     */
    public static boolean isCacheGeneratedResourcesDisabled(FacesContext context) {
        if (_CACHE_GENERATED_RESOURCES_DISABLED == null) {
            _CACHE_GENERATED_RESOURCES_DISABLED = getBooleanInitParameter(context, CACHE_GENERATED_RESOURCES_DISABLED_PARAM, false);
        }
        return _CACHE_GENERATED_RESOURCES_DISABLED.booleanValue();
    }

    private static Boolean getBooleanInitParameter(FacesContext context, String paramName, boolean defaulValue) {
        return Boolean.valueOf(WebConfigParamUtils.getBooleanInitParameter(context.getExternalContext(), paramName, defaulValue));
    }
}
