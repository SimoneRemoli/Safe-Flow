package it.web.safeflow.model;

public class ReportImageAttachment {

    private final String fileName;
    private final String contentType;

    public ReportImageAttachment(String fileName, String contentType) {
        this.fileName = fileName;
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }
}
