import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.util.*;

public class JavaHTTPServer implements Runnable{

    static final File WEB_ROOT = new File(".");
    static final String DEFAULT_FILE = "index.html";
    static final String FILE_NOT_FOUND = "404.html";
    static final String METHOD_NOT_SUPPORTED = "not_supported.html";

    static final String hostname = "192.168.0.203";
    static final int PORT = 8080;

    static final boolean verbose = true;

    static final List<QRRequest> requests = new ArrayList<QRRequest>();
    static Boolean flag = false;
    static int hourlyRate = 4;

    static Parking parking = new Parking();

    // Client Connection via Socket Class\
    private Socket connect;

    public JavaHTTPServer(Socket c) {
        connect = c;
    }

    public static void main(String[] args) {
        try {
            InetAddress addr = InetAddress.getByName(hostname);
            ServerSocket serverConnect = new ServerSocket(PORT, 50, addr);
            System.out.println("Server started.\nListening for connections on IP : " + hostname + ":" + PORT + "\n");

            // we listen until user halts server execution
            while (true) {
                JavaHTTPServer myServer = new JavaHTTPServer(serverConnect.accept());

                if (verbose) {
                    System.out.println("Connecton opened. (" + new Date() + ")");
                }

                // create dedicated thread to manage the client connection
                Thread thread = new Thread(myServer);
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }
    }

    public static void setTimeoutByParva(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }

    @Override
    public void run() {
        // we manage our particular client connection
        BufferedReader in = null; PrintWriter out = null; BufferedOutputStream dataOut = null;
        String fileRequested = null;
        String params = null;

        try {
            // we read characters from the client via input stream on the socket
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            // we get character output stream to client (for headers)
            out = new PrintWriter(connect.getOutputStream());
            // get binary output stream to client (for requested data)
            dataOut = new BufferedOutputStream(connect.getOutputStream());

            // get first line of the request from the client
            String input = in.readLine();
            // we parse the request with a string tokenizer
            System.out.println(input);
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
            // we get file requested
            params = parse.nextToken().toLowerCase();

            // POST request from the Scanner
            if (method.equals("POST")) {

                if (params.equals("/") || params.equals("/?")) {

                    System.out.println("No user specified. Nothing can be done.");

                } else if (params.contains("/entry?id=")) { // Pass url/enter?id=7I1DZpCvHdLfNIa2fL91&validity=valid

                    String ID = params.replace("/entry?id=", "");
//                    String[] details = params.replace("/enter?", "").split("&");
//                    String ID = details[0].split("=")[1];
//                    String validity = details[1].split("=")[1];
                    String validity = "valid";
                    String type = "entry";
                    requests.add(new QRRequest(ID, validity, type, ""));

                }  else if (params.contains("/exit?id=")) { // Pass url/exit?id=7I1DZpCvHdLfNIa2fL91

                    String[] details = params.replace("/exit?", "").split("&");
                    String ID = details[0].split("=")[1];
                    String entryTime = details[1].split("=")[1];
                    String type = "exit";
                    requests.add(new QRRequest(ID, "valid", type, entryTime));

                }
                for (QRRequest r : requests) {
                    System.out.println(r.ID + ", " + r.validity + ", " + r.type + ", " + r.entryTime);
                }

            } else if (method.equals("GET")) {

                if (params.equals("/") || params.equals("/?")) {
                    System.out.println("No user specified. Nothing can be done.");
                }
                else if (params.contains("/check?id=")) {
                    String id = params.replace("/check?id=", "");

                    flag = true;
                    new Thread(() -> {
                        try {
                            Thread.sleep(15000);
                            flag = false;
                        }
                        catch (Exception e){
                            System.err.println(e);
                        }
                    }).start();

//                    String validationOutput = "userCantEnter.txt";

                    System.out.println("Entering while loop");
                    QRRequest foundRequest = new QRRequest("", "", "", "");
                    String response = "You can't go.";
                    while(flag) {
                        // Check for the user's in-time being entered in the database.

                        String output = "invalid";
                        // Check for the last 10 records in the requests. If the same id exists with a valid flag, let them enter.
                        Integer checkCount = 10;
                        Integer lengthOfRequests = requests.size();
                        for (int i=lengthOfRequests-1; (i>lengthOfRequests - checkCount) && (i>=0); i--) {
                            QRRequest request = requests.get(i);
                            if (id.equals(request.ID) && request.validity.equals("valid")) {
                                output = "valid";
                                foundRequest = request;
                                requests.remove(i);
                                break;
                            }
                        }
                        if (output == "valid") {
                            if (foundRequest.type.equals("entry")) {
                                response = "Enter-" + parking.getNearestSpot();
                            } else if (foundRequest.type.equals("exit")) {
                                response = "Exit-";
                                long currentTime = System.currentTimeMillis();
                                long entryTime = Long.parseLong(foundRequest.entryTime);
//                                System.out.println(((currentTime - entryTime)*hourlyRate)/3600000.00);
                                double fare = Math.max(4, Math.round(((currentTime - entryTime)*hourlyRate) / 36000.0) / 100.0);
                                response = response + String.valueOf(fare);
                            }
//                            validationOutput = "userEnter.txt";
//                            System.out.println(parking.getNearestSpot());
                            flag = false;
                        }
//                        else {
//                            validationOutput = "userCantEnter.txt";
//                        }
                    }
                    System.out.println("Exiting while loop");
//                    File file = new File(WEB_ROOT, validationOutput);
//                    int fileLength = (int) file.length();
//                    String content = getContentType(validationOutput);
//                    byte[] fileData = readFileData(file, fileLength);

                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Java HTTP Server from Parva : 1.0");
                    out.println("Date: " + new Date());
                    out.println("Content-type: text/plain");
                    out.println("Content-length: " + response.length());
                    out.println();
                    out.flush();

//                    dataOut.write(fileData, 0, fileLength);
                    dataOut.write(response.getBytes());
                    dataOut.flush();
                }
//                else if (params.contains("/checkexit?id=")) {
//
//                    String id = params.replace("/checkexit?id=", "");
//
//                    flag = true;
//                    new Thread(() -> {
//                        try {
//                            Thread.sleep(1000);
//                            flag = false;
//                        }
//                        catch (Exception e){
//                            System.err.println(e);
//                        }
//                    }).start();
//
//                    String validationOutput = "userCantExit.txt";
//
//                    while(flag) {
//                        // Check for the user's in-time being entered in the database.
//
//                        String output = "invalid";
//                        // Check for the last 10 records in the requests. If the same id exists with a valid flag, let them enter.
//                        Integer checkCount = 10;
//                        Integer lengthOfRequests = requests.size();
//                        for (int i=lengthOfRequests-1; (i>lengthOfRequests - checkCount) && (i>=0); i--) {
//                            QRRequest request = requests.get(i);
//                            if (id.equals(request.ID) && request.type.equals("exit") && request.validity.equals("valid")) {
//                                output = "valid";
//                            }
//                        }
//                        if (output == "valid") {
//                            validationOutput = "userExit.txt";
//                            flag = false;
//                        } else {
//                            validationOutput = "userCantExit.txt";
//                        }
//                    }
//
//                    File file = new File(WEB_ROOT, validationOutput);
//                    int fileLength = (int) file.length();
//                    String content = getContentType(validationOutput);
//                    byte[] fileData = readFileData(file, fileLength);
//
//                    out.println("HTTP/1.1 200 OK");
//                    out.println("Server: Java HTTP Server from Parva : 1.0");
//                    out.println("Date: " + new Date());
//                    out.println("Content-type: " + content);
//                    out.println("Content-length: " + fileLength);
//                    out.println();
//                    out.flush();
//
//                    dataOut.write(fileData, 0, fileLength);
//                    dataOut.flush();
//                }
                else if (params.contains("/getparking")) {
                    var parkingString = "";
                    for (int i=0; i<parking.parkingGuide.length; i++) {
                        parkingString = parkingString + String.join("-", parking.parkingGuide[i]) + "+";
                    }

                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Java HTTP Server from Parva : 1.0");
                    out.println("Date: " + new Date());
                    out.println("Content-type: text/plain");
                    out.println("Content-length: " + parkingString.length());
                    out.println();
                    out.flush();

                    dataOut.write(parkingString.getBytes());
                    dataOut.flush();
                }
                else if (params.contains("/setparking?")) {
                    String[] values = params.replace("/setparking?", "").split("&");

                    String[] coordinates = values[0].replace("coordinates=", "").split("-");
                    int x = Integer.parseInt(coordinates[0]);
                    int y = Integer.parseInt(coordinates[1]);

                    String parkingStatus = values[1].replace("status=", "");

                    parking.parkingGuide[x][y] = parkingStatus;

                    String response = "Parking updated.";
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Java HTTP Server from Parva : 1.0");
                    out.println("Date: " + new Date());
                    out.println("Content-type: text/plain");
                    out.println("Content-length: " + response.length());
                    out.println();
                    out.flush();

                    dataOut.write(response.getBytes());
                    dataOut.flush();
                }
            }
        } catch (FileNotFoundException fnfe) {
            try {
                fileNotFound(out, dataOut, fileRequested);
            } catch (IOException ioe) {
                System.err.println("Error with file not found exception : " + ioe.getMessage());
            }

        } catch (IOException ioe) {
            System.err.println("Server error : " + ioe);
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
                connect.close();
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (verbose) {
                System.out.println("Connection closed.\n");
            }
        }


    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }

    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
            return "text/html";
        else
            return "text/plain";
    }

    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
        File file = new File(WEB_ROOT, FILE_NOT_FOUND);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readFileData(file, fileLength);

        out.println("HTTP/1.1 404 File Not Found");
        out.println("Server: Java HTTP Server from SSaurel : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + content);
        out.println("Content-length: " + fileLength);
        out.println();
        out.flush();

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        if (verbose) {
            System.out.println("File " + fileRequested + " not found");
        }
    }

}

class QRRequest {
    String ID = "invalid";
    String validity = "invalid";
    String type = "entry";
    String entryTime = "";

    public QRRequest(String id, String val, String t, String time) {
        ID = id;
        validity = val;
        type = t;
        entryTime = time;
    }

    public static void main(String[] args) {
        System.out.println("New request created.");
    }
}

public class Parking {
    static int ROWS = 21;
    static int COLUMNS = 30;

    static String[][] parkingGuide =
            {
                    {"xx", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "xx"},
                    {"ss", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "pn"},
                    {"pn", "rv", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pa", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pa", "pn", "pn", "pa", "pa", "pa", "rv", "pa"},
                    {"pn", "rv", "pn", "pn", "pa", "pa", "pn", "pn", "pn", "pa", "pa", "pa", "pa", "pn", "pn", "pn", "pn", "pn", "pn", "pa", "pa", "pn", "pn", "pn", "pn", "pn", "pa", "pn", "rv", "pn"},
                    {"pn", "rv", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rv", "pn"},
                    {"pn", "rv", "pn", "pn", "pn", "pn", "pn", "pn", "pa", "pa", "pn", "pn", "pn", "pn", "pn", "pa", "pn", "pn", "pa", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "rv", "pn"},
                    {"pn", "rv", "pn", "pn", "pn", "pa", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pa", "pa", "pn", "pn", "pn", "pn", "rv", "pa"},
                    {"pn", "rv", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rv", "pn"},
                    {"pn", "rv", "pn", "pa", "pn", "pn", "pn", "pa", "pa", "pn", "pn", "pa", "pn", "pa", "pn", "pn", "pn", "pn", "pn", "pa", "pn", "pa", "pn", "pn", "pn", "pn", "pn", "pn", "rv", "pn"},
                    {"pa", "rv", "pn", "pn", "pn", "pa", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pa", "pa", "pa", "pn", "pn", "pn", "pn", "pa", "pn", "pn", "pa", "pn", "pn", "rv", "pn"},
                    {"pn", "rv", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rv", "pn"},
                    {"pa", "rv", "pn", "pn", "pa", "pn", "pn", "pa", "pa", "pn", "pn", "pn", "pn", "pa", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pa", "pn", "pn", "pn", "pn", "pn", "pn", "rv", "pa"},
                    {"pn", "rv", "pn", "pa", "pa", "pn", "pn", "pn", "pn", "pn", "pa", "pn", "pn", "pn", "pn", "pn", "pa", "pa", "pa", "pn", "pn", "pn", "pn", "pn", "pn", "pa", "pn", "pa", "rv", "pn"},
                    {"pn", "rv", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rv", "pn"},
                    {"pn", "rv", "pn", "pa", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "rv", "pn"},
                    {"pa", "rv", "pn", "pn", "pn", "pn", "pa", "pa", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pa", "pn", "pa", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "rv", "pa"},
                    {"pn", "rv", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rv", "pn"},
                    {"pn", "rv", "pn", "pa", "pa", "pn", "pa", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pa", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pa", "pn", "pn", "rv", "pn"},
                    {"pn", "rv", "pn", "pa", "pn", "pn", "pn", "pn", "pn", "pa", "pa", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pa", "pn", "pn", "pn", "pn", "pa", "pn", "pn", "pn", "pn", "rv", "pa"},
                    {"pn", "rv", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "rh", "se"},
                    {"xx", "pn", "pa", "pn", "pn", "pn", "pa", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pa", "pa", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "pn", "xx"}
            };

    static boolean[][] pathVisited = new boolean[ROWS][COLUMNS];

    static int[] xVal = new int[]{ 0, 1, 0, -1 };
    static int[] yVal = new int[]{ 1, 0, -1, 0 };
    static char[] dir = new char[]{ 'E', 'S', 'W', 'N' };

    public static void Parking() {

    }

    public static String getPathDirections(String Dir) {
        String path = "S";
        int l = Dir.length();
        int i = 0;
        int j = 1;

        while(j < l){
            if(Dir.charAt(i) == 'E' && Dir.charAt(j) == 'E'){
                path = path +'S';
            }

            if(Dir.charAt(i) == 'E' && Dir.charAt(j) == 'S'){
                path = path +'R';
            }

            if(Dir.charAt(i) == 'E' && Dir.charAt(j) == 'N'){
                path = path +'L';
            }

            if(Dir.charAt(i) == 'W' && Dir.charAt(j) == 'W'){
                path = path +'S';
            }

            if(Dir.charAt(i) == 'W' && Dir.charAt(j) == 'S'){
                path = path +'L';
            }

            if(Dir.charAt(i) == 'W' && Dir.charAt(j) == 'N'){
                path = path +'R';
            }

            if(Dir.charAt(i) == 'S' && Dir.charAt(j) == 'S'){
                path = path +'S';
            }

            if(Dir.charAt(i) == 'S' && Dir.charAt(j) == 'E'){
                path = path +'L';
            }

            if(Dir.charAt(i) == 'S' && Dir.charAt(j) == 'W'){
                path = path +'R';
            }

            if(Dir.charAt(i) == 'N' && Dir.charAt(j) == 'N'){
                path = path +'S';
            }

            if(Dir.charAt(i) == 'N' && Dir.charAt(j) == 'E'){
                path = path +'R';
            }

            if(Dir.charAt(i) == 'N' && Dir.charAt(j) == 'W'){
                path = path +'L';
            }
            i++;
            j++;
        }

        return path;
    }

    public static void main(String[] args) {
        System.out.println("New Parking created.");
    }

    public static String getNearestSpot() {
        int row = 1, column = 1;
        int[] pxVal = new int[2], pyVal = new int[2];
        char[] pDir = new char[2];

        Queue<Cell> q = new LinkedList<Cell>();
        q.clear();
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
                pDir = new char[]{'N', 'S'};
            } else if (parkingGuide[x][y] == "rv") {
                pxVal = new int[]{0, 0};
                pyVal = new int[]{-1, 1};
                pDir = new char[]{'W', 'E'};
            }

            for(int i=0; i<pxVal.length; i++) {
                int adjx = x + pxVal[i];
                int adjy = y + pyVal[i];
                if (parkingGuide[adjx][adjy].charAt(1) == 'a') {
                    flag = false;
                    pathVisited = new boolean[ROWS][COLUMNS];
                    q.clear();
//                    return path + pDir[i];
//                    System.out.println(path + pDir[i]);
                    var parkingNumber = (char)(65 + adjx) + String.valueOf(adjy + 1);
//                    System.out.println(parkingNumber);
                    return parkingNumber + "-" + getPathDirections("E" + path + pDir[i]);
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
        q.clear();
        return "Parking full.";
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
