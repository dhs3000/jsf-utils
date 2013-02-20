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
package de.dennishoersch.web.servlet;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

/**
 * Wrapper of {@link ServletContext}.
 * @author hoersch
 */
public class ServletContextWrapper implements ServletContext {

        private ServletContext _wrapped;

        /**
         *
         * @param wrapped
         */
        protected ServletContextWrapper(ServletContext wrapped) {
            _wrapped = wrapped;
        }

        protected final ServletContext getWrapped() {
            return _wrapped;
        }

        @Override
        public Object getAttribute(String arg0) {
            return getWrapped().getAttribute(arg0);
        }

        @Override
        public Enumeration<String> getAttributeNames() {
            return getWrapped().getAttributeNames();
        }

        @Override
        public ServletContext getContext(String arg0) {
            return getWrapped().getContext(arg0);
        }

        @Override
        public String getContextPath() {
            return getWrapped().getContextPath();
        }

        @Override
        public String getInitParameter(String arg0) {
            return getWrapped().getInitParameter(arg0);
        }

        @Override
        public Enumeration<String> getInitParameterNames() {
            return getWrapped().getInitParameterNames();
        }

        @Override
        public int getMajorVersion() {
            return getWrapped().getMajorVersion();
        }

        @Override
        public String getMimeType(String arg0) {
            return getWrapped().getMimeType(arg0);
        }

        @Override
        public int getMinorVersion() {
            return getWrapped().getMinorVersion();
        }

        @Override
        public RequestDispatcher getNamedDispatcher(String arg0) {
            return getWrapped().getNamedDispatcher(arg0);
        }

        @Override
        public String getRealPath(String arg0) {
            return getWrapped().getRealPath(arg0);
        }

        @Override
        public RequestDispatcher getRequestDispatcher(String arg0) {
            return getWrapped().getRequestDispatcher(arg0);
        }

        @Override
        public URL getResource(String arg0) throws MalformedURLException {
            return getWrapped().getResource(arg0);
        }

        @Override
        public InputStream getResourceAsStream(String arg0) {
            return getWrapped().getResourceAsStream(arg0);
        }

        @Override
        public Set<String> getResourcePaths(String arg0) {
            return getWrapped().getResourcePaths(arg0);
        }

        @Override
        public String getServerInfo() {
            return getWrapped().getServerInfo();
        }

        @Override
        @Deprecated
        public Servlet getServlet(String arg0) throws ServletException {
            return getWrapped().getServlet(arg0);
        }

        @Override
        public String getServletContextName() {
            return getWrapped().getServletContextName();
        }

        @Override
        @Deprecated
        public Enumeration<String> getServletNames() {
            return getWrapped().getServletNames();
        }

        @Override
        @Deprecated
        public Enumeration<Servlet> getServlets() {
            return getWrapped().getServlets();
        }

        @Override
        @Deprecated
        public void log(Exception arg0, String arg1) {
            getWrapped().log(arg0, arg1);
        }

        @Override
        public void log(String arg0, Throwable arg1) {
            getWrapped().log(arg0, arg1);
        }

        @Override
        public void log(String arg0) {
            getWrapped().log(arg0);
        }

        @Override
        public void removeAttribute(String arg0) {
            getWrapped().removeAttribute(arg0);
        }

        @Override
        public void setAttribute(String arg0, Object arg1) {
            getWrapped().setAttribute(arg0, arg1);
        }

        @Override
        public Dynamic addFilter(String arg0, String arg1) {
            return getWrapped().addFilter(arg0, arg1);
        }

        @Override
        public Dynamic addFilter(String arg0, Filter arg1) {
            return getWrapped().addFilter(arg0, arg1);
        }

        @Override
        public Dynamic addFilter(String arg0, Class<? extends Filter> arg1) {
            return getWrapped().addFilter(arg0, arg1);
        }

        @Override
        public void addListener(Class<? extends EventListener> arg0) {
            getWrapped().addListener(arg0);
        }

        @Override
        public void addListener(String arg0) {
            getWrapped().addListener(arg0);
        }

        @Override
        public <T extends EventListener> void addListener(T arg0) {
            getWrapped().addListener(arg0);
        }

        @Override
        public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0, String arg1) {
            return getWrapped().addServlet(arg0, arg1);
        }

        @Override
        public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0, Servlet arg1) {
            return getWrapped().addServlet(arg0, arg1);
        }

        @Override
        public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0, Class<? extends Servlet> arg1) {
            return getWrapped().addServlet(arg0, arg1);
        }

        @Override
        public <T extends Filter> T createFilter(Class<T> arg0) throws ServletException {
            return getWrapped().createFilter(arg0);
        }

        @Override
        public <T extends EventListener> T createListener(Class<T> arg0) throws ServletException {
            return getWrapped().createListener(arg0);
        }

        @Override
        public <T extends Servlet> T createServlet(Class<T> arg0) throws ServletException {
            return getWrapped().createServlet(arg0);
        }

        @Override
        public void declareRoles(String... arg0) {
            getWrapped().declareRoles(arg0);
        }

        @Override
        public ClassLoader getClassLoader() {
            return getWrapped().getClassLoader();
        }

        @Override
        public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
            return getWrapped().getDefaultSessionTrackingModes();
        }

        @Override
        public int getEffectiveMajorVersion() {
            return getWrapped().getEffectiveMajorVersion();
        }

        @Override
        public int getEffectiveMinorVersion() {
            return getWrapped().getEffectiveMinorVersion();
        }

        @Override
        public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
            return getWrapped().getDefaultSessionTrackingModes();
        }

        @Override
        public FilterRegistration getFilterRegistration(String arg0) {
            return getWrapped().getFilterRegistration(arg0);
        }

        @Override
        public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
            return getWrapped().getFilterRegistrations();
        }

        @Override
        public JspConfigDescriptor getJspConfigDescriptor() {
            return getWrapped().getJspConfigDescriptor();
        }

        @Override
        public ServletRegistration getServletRegistration(String arg0) {
            return getWrapped().getServletRegistration(arg0);
        }

        @Override
        public Map<String, ? extends ServletRegistration> getServletRegistrations() {
            return getWrapped().getServletRegistrations();
        }

        @Override
        public SessionCookieConfig getSessionCookieConfig() {
            return getWrapped().getSessionCookieConfig();
        }

        @Override
        public boolean setInitParameter(String arg0, String arg1) {
            return getWrapped().setInitParameter(arg0, arg1);
        }

        @Override
        public void setSessionTrackingModes(Set<SessionTrackingMode> arg0) throws IllegalStateException, IllegalArgumentException {
            getWrapped().setSessionTrackingModes(arg0);
        }
}
