/*
 * Copyright (c) 2012 HIS GmbH All Rights Reserved.
 *
 * $Id: LogExceptionQeuedEventListener.java,v 1.1 2012-10-16 14:44:18 hoersch#his.de Exp $
 *
 * Created on 08.03.2012 by hoersch
 */
package de.dennishoersch.web.jsf;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import de.dennishoersch.web.jsf.resources.javascript.JavascriptBuilder;

/**
 * Listener that logs exceptions that are queued during JSF lifecycle.
 *
 * @author hoersch
 */
public class LogExceptionQeuedEventListener implements SystemEventListener {

    private static final Logger logger = Logger.getLogger(JavascriptBuilder.class.getName());

    @Override
    public boolean isListenerForSource(Object source) {
        return ExceptionQueuedEventContext.class.isInstance(source);
    }

    @Override
    public void processEvent(SystemEvent event) {
        if (!(event instanceof ExceptionQueuedEvent)) {
            return;
        }
        ExceptionQueuedEvent exceptionQueuedEvent = (ExceptionQueuedEvent) event;
        ExceptionQueuedEventContext context = exceptionQueuedEvent.getContext();

        logger.log(Level.SEVERE, "Exception in Phase-ID: " + context.getPhaseId(), context.getException());
    }
}
