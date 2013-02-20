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

import java.io.File;

import com.google.common.base.Function;

/**
 * Metadata of a generated resource.
 *
 * @author hoersch
 */
public class GeneratedResourceMetadata {
    /** the library name */
    public final String libraryName;

    /** the name of the resource*/
    public final String resourceName;

    /**
     *
     * @param libraryName
     * @param resourceName
     */
    public GeneratedResourceMetadata(String libraryName, String resourceName) {
        this.libraryName = libraryName;
        this.resourceName = resourceName;
    }

    @Override
    public String toString() {
        return libraryName + ":" + resourceName;
    }

    /**
     * A function to get the metadata of a file.
     * @param resourcesBasePath
     * @return function
     */
    public static Function<File, GeneratedResourceMetadata> fromFile(final String resourcesBasePath) {
        return new Function<File, GeneratedResourceMetadata>() {
            @Override
            public GeneratedResourceMetadata apply(File input) {
                String resourceName = input.getName();

                String libraryName = input.getAbsolutePath().substring(resourcesBasePath.length());
                libraryName = libraryName.substring(0, libraryName.length() - resourceName.length() - 1);

                return new GeneratedResourceMetadata(libraryName, resourceName);
            }
        };
    }
}
