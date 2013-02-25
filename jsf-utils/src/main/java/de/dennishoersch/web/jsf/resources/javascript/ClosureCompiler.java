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

import java.util.Arrays;
import java.util.logging.Logger;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JqueryCodingConvention;
import com.google.javascript.jscomp.Result;
import com.google.javascript.jscomp.SourceFile;

/**
 * Compiler to compile javascript code with the help of the Google Closure compiler using the Level {@link CompilationLevel#SIMPLE_OPTIMIZATIONS}.
 * @author hoersch
 */
public final class ClosureCompiler {
    private static final Logger logger = Logger.getLogger(ClosureCompiler.class.getName());
    private ClosureCompiler(){
    }

    public static String compile(String script) {
        String result = script;

        CompilerOptions options = createoptions();

        Compiler compiler = new Compiler();
        Result compile = compiler.compile(Arrays.<SourceFile>asList(), Arrays.asList(SourceFile.fromCode("generated.js", result)), options);

        if (compile.success) {
            result = compiler.toSource();
        } else {
            logger.severe("Can't compile script!");
        }
        return result;
    }

    private static CompilerOptions createoptions() {
        CompilerOptions options = new CompilerOptions();
        options.setCodingConvention(new JqueryCodingConvention());
        CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);
        return options;
    }
}
