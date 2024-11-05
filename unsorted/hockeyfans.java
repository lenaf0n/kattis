package unsorted;
//returned time limit exception
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.io.IOException;

public class hockeyfans {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String input = br.readLine();
        String[] inputs = input.split(" ");

        int n = Integer.parseInt(inputs[0]);
        int s = Integer.parseInt(inputs[1]);
        int m = Integer.parseInt(inputs[2]);

        int[] chants = new int[n];

        input = br.readLine();

        br.close();

        inputs = input.split(" ");
        Set<Integer> values = new TreeSet<>();

        for (int i = 0; i < n; i++) {
            chants[i] = Integer.parseInt(inputs[i]);
            values.add(chants[i]);
        }
        List<Integer> valuesList = new ArrayList<>(values);
        int a = 0;
        int b = valuesList.size();

        while (true) {
            int midIndex = (a+b)/2;
            int count = 0;

            for (int i = 0; i <= n - s;) {
                if (isValid(chants, i, s, valuesList.get(midIndex))) {
                    count++;
                    i += s;
                } else {
                    i++; 
                }
                
                if (count == m) {
                    break;
                }
            }

            if (count >= m) {
                a = midIndex;
            } else {
                b = midIndex;
            }

            if (Math.abs(b-a) <= 1) {
                break;
            }
        }

        System.out.println(valuesList.get(a));
    }

    private static boolean isValid(int[] chants, int i, int s, int temp) {
        for (int j = i; j < i+s; j++) {
            if (chants[j] < temp) {
                return false;
            }
        }
        return true;
    }
}
