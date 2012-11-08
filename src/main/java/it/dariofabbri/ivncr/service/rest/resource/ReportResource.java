package it.dariofabbri.ivncr.service.rest.resource;


import it.dariofabbri.ivncr.service.local.report.BasicReportService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/reports")
@Produces("application/pdf")
public class ReportResource {

	private static final Logger logger = LoggerFactory.getLogger(ReportResource.class);
	
	@GET
	@Path("/basictest")
	public Response getBasicTestReport() {

		logger.debug("getBasicTestReport called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("reports:basictest")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}

		BasicReportService brs = new BasicReportService();
		byte[] report = brs.generateReport("reports/basictest.jasper");
		
		return Response.ok().entity(report).build();
	}
}
