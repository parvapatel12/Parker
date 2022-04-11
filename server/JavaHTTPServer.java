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
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.StringTokenizer;

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
            System.out.println("**********************");
            System.out.println(input);
            System.out.println("**********************");
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
            // we get file requested
            params = parse.nextToken().toLowerCase();

            // POST request from the Scanner
            if (method.equals("POST")) {

                if (params.equals("/") || params.equals("/?")) {

                    System.out.println("No user specified. Nothing can be done.");

                } else if (params.contains("/enter?id=")) { // Pass url/enter?id=7I1DZpCvHdLfNIa2fL91&validity=valid

                    String[] details = params.replace("/enter?", "").split("&");
                    String ID = details[0].split("=")[1];
                    String validity = details[1].split("=")[1];
                    String type = "entry";
                    requests.add(new QRRequest(ID, validity, type));

                }  else if (params.contains("/exit?id=")) { // Pass url/exit?id=7I1DZpCvHdLfNIa2fL91

                    String details = params.replace("/exit?", "");
                    String ID = details.split("=")[1];
                    String type = "exit";
                    requests.add(new QRRequest(ID, "valid", type));

                }
                for (QRRequest r : requests) {
                    System.out.println(r.ID + ", " + r.validity + ", " + r.type);
                }

            } else if (method.equals("GET")) {

                if (params.equals("/") || params.equals("/?")) {
                    System.out.println("No user specified. Nothing can be done.");
                }
                else if (params.contains("/checkentry?id=")) {
                    String id = params.replace("/checkentry?id=", "");

                    flag = true;
                    new Thread(() -> {
                        try {
                            Thread.sleep(10000);
                            flag = false;
                        }
                        catch (Exception e){
                            System.err.println(e);
                        }
                    }).start();

                    String validationOutput = "userCantEnter.txt";

                    System.out.println("Entering while loop");
                    while(flag) {
                        // Check for the user's in-time being entered in the database.

                        String output = "invalid";
                        // Check for the last 10 records in the requests. If the same id exists with a valid flag, let them enter.
                        Integer checkCount = 10;
                        Integer lengthOfRequests = requests.size();
                        for (int i=lengthOfRequests-1; (i>lengthOfRequests - checkCount) && (i>=0); i--) {
                            QRRequest request = requests.get(i);
                            if (id.equals(request.ID) && request.type.equals("entry") && request.validity.equals("valid")) {
                                output = "valid";
                            }
                        }
                        if (output == "valid") {
                            validationOutput = "userEnter.txt";
                            flag = false;
                        } else {
                            validationOutput = "userCantEnter.txt";
                        }
                    }
                    System.out.println("Exiting while loop");
                    File file = new File(WEB_ROOT, validationOutput);
                    int fileLength = (int) file.length();
                    String content = getContentType(validationOutput);
                    byte[] fileData = readFileData(file, fileLength);

                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Java HTTP Server from Parva : 1.0");
                    out.println("Date: " + new Date());
                    out.println("Content-type: " + content);
                    out.println("Content-length: " + fileLength);
                    out.println();
                    out.flush();

                    dataOut.write(fileData, 0, fileLength);
                    dataOut.flush();
                }
                else if (params.contains("/checkexit?id=")) {

                    String id = params.replace("/checkexit?id=", "");

                    flag = true;
                    new Thread(() -> {
                        try {
                            Thread.sleep(10000);
                            flag = false;
                        }
                        catch (Exception e){
                            System.err.println(e);
                        }
                    }).start();

                    String validationOutput = "userCantExit.txt";

                    while(flag) {
                        // Check for the user's in-time being entered in the database.

                        String output = "invalid";
                        // Check for the last 10 records in the requests. If the same id exists with a valid flag, let them enter.
                        Integer checkCount = 10;
                        Integer lengthOfRequests = requests.size();
                        for (int i=lengthOfRequests-1; (i>lengthOfRequests - checkCount) && (i>=0); i--) {
                            QRRequest request = requests.get(i);
                            if (id.equals(request.ID) && request.type.equals("exit") && request.validity.equals("valid")) {
                                output = "valid";
                            }
                        }
                        if (output == "valid") {
                            validationOutput = "userExit.txt";
                            flag = false;
                        } else {
                            validationOutput = "userCantExit.txt";
                        }
                    }

                    File file = new File(WEB_ROOT, validationOutput);
                    int fileLength = (int) file.length();
                    String content = getContentType(validationOutput);
                    byte[] fileData = readFileData(file, fileLength);

                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Java HTTP Server from Parva : 1.0");
                    out.println("Date: " + new Date());
                    out.println("Content-type: " + content);
                    out.println("Content-length: " + fileLength);
                    out.println();
                    out.flush();

                    dataOut.write(fileData, 0, fileLength);
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

    public QRRequest(String id, String val, String t) {
        ID = id;
        validity = val;
        type = t;
    }

    public static void main(String[] args) {
        System.out.println("New request created.");
    }
}