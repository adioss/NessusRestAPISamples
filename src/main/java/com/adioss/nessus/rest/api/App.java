/**
 * Copyright 2015 Adrien PAILHES
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adioss.nessus.rest.api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class App {
    public static final String NESSUS_URL = "http://url:port";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String ACCESS_KEY = "ACCESS_KEY";
    public static final String SECRET_KEY = "SECRET_KEY";

    public static void main(String... args) throws IOException {
        getSession();
        listScans();
    }

    /**
     * Example request POST with parameters (passed in the body)
     * POST https://url:port/session HTTP/1.1
     * Accept-Encoding: gzip,deflate
     * Content-Type: application/x-www-form-urlencoded
     * Content-Length: XXX
     * Host: url:port
     * Connection: Keep-Alive
     * User-Agent: Apache-HttpClient/4.1.1 (java 1.5)
     *
     * @throws IOException if URL can not be resolved
     */
    private static void getSession() throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(NESSUS_URL + "/session");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            // write body to query
            String body = "username=" + USERNAME + "&password=" + PASSWORD;
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(body);
            writer.flush();
            writer.close();
            os.close();
            // connect
            connection.connect();

            // result
            System.out.println(connection.getResponseCode());
            String result = getResult(connection);
            System.out.println(result);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Example request GET with parameters (passed as headers)
     * GET https://url:port/scans HTTP/1.1
     * Accept-Encoding: gzip,deflate
     * X-ApiKeys: accessKey=titi;secretKey=toto
     * Host: url:port
     * Connection: Keep-Alive
     * User-Agent: Apache-HttpClient/4.1.1 (java 1.5)
     *
     * @throws IOException if URL can not be resolved
     */
    private static void listScans() throws IOException {
        HttpURLConnection connection = null;
        String apiKeys = "accessKey=" + ACCESS_KEY + ";" + "secretKey=" + SECRET_KEY;
        try {
            URL url = new URL(NESSUS_URL + "/scans");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.addRequestProperty("X-ApiKeys", apiKeys);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // connect
            connection.connect();

            // result
            System.out.println(connection.getResponseCode());
            String result = getResult(connection);
            System.out.println(result);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String getResult(HttpURLConnection connection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder results = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            results.append(line);
        }
        return results.toString();
    }

    private App() {
    }
}