import java.util.*;

public class Parking {
    static int ROWS = 21;
    static int COLUMNS = 30;

    static String[][] parkingGuide =
        {
            {"--", "pn", "pn", "pn", "pn", "pn", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "--"},
            {"ss", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "pa"},
            {"pn", "rv", "pn", "pn", "pn", "pn", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "rv", "pa"},
            {"pn", "rv", "pn", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "rv", "pa"},
            {"pn", "rv", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rv", "pa"},
            {"pa", "rv", "pn", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "rv", "pa"},
            {"pa", "rv", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "rv", "pa"},
            {"pa", "rv", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rv", "pa"},
            {"pa", "rv", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "rv", "pa"},
            {"pa", "rv", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "rv", "pa"},
            {"pa", "rv", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rv", "pa"},
            {"pa", "rv", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "rv", "pa"},
            {"pa", "rv", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "rv", "pa"},
            {"pa", "rv", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rv", "pa"},
            {"pa", "rv", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "rv", "pa"},
            {"pa", "rv", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "rv", "pa"},
            {"pa", "rv", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rv", "pa"},
            {"pa", "rv", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "rv", "pa"},
            {"pa", "rv", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "rv", "pa"},
            {"pa", "rv", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "ee"},
            {"--", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "--"}
        };
    static boolean[][] pathVisited = new boolean[ROWS][COLUMNS];

    static int[] xVal = new int[]{ 0, 1, 0, -1 };
    static int[] yVal = new int[]{ 1, 0, -1, 0 };
    static char[] dir = new char[]{ 'R', 'D', 'L', 'U' };

    public static void Parking() {

    }

    public static void main(String[] args) {
        System.out.println("New Parking created.");
        System.out.println(getNearestSpot());
        System.out.println(getNearestSpot());
    }

    public static String getNearestSpot() {
        int row = 1, column = 1;
        int[] pxVal = new int[2], pyVal = new int[2];
        char[] pDir = new char[2];

        Queue<Cell> q = new LinkedList<>();
        q.add(new Cell(row, column, ""));
        pathVisited[row][column] = true;
        boolean flag = true;

        while (flag && !q.isEmpty()) {
            Cell cell = q.peek();
            int x = cell.first;
            int y = cell.second;
            String path = cell.path;
            q.remove();
            if (parkingGuide[x][y] == "rh") {
                pxVal = new int[]{-1, 1};
                pyVal = new int[]{0, 0};
                pDir = new char[]{'U', 'D'};
            } else if (parkingGuide[x][y] == "rv") {
                pxVal = new int[]{0, 0};
                pyVal = new int[]{-1, 1};
                pDir = new char[]{'L', 'R'};
            }

            for(int i=0; i<pxVal.length; i++) {
                int adjx = x + pxVal[i];
                int adjy = y + pyVal[i];
                if (parkingGuide[adjx][adjy] == "pa") {
                    flag = false;
                    System.out.println(adjx + ", " + adjy);

                    parkingGuide[adjx][adjy] = "pn";
                    pathVisited = new boolean[ROWS][COLUMNS];
                    return path + pDir[i];
                }
            }

            for(int i=0; i<xVal.length; i++) {
                int adjx = x + xVal[i];
                int adjy = y + yVal[i];
                if (!pathVisited[adjx][adjy] && parkingGuide[adjx][adjy].charAt(0) == 'r') {
                    q.add(new Cell(adjx, adjy, path + dir[i]));
                    pathVisited[adjx][adjy] = true;
                }
            }
        }
        pathVisited = new boolean[ROWS][COLUMNS];
        return "No parking available";
    }

}

class Cell {
    int first, second;
    String path = "";
    public Cell (int first, int second, String path) {
        this.first = first;
        this.second = second;
        this.path = path;
    }
}