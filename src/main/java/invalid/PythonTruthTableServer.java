package invalid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import static invalid.DataPreprocessing.arrToVec;
import static invalid.DataPreprocessing.truthTableParse;

public class PythonTruthTableServer {
    public static String logicParse(String expr) {
        StringBuilder response = new StringBuilder();
        try {
            // Define the server URL with the parameter
            String serverUrl = "http://localhost:8000/tb?param=" + expr;

            // Create a URL object
            URL url = new URL(serverUrl);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Get the response from the server
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Print the server's response
            } else {
                System.err.println("HTTP Request failed with response code: " + responseCode);
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public static String simplifyParse(String expr) {
        StringBuilder response = new StringBuilder();
        try {
            // Define the server URL with the parameter
            String serverUrl = "http://localhost:8000/simplify?param=" + expr;

            // Create a URL object
            URL url = new URL(serverUrl);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Get the response from the server
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Print the server's response
            } else {
                System.err.println("HTTP Request failed with response code: " + responseCode);
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public static Vector<Vector<Vector<String>>> dnfParse(String expr) {
        StringBuilder response = new StringBuilder();
        try {
            // Define the server URL with the parameter
            String serverUrl = "http://localhost:8000/dnf?param=" + expr;

            // Create a URL object
            URL url = new URL(serverUrl);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Get the response from the server
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Print the server's response
            } else {
                System.err.println("HTTP Request failed with response code: " + responseCode);
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Vector<Vector<Vector<String>>> lineTable = new Vector<>();
        if (response.toString().contains("Or") && response.toString().contains("And")) {
            String dnf = response.substring(3, response.length() - 2);
            Vector<String> dnfVec = arrToVec(dnf.split("\\),"));
            dnfVec.forEach(s -> lineTable.add(truthTableParse(logicParse(s.trim().substring(4).replace(", ", "%26")), s.trim().substring(4).replace(", ", "%26"))));
        } else if (response.toString().contains("And")) {
            String and = response.substring(4, response.length() - 1);
            and = and.replace(", ", "%26");
            lineTable.add(truthTableParse(logicParse(and), and));
        } else if (response.toString().contains("Or")) {
            String dnf = response.substring(3, response.length() - 1);
            Vector<String> dnfVec = arrToVec(dnf.split(", "));
            dnfVec.forEach(s -> lineTable.add(truthTableParse(logicParse(s), s)));
        } else {
            lineTable.add(truthTableParse(response.toString(), response.toString()));
        }
        return lineTable;
    }
}

