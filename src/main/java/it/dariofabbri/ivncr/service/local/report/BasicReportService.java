package it.dariofabbri.ivncr.service.local.report;

import java.net.URL;
import java.util.HashMap;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

public class BasicReportService {

	public byte[] generateReport(String report) {

		byte[] result = null;
		
		URL url = this.getClass().getClassLoader().getResource(report);
		try {
			JasperReport jr = (JasperReport) JRLoader.loadObject(url);
			JasperPrint jp = JasperFillManager.fillReport(jr, new HashMap<String, Object>(), new JREmptyDataSource());
			result = JasperExportManager.exportReportToPdf(jp);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
