import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketPool {
    private static final Logger logger = Logger.getLogger(TicketPool.class.getName());
    private final Queue<Integer> tickets = new LinkedList<>();
    private final int maxCapacity;

    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }


    public synchronized void addTicket(int ticketId) {
        while (tickets.size() >= maxCapacity && !Thread.currentThread().isInterrupted()) {
            try {
                logger.info("Waiting to add ticket, pool is full.");
                wait(); // Wait until there is space to add a ticket
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Thread interrupted while waiting to add ticket: " + e.getMessage(), e);
                Thread.currentThread().interrupt(); // Preserve the interrupt status
                return; // Exit the method if interrupted
            }
        }
        if (Thread.currentThread().isInterrupted()) return; // Exit if interrupted before adding ticket
        tickets.add(ticketId);
        logger.info("Ticket " + ticketId + " added to pool.");
        notifyAll(); // Notify all waiting threads that a new ticket is available
    }

    public synchronized Integer removeTicket() {
        while (tickets.isEmpty() && !Thread.currentThread().isInterrupted()) {
            try {
                logger.info("Waiting to retrieve ticket, pool is empty.");
                wait(); // Wait until a ticket becomes available
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Thread interrupted while waiting to retrieve ticket: " + e.getMessage(), e);
                Thread.currentThread().interrupt(); // Preserve the interrupt status
                return null; // Exit method if interrupted
            }
        }
        if (Thread.currentThread().isInterrupted()) return null; // Exit if interrupted before removing ticket
        Integer ticket = tickets.poll();
        logger.info("Ticket " + ticket + " sold.");
        notifyAll(); // Notify all waiting threads that a ticket has been removed
        return ticket;
    }
}
