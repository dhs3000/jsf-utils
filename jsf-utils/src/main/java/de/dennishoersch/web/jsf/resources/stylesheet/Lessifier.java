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
package de.dennishoersch.web.jsf.resources.stylesheet;

import java.net.URL;

import org.lesscss.LessCompiler;
import org.lesscss.LessException;

/**
 * @author hoersch
 *
 */
public final class Lessifier {
    // There are ThreadLocals involved so sharing between threads is not possible
    private static final ThreadLocal<LessCompiler> _lessCompiler = new ThreadLocal<LessCompiler>() {
        @Override
        protected LessCompiler initialValue() {
            return create();
        }
    };

    private Lessifier() {
    }

    protected static LessCompiler create() {
        LessCompiler compiler = new LessCompiler();
        compiler.setCompress(true);

        URL lessJs = Lessifier.class.getClassLoader().getResource("less-1.3.3.js");
        compiler.setLessJs(lessJs);

        return compiler;
    }

    public static String lessify(String less) {
        try {
            String css = _lessCompiler.get().compile(less);
            return clean(css);
        } catch (LessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String clean(String css) {
        return css.replace("\n", "");
    }

}
