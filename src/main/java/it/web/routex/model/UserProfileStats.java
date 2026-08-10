package it.web.routex.model;

public class UserProfileStats {

    private final int reportCount;
    private final int cityCount;
    private final int approvalCount;

    public UserProfileStats(int reportCount, int cityCount, int approvalCount) {
        this.reportCount = reportCount;
        this.cityCount = cityCount;
        this.approvalCount = approvalCount;
    }

    public int getReportCount() {
        return reportCount;
    }

    public int getCityCount() {
        return cityCount;
    }

    public int getApprovalCount() {
        return approvalCount;
    }
}
