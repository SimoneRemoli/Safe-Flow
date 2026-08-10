package it.web.safeflow.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Collections;

public class UserProfileStats {

    private final int reportCount;
    private final int cityCount;
    private final int approvalCount;
    private final int pendingCount;
    private final int rejectedCount;
    private final int helpfulScore;
    private final int communityScore;
    private final int communityRank;
    private final Map<String, Integer> categoryCounts;
    private final Map<String, Integer> cityCounts;
    private final Map<String, Integer> stationCounts;
    private final Map<String, Integer> monthlyReportCounts;

    public UserProfileStats(int reportCount, int cityCount, int approvalCount) {
        this(
                reportCount,
                cityCount,
                approvalCount,
                0,
                0,
                0,
                approvalCount * 10,
                0,
                Map.of(),
                Map.of(),
                Map.of(),
                Map.of()
        );
    }

    public UserProfileStats(int reportCount,
                            int cityCount,
                            int approvalCount,
                            int pendingCount,
                            int rejectedCount,
                            int helpfulScore,
                            int communityScore,
                            int communityRank,
                            Map<String, Integer> categoryCounts,
                            Map<String, Integer> cityCounts,
                            Map<String, Integer> stationCounts,
                            Map<String, Integer> monthlyReportCounts) {
        this.reportCount = Math.max(0, reportCount);
        this.cityCount = Math.max(0, cityCount);
        this.approvalCount = Math.max(0, approvalCount);
        this.pendingCount = Math.max(0, pendingCount);
        this.rejectedCount = Math.max(0, rejectedCount);
        this.helpfulScore = Math.max(0, helpfulScore);
        this.communityScore = Math.max(0, communityScore);
        this.communityRank = Math.max(0, communityRank);
        this.categoryCounts = copy(categoryCounts);
        this.cityCounts = copy(cityCounts);
        this.stationCounts = copy(stationCounts);
        this.monthlyReportCounts = copy(monthlyReportCounts);
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

    public int getPendingCount() {
        return pendingCount;
    }

    public int getRejectedCount() {
        return rejectedCount;
    }

    public int getHelpfulScore() {
        return helpfulScore;
    }

    public int getCommunityScore() {
        return communityScore;
    }

    public int getCommunityRank() {
        return communityRank;
    }

    public int getApprovalRatePercent() {
        if (reportCount == 0) {
            return 0;
        }
        return Math.round((approvalCount * 100.0f) / reportCount);
    }

    public String getTrustLevel() {
        if (approvalCount >= 20 && helpfulScore >= 50) {
            return "Trusted Reporter";
        }
        if (approvalCount >= 10 || helpfulScore >= 25) {
            return "Safety Contributor";
        }
        if (reportCount >= 3) {
            return "Active Reporter";
        }
        return "New Reporter";
    }

    public Map<String, Integer> getCategoryCounts() {
        return categoryCounts;
    }

    public Map<String, Integer> getCityCounts() {
        return cityCounts;
    }

    public Map<String, Integer> getStationCounts() {
        return stationCounts;
    }

    public Map<String, Integer> getMonthlyReportCounts() {
        return monthlyReportCounts;
    }

    private static Map<String, Integer> copy(Map<String, Integer> source) {
        if (source == null || source.isEmpty()) {
            return Map.of();
        }
        return Collections.unmodifiableMap(new LinkedHashMap<>(source));
    }
}
