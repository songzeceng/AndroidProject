package com.example.songzeceng.studyofretrofit;

/**
 * Created by songzeceng on 2018/2/4.
 */

public class Result {
    private int status;

    private Content content;

    private class Content{
        private String from;
        private String to;
        private String vendor;
        private String out;
        private int errNo;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            sb.append("\"from\":\"")
                    .append(from).append('\"');
            sb.append(",\"to\":\"")
                    .append(to).append('\"');
            sb.append(",\"vendor\":\"")
                    .append(vendor).append('\"');
            sb.append(",\"out\":\"")
                    .append(out).append('\"');
            sb.append(",\"errNo\":")
                    .append(errNo);
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"status\":")
                .append(status);
        sb.append(",\"content\":")
                .append(content.toString());
        sb.append('}');
        return sb.toString();
    }
}
