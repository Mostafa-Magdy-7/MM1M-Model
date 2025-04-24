# MM1M Queue Simulation (JavaFX)

A simple simulation of the **MM1M Queue Model** using JavaFX.  
The application visually represents customers arriving and being serviced in a single-server queue system.

---

## 🚀 Features

- Customers are represented as red circles.
- The customer being served turns blue.
- Arrival rate (`λ - lambda`) and service rate (`μ - mu`) are simulated using exponential distributions.
- Real-time animation of customers entering and leaving the queue.

---

## 🧠 Concepts

This project demonstrates:

- **MM1M Queue Model**  
  Single queue, single server — customers arrive randomly and get served one by one.

- **Exponential Distribution**  
  Used to simulate random intervals for arrivals and service time.

- **JavaFX Threading**  
  UI updates via `Platform.runLater`, background processing on separate threads.
