# TicketMaster - High-Concurrency Booking Engine

This backend system handles real-time seat reservations for high-demand concert events, implementing advanced concurrency control and dynamic pricing strategies.

## 🚀 Key Features

### 1. 10-Minute Exclusive Seat Hold
Implemented **Pessimistic Locking** (`FOR UPDATE`) to handle high-concurrency requests. When a seat is held, a 10-minute timer starts. If another user attempts to hold the same seat, the system returns a `409 Conflict` with the remaining seconds.

**Success: Seat Hold (Postman)**
![Seat Hold Success](images/seat_hold_success.png)
*(Image: Showing 200 OK response with holdExpiry timestamp)*

**Error: Seat Already Locked**
![Seat Locked Error](images/seat_locked_error.png)
*(Image: Showing 409 Conflict and secondsRemaining DTO)*

---

### 2. Tiered Pricing (Strategy Pattern)
The system calculates ticket prices based on the **User Tier** (Regular, VIP, Platinum) and **Event Demand**.
* **VIPs** get a 10% discount unless the event is marked as `HIGH_DEMAND`.
* **Platinum** users receive priority access flags in the booking response.

**Pricing Logic Flow**
![Strategy Pattern Diagram]()
*(Image: Diagram showing the Strategy Pattern implementation for price calculation)*

---

### 3. Audit Shadow (Spring AOP)
A specialized security requirement where all failed booking attempts are automatically logged into an `audit_logs` table without polluting the core business logic. This is achieved using **Spring AOP** and a custom `@AuditFailure` annotation.

**Audit Logs in Database**
![Audit Log Table](images/audit_log_db.png)
*(Image: Screenshot of MySQL Workbench showing recorded failures and timestamps)*

---

## 🛠️ Database Architecture
The system utilizes a relational schema to maintain data integrity between Users, Events, Seats, and Bookings.

**Entity Relationship Diagram (ERD)**
![ER Diagram](/src/main/resources/images/ticketMaster.drawio.png)
*(Image: Your project ER Diagram)*

---

## 🚦 How to Run & Test
1. **Database Setup**: Run the provided `schema.sql` and `data.sql` in your MySQL instance.
2. **Configuration**: Update `src/main/resources/application.properties` with your MySQL credentials.
3. **Execution**: Run the `Main.java` class.
4. **Testing**: Import the provided Postman Collection to test the following lifecycle:
    - `POST /api/seats/{id}/hold`
    - `POST /api/bookings`
    - `POST /api/seats/{id}/cancel`