import java.io.*;
import java.util.*;

public class WeatherMR {

    public static void main(String[] args) throws Exception {

        System.out.println("=== Simulating MapReduce for Weather Data ===\n");

        // -------------------------------
        // Step 1: Mapper Phase
        // -------------------------------
        BufferedReader br = new BufferedReader(new FileReader("data.txt"));

        // Map<Year, List of temperatures>
        Map<String, List<Integer>> map = new HashMap<>();

        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");

            String year = parts[0];
            int temp = Integer.parseInt(parts[1]);

            // group data by year
            map.putIfAbsent(year, new ArrayList<>());
            map.get(year).add(temp);
        }

        br.close();

        // Show Mapper Output
        System.out.println("Mapper Output (Year -> Temperatures):");
        for (String year : map.keySet()) {
            System.out.println(year + " -> " + map.get(year));
        }

        // -------------------------------
        // Step 2: Reducer Phase
        // -------------------------------
        Map<String, Double> avgTemp = new HashMap<>();

        for (String year : map.keySet()) {
            List<Integer> temps = map.get(year);

            int sum = 0;
            for (int t : temps) {
                sum += t;
            }

            double avg = (double) sum / temps.size();
            avgTemp.put(year, avg);
        }

        // Show Reducer Output
        System.out.println("\nReducer Output (Year -> Avg Temperature):");
        for (String year : avgTemp.keySet()) {
            System.out.println(year + " -> " + avgTemp.get(year));
        }

        // -------------------------------
        // Step 3: Find Hottest & Coolest Year
        // -------------------------------
        String hottestYear = "";
        String coolestYear = "";

        double maxTemp = Double.MIN_VALUE;
        double minTemp = Double.MAX_VALUE;

        for (String year : avgTemp.keySet()) {
            double temp = avgTemp.get(year);

            if (temp > maxTemp) {
                maxTemp = temp;
                hottestYear = year;
            }

            if (temp < minTemp) {
                minTemp = temp;
                coolestYear = year;
            }
        }

        // -------------------------------
        // Final Output
        // -------------------------------
        System.out.println("\n=== Final Result ===");
        System.out.println("Hottest Year: " + hottestYear + " (Avg Temp: " + maxTemp + ")");
        System.out.println("Coolest Year: " + coolestYear + " (Avg Temp: " + minTemp + ")");
    }
}