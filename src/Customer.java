import java.util.logging.Logger;
import java.util.logging.Level;

public class Customer implements Runnable {
    private static final Logger logger = Logger.getLogger(Customer.class.getName());
    private final TicketPool ticketPool;
    private final int customerRetrievalRate;

    public Customer(TicketPool ticketPool, int customerRetrievalRate) {
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Integer ticket = ticketPool.removeTicket();
                if (ticket != null) {
                    logger.info("Customer bought ticket " + ticket);
                } else {
                    logger.info("No tickets available for customer.");
                }
                Thread.sleep(1000 / customerRetrievalRate);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Preserve interrupted status
            logger.log(Level.WARNING, "Customer thread interrupted", e);
        } finally {
            logger.info("Customer thread stopped.");
        }
    }
}
