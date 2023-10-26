package server;

import spark.Spark;

public class Server {
    public static void main(String[] args) {
        Spark.externalStaticFileLocation("path/to/web/folder");
    }

}
