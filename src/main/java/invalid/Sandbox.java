package invalid;
import java.util.Scanner;
import java.util.Vector;

public class Sandbox {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int N = scanner.nextInt();
        Vector<Vector<Integer>> vector = new Vector<>();

        for (int i = 0; i < N; i++) {
            int eventType = scanner.nextInt();
            int value = scanner.nextInt();

            Vector<Integer> valueVec = new Vector<>();
            valueVec.add(eventType);
            valueVec.add(value);
            vector.add(valueVec);
        }

        findResult(vector);

        scanner.close();
    }

    public static void findResult(Vector<Vector<Integer>> vector) {
        int minTH = vector.get(vector.size() - 1).get(1);
        int maxTH = 0;
        int r = 0;
        int l = 0;
        int minVal = 0;
        boolean maxFound = false;
        for (int i = vector.size()-2; i > 0; i--) {
            if (vector.get(i).get(0) == 2 && !maxFound) {
                maxTH = vector.get(i).get(1);
                if(maxTH < minTH) {
                    System.out.println(-1);
                }
                maxFound = true;
                minVal = vector.get(i).get(1);
                r = i;
            }
            if(vector.get(i).get(0) == 2 && maxFound) {
                if (vector.get(i).get(1) < minVal) {
                    minVal = vector.get(i).get(1);
                    l = i;

                }
            }
        }
    }
}
