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
package de.dennishoersch.web.cdi;

import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Singleton;

import com.google.common.collect.Sets;

/**
 * Simple extension to enable a startup initialization of beans.
 * They must be annotated with @{@link Startup} and be application scoped or singletons.
 *
 * @author hoersch
 */
public class StartupBeanExtension implements Extension {
    private Set<Bean<?>> _startupBeans = Sets.newLinkedHashSet();

    public void processBean(@Observes ProcessBean<?> event) {
        if (event.getAnnotated().isAnnotationPresent(Startup.class) && (event.getAnnotated().isAnnotationPresent(Singleton.class) || event.getAnnotated().isAnnotationPresent(ApplicationScoped.class))) {
            _startupBeans.add(event.getBean());
        }
    }

    public void afterDeploymentValidation(@SuppressWarnings("unused") @Observes AfterDeploymentValidation event, BeanManager manager) {
        if (_startupBeans == null) {
            return;
        }
        for (Bean<?> bean : _startupBeans) {
            // the call to toString() is a cheat to force the bean to be initialized
            manager.getReference(bean, bean.getBeanClass(), manager.createCreationalContext(bean)).toString();
        }
        _startupBeans = null;
    }
}
