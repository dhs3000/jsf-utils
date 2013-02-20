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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.shared.renderkit.JSFAttr;
import org.apache.myfaces.shared.renderkit.html.util.ResourceUtils;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * Metadata of a JSF-resource.
 *
 * @author hoersch
 */
public class ResourceMetadata {
    /** the original component resource */
    public final UIComponent componentResource;

    /** the library name */
    public final String libraryName;

    /** the name of the resource*/
    public final String resourceName;

    ResourceMetadata(UIComponent componentResource) {
        this.componentResource = componentResource;
        this.libraryName = (String) componentResource.getAttributes().get(JSFAttr.LIBRARY_ATTR);
        this.resourceName = (String) componentResource.getAttributes().get(JSFAttr.NAME_ATTR);
    }

    @Override
    public String toString() {
        return libraryName + ":" + resourceName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((libraryName == null) ? 0 : libraryName.hashCode());
        result = prime * result + ((resourceName == null) ? 0 : resourceName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ResourceMetadata other = (ResourceMetadata) obj;
        if (libraryName == null) {
            if (other.libraryName != null) {
                return false;
            }
        } else if (!libraryName.equals(other.libraryName)) {
            return false;
        }
        if (resourceName == null) {
            if (other.resourceName != null) {
                return false;
            }
        } else if (!resourceName.equals(other.resourceName)) {
            return false;
        }
        return true;
    }

    /**
     * @return Is a given component resource a stylesheet?
     */
    public static Predicate<UIComponent> isStylesheet() {
        return IsStylesheetPredicate.INSTANCE;
    }

    /**
     * @param component
     * @return Is the given component resource a stylesheet?
     */
    public static boolean isStylesheet(UIComponent component) {
        return isStylesheet().apply(component);
    }

    private enum IsStylesheetPredicate implements Predicate<UIComponent> {
        /** singleton instance */
        INSTANCE;

        @Override
        public boolean apply(UIComponent input) {
            return ResourceUtils.DEFAULT_STYLESHEET_RENDERER_TYPE.equals(input.getRendererType());
        }
    }

    /**
     * @return Is a given component resource a script?
     */
    public static Predicate<UIComponent> isScript() {
        return IsScriptPredicate.INSTANCE;
    }

    /**
     * @param component
     * @return Is the given component resource a script?
     */
    public static boolean isScript(UIComponent component) {
        return isScript().apply(component);
    }

    private enum IsScriptPredicate implements Predicate<UIComponent> {
        /** singleton instance */
        INSTANCE;

        @Override
        public boolean apply(UIComponent input) {
            return ResourceUtils.DEFAULT_SCRIPT_RENDERER_TYPE.equals(input.getRendererType());
        }
    }

    /**
     * @return a function to create the metadata of a given component resource.
     */
    public static Function<UIComponent, ResourceMetadata> toResourceMetadata() {
        return ToResourceMetadataFunction.INSTANCE;
    }

    /**
     * @param component
     * @return create the metadata of a given component resource
     */
    public static ResourceMetadata toResourceMetadata(UIComponent component) {
        return toResourceMetadata().apply(component);
    }

    private enum ToResourceMetadataFunction implements Function<UIComponent, ResourceMetadata> {
        /** singleton instance */
        INSTANCE;

        @Override
        public ResourceMetadata apply(UIComponent input) {
            return executeInComponentContext(input, InternalToResourceFunction.INTERNAL_INSTANCE);
        }

        private enum InternalToResourceFunction implements Function<UIComponent, ResourceMetadata> {
            INTERNAL_INSTANCE;
            @Override
            public ResourceMetadata apply(UIComponent input) {
                return new ResourceMetadata(input);
            }
        }
    }

    static <C extends UIComponent, R> R executeInComponentContext(C component, Function<C, R> function) {
        // We must call pushComponentToEL here because ValueExpression may have
        // implicit objects "component" and "cc" used.
        // The order: first cc than component
        UIComponent cc = UIComponent.getCompositeComponentParent(component);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (cc != null) {
            cc.pushComponentToEL(facesContext, cc);
        }
        try {
            component.pushComponentToEL(facesContext, component);
            try {
                return function.apply(component);
            } finally {
                component.popComponentFromEL(facesContext);
            }
        } finally {
            if (cc != null) {
                cc.popComponentFromEL(facesContext);
            }
        }
    }
}
