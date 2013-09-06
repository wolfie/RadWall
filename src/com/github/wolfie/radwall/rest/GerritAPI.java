package com.github.wolfie.radwall.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.GsonBuilder;

public class GerritAPI {
    private static class Param {
        public static final String IS = "is";
        public static final String IS_OPEN = "open";
        public static final String IS_CLOSED = "closed";

        public static final String REVIEWER = "reviewer";
        public static final String OWNER = "owner";
        public static final String STATUS = "status";

        public static final String STATUS_OPEN = "open";

        private String value;
        private String key;

        private Param(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private static class SubQuery {
        protected List<Param> ands = new ArrayList<Param>();

        private SubQuery(String key, String value) {
            ands.add(new Param(key, value));
        }

        public SubQuery and(String key, String value) {
            ands.add(new Param(key, value));
            return this;
        }

        @Override
        public String toString() {
            String string = "";
            for (Param pair : ands) {
                String v = pair.value;
                String escapedValue = v.contains(" ") ? "\"" + v + "\"" : v;
                string += pair.key + ":" + escapedValue + "+";
            }
            if (!string.isEmpty()) {
                string = string.substring(0, string.length() - 1);
            }
            return string;
        }
    }

    private static class Query {
        protected List<SubQuery> ors = new ArrayList<SubQuery>();

        public Query(String key, String value) {
            ors.add(new SubQuery(key, value));
        }

        public Query or(SubQuery query) {
            ors.add(query);
            return this;
        }

        public Query or(String key, String value) {
            ors.add(new SubQuery(key, value));
            return this;
        }

        @Override
        public String toString() {
            String string = "";
            for (SubQuery subQuery : ors) {
                string = subQuery + "&";
            }
            if (!string.isEmpty()) {
                string = string.substring(0, string.length() - 1);
            }
            return string;
        }
    }

    private static final String BASE_URL = "https://dev.vaadin.com/review/";
    private static final String CHANGES = "changes/";

    private final HttpClient httpClient = new DefaultHttpClient();

    public List<ChangeDAO> getAllChanges() throws ClientProtocolException,
            IOException {
        ChangeDAO[] changeDAOs = new GsonBuilder().create().fromJson(
                query(CHANGES), ChangeDAO[].class);
        return Arrays.asList(changeDAOs);
    }

    private String query(String action) throws ClientProtocolException,
            IOException {
        return query(action, null);
    }

    private String query(String action, Query query)
            throws ClientProtocolException, IOException {
        String uri = BASE_URL + action;

        if (query != null) {
            uri = "?q=" + query;
        }

        HttpGet get = new HttpGet(uri);
        String json = httpClient.execute(get, new BasicResponseHandler());
        return json;
    }
}
