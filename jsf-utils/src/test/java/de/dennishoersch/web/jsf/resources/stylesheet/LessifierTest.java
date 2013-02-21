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

import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.lesscss.LessException;

/**
 * @author hoersch
 *
 */
public class LessifierTest {

    /**
     * There are no interferences between compiling.
     */
    @Test
    public void testLessify() {
        String first = "@myColor:#000; .someClass {color:@myColor;}";
        String second = ".someOtherClass {color:@myColor;}";

        String result1 = Lessifier.lessify(first);
        assertThat(result1, containsString("#000"));
        assertThat(result1, not(containsString("@myColor")));

        try {
            Lessifier.lessify(second);
            fail("The less style '" + second + "' can't be compiled!");
        } catch (Exception e) {
            assertThat(e.getCause(), instanceOf(LessException.class));
            assertThat(e.getCause().getMessage(), containsString("@myColor"));
        }
    }
}
