/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.view.facelets;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageFactory;

import org.apache.myfaces.view.ViewDeclarationLanguageStrategy;

public class MockViewDeclarationLanguageFactory extends ViewDeclarationLanguageFactory
{

    private ViewDeclarationLanguageStrategy _strategy;

    public MockViewDeclarationLanguageFactory()
    {
        super();
        _strategy = new MockViewDeclarationLanguageStrategy();
    }

    @Override
    public ViewDeclarationLanguage getViewDeclarationLanguage(String viewId)
    {
        return _strategy.getViewDeclarationLanguage();
    }

    public static class MockViewDeclarationLanguageStrategy
        implements ViewDeclarationLanguageStrategy
    {
        private ViewDeclarationLanguage _language;

        @Override
        public boolean handles(String viewId)
        {
            return true;
        }

        @Override
        public ViewDeclarationLanguage getViewDeclarationLanguage()
        {
            if (_language == null)
            {
                _language = new MockFaceletViewDeclarationLanguage(FacesContext.getCurrentInstance());
            }
            return _language;
        }
    }
}
