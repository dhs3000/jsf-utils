jsf-utils
=========

Provides some utilities for JSF. 

+ Post-processing js / css (less) files registered at the view root.
+ CDI-Extension to mark beans for eager initializing

Post-processing the View Root
-----------------------------
The post-processor modifies all referenced stylesheet and script resources of the view root. The resources are collected into one file per type.

For the stylesheets (referenced by <em>&lt;h:outputStylesheet /></em>) it enables the usage of LESS (instead of pure CSS). All stylesheets are collected into one file and afterwards compiled with the less compiler so that variable declarions etc. defined in different files could be used everywhere.

+ <a href="http://lesscss.org/">LESS CSS</a>

All scripts (referenced by <em>&lt;h:outputScript /></em>) are collected into one single file and compiled by the Google Closure Compiler to remove unnecessary code etc.

+ <a href="https://developers.google.com/closure/compiler/">Google Closure Compiler</a>

The post-prcessor has to be configured on the faces-config.xml as follows:

    <system-event-listener>
        <system-event-listener-class>de.dennishoersch.web.jsf.resources.PostProcessResourcesPreRenderListener</system-event-listener-class>
        <system-event-class>javax.faces.event.PreRenderComponentEvent</system-event-class>
    </system-event-listener>
