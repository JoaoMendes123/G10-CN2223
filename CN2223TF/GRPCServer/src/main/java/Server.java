import io.grpc.ServerBuilder;
import java.util.Scanner;
import java.util.logging.Logger;

public class Server {
    public static final int SERVER_PORT = 7001;
    public static io.grpc.Server svc;

    public static void main(String[] args) {
        try {
            svc = ServerBuilder.forPort(SERVER_PORT).addService(new ContractMockImplementation()).build();
            svc.start();
            System.out.println("Server is listening on port "+ SERVER_PORT);
            Scanner in = new Scanner(System.in);in.nextLine();
            svc.shutdown();
        }catch(Exception e){
            System.out.println("Something went wrong:" + e.toString());
            svc.shutdownNow();
        }
        System.out.println("Hello, gRPC world!");
    }

}
