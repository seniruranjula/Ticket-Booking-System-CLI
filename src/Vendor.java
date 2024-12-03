import java.util.logging.Logger;
import java.util.logging.Level;

public class Vendor implements Runnable {
    private static final Logger logger = Logger.getLogger(Vendor.class.getName());
    private final TicketPool ticketPool;
    private final int ticketReleaseRate;

    public Vendor(TicketPool ticketPool, int ticketReleaseRate) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
    }

    @Override
    public void run() {
        int ticketId = 1;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                ticketPool.addTicket(ticketId);
                logger.info("Vendor released ticket " + ticketId);
                ticketId++;
                Thread.sleep(1000 / ticketReleaseRate);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Ensure thread's interrupted status is preserved
            logger.log(Level.WARNING, "Vendor thread interrupted", e);
        } finally {
            logger.info("Vendor thread stopped.");
        }
    }
}
