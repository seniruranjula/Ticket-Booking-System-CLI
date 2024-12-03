import java.util.Scanner;

public class Configuration {
    public int totalTickets;
    public int ticketReleaseRate;
    public int customerRetrievalRate;
    public int maxTicketCapacity;

    public void loadConfiguration() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter total number of tickets: ");
        totalTickets = scanner.nextInt();

        System.out.print("Enter ticket release rate (tickets per second): ");
        ticketReleaseRate = scanner.nextInt();

        System.out.print("Enter customer retrieval rate (tickets per second): ");
        customerRetrievalRate = scanner.nextInt();

        System.out.print("Enter max ticket capacity in pool: ");
        maxTicketCapacity = scanner.nextInt();

        System.out.println("Configuration loaded successfully.");
    }
}
