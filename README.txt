AngularJS OSGi Management Client

On this branch the AngularJS Client itself is modularized
and implemented in OSGi bundles. It's not needed to start 
another server for the client resources. 

The bundle nh.angularjsosgi.launcher provides a launcher that
starts a OSGi framework
with two servlets registered at the HttpService: one "ApiServlet"
for the REST Api and a servlet for the Client resources.

The bundle nh.angularjs.osgi.client implements the Servlet that
delivers the client resources. (Mapped to /).
In addition it contains web resources for the "platform framework"
and contributes to perspectives (Bundles and Services).
The bundle nh.angularjs.osgi.client.dscomponents does not contain
any Java source but only contributes JS- and HTML-Resources for
the DS Components perspective.


Nils Hartmann <nils@nilshartmann.net>
