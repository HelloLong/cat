package com.dianping.cat.report.page.model.problem;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.dianping.cat.consumer.problem.model.entity.ProblemReport;
import com.dianping.cat.consumer.problem.model.transform.DefaultSaxParser;
import com.dianping.cat.hadoop.dal.Report;
import com.dianping.cat.hadoop.dal.ReportDao;
import com.dianping.cat.hadoop.dal.ReportEntity;
import com.dianping.cat.report.page.model.spi.ModelPeriod;
import com.dianping.cat.report.page.model.spi.ModelRequest;
import com.dianping.cat.report.page.model.spi.internal.BaseLocalModelService;
import com.dianping.cat.storage.Bucket;
import com.dianping.cat.storage.BucketManager;
import com.site.lookup.annotation.Inject;

public class LocalProblemService extends BaseLocalModelService<ProblemReport> {
	@Inject
	private BucketManager m_bucketManager;

	@Inject
	private ReportDao m_reportDao;

	public LocalProblemService() {
		super("problem");
	}

	private ProblemReport getLocalReport(long timestamp, String domain) throws Exception {
		Bucket<String> bucket = m_bucketManager.getReportBucket(timestamp, "problem");
		String xml = bucket.findById(domain);

		return xml == null ? null : DefaultSaxParser.parse(xml);
	}

	@Override
	protected ProblemReport getReport(ModelRequest request, ModelPeriod period, String domain) throws Exception {
		ProblemReport report = super.getReport(request, period, domain);

		if (report == null && period.isLast()) {
			long current = System.currentTimeMillis();
			long hour = 60 * 60 * 1000;
			long date = current - current % (hour) - hour;
			report = getLocalReport(date, domain);

			if (report == null) {
				report = new ProblemReport(domain);

				List<Report> historyReports = m_reportDao.findAllByDomainNameDuration(new Date(date), new Date(
				      date + 60 * 60 * 1000), null, "problem", ReportEntity.READSET_DOMAIN_NAME);

				Set<String> domainNames = report.getDomainNames();
				for (Report temp : historyReports) {
					domainNames.add(temp.getDomain());
				}
			}
		}

		return report;
	}
}
