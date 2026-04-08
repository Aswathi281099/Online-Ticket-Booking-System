import streamlit as st

# -------------------------------
# Session State Initialization
# -------------------------------
if 'users' not in st.session_state:
    st.session_state.users = {"user": {"password": "pass", "full_name": "Demo User"}}

if 'bookings' not in st.session_state:
    st.session_state.bookings = []

if 'tickets' not in st.session_state:
    st.session_state.tickets = [
        {"ticketId": "T1", "ticketType": "Bus", "price": 100},
        {"ticketId": "T2", "ticketType": "Train", "price": 200},
        {"ticketId": "T3", "ticketType": "Flight", "price": 500}
    ]

if 'logged_in' not in st.session_state:
    st.session_state.logged_in = False

if 'username' not in st.session_state:
    st.session_state.username = None

if 'full_name' not in st.session_state:
    st.session_state.full_name = None

if 'menu' not in st.session_state:
    st.session_state.menu = "Login"

# -------------------------------
# UI Title
# -------------------------------
st.title("🎫 Online Ticket Booking System")

# -------------------------------
# Sidebar Menu
# -------------------------------
if st.session_state.logged_in:
    st.session_state.menu = st.sidebar.selectbox(
        "Menu",
        ["Dashboard", "Book Ticket", "View Bookings", "Cancel Ticket", "Logout"]
    )
else:
    st.session_state.menu = st.sidebar.selectbox(
        "Menu",
        ["Login", "Register"]
    )

menu = st.session_state.menu

# -------------------------------
# LOGIN
# -------------------------------
if menu == "Login":
    st.header("Login Page")

    username = st.text_input("Username").strip()
    password = st.text_input("Password", type="password").strip()

    if st.button("Login"):
        user = st.session_state.users.get(username)

        if user:
            if user["password"] == password:
                st.session_state.logged_in = True
                st.session_state.username = username
                st.session_state.full_name = user["full_name"]
                st.session_state.menu = "Dashboard"
                st.success(f"Welcome, {user['full_name']}!")
                st.rerun()
            else:
                st.error("Wrong password")
        else:
            st.error("User not found")

# -------------------------------
# REGISTER
# -------------------------------
elif menu == "Register":
    st.header("Register New Account")

    full_name = st.text_input("Full Name").strip()
    username = st.text_input("Username").strip()
    password = st.text_input("Password", type="password").strip()

    if st.button("Register"):
        if username in st.session_state.users:
            st.error("Username already exists")
        elif username == "" or password == "" or full_name == "":
            st.error("All fields are required")
        else:
            st.session_state.users[username] = {
                "password": password,
                "full_name": full_name
            }

            # ✅ FIXED (no rerun)
            st.success("Registration successful! Now go to Login from sidebar.")

# -------------------------------
# DASHBOARD
# -------------------------------
elif menu == "Dashboard":
    st.markdown(f"## 👋 Welcome, {st.session_state.full_name}!")

    total_users = len(st.session_state.users)
    total_bookings = len(st.session_state.bookings)
    total_revenue = sum(b.get("totalFare", 0) for b in st.session_state.bookings)

    col1, col2, col3 = st.columns(3)
    col1.metric("👥 Users", total_users)
    col2.metric("🎟️ Bookings", total_bookings)
    col3.metric("💰 Revenue", f"₹{total_revenue}")

# -------------------------------
# BOOK TICKET
# -------------------------------
elif menu == "Book Ticket":
    st.markdown("## 🎟️ Book Your Ticket")

    tickets = st.session_state.tickets

    ticket_options = {
        f"{t['ticketId']} - {t['ticketType']} (₹{t['price']})": t
        for t in tickets
    }

    selected_key = st.selectbox("Select Ticket Type", list(ticket_options.keys()))
    selected_ticket = ticket_options[selected_key]

    st.markdown(f"**Selected Ticket:** {selected_ticket['ticketType']}")
    st.markdown(f"**Price per Ticket:** ₹{selected_ticket['price']}")

    quantity = st.number_input("Number of Tickets", min_value=1, max_value=10, value=1)

    total_fare = selected_ticket["price"] * quantity

    st.markdown(f"### 💰 Total Fare: ₹{total_fare}")

    col1, col2 = st.columns(2)

    with col1:
        if st.button("Book Ticket"):
            booking = {
                "bookingId": len(st.session_state.bookings) + 1,
                "username": st.session_state.username,
                "ticketId": selected_ticket["ticketId"],
                "ticketType": selected_ticket["ticketType"],
                "price": selected_ticket["price"],
                "quantity": quantity,
                "totalFare": total_fare
            }

            st.session_state.bookings.append(booking)

            st.success(f"""
Booking Successful!

Booking ID: {booking['bookingId']}
Ticket: {booking['ticketType']}
Quantity: {quantity}
Total Fare: ₹{total_fare}
""")

    with col2:
        if st.button("Clear"):
            st.rerun()

# -------------------------------
# VIEW BOOKINGS
# -------------------------------
elif menu == "View Bookings":
    st.header("My Bookings")

    user_bookings = [
        b for b in st.session_state.bookings
        if b["username"] == st.session_state.username
    ]

    if user_bookings:
        for booking in user_bookings:
            with st.expander(f"Booking ID: {booking['bookingId']}"):
                st.write(f"Ticket ID: {booking['ticketId']}")
                st.write(f"Ticket Type: {booking['ticketType']}")
                st.write(f"Price: ₹{booking['price']}")
                st.write(f"Quantity: {booking['quantity']}")
                st.write(f"Total Fare: ₹{booking['totalFare']}")
    else:
        st.info("No bookings yet.")

# -------------------------------
# CANCEL BOOKING
# -------------------------------
elif menu == "Cancel Ticket":
    st.header("Cancel Booking")

    user_bookings = [
        b for b in st.session_state.bookings
        if b["username"] == st.session_state.username
    ]

    if user_bookings:
        booking_options = {
            f"{b['bookingId']} - {b['ticketType']} (₹{b['totalFare']})": b["bookingId"]
            for b in user_bookings
        }

        selected_key = st.selectbox("Select Booking to Cancel", list(booking_options.keys()))
        selected_id = booking_options[selected_key]

        if st.button("Cancel Booking"):
            st.session_state.bookings = [
                b for b in st.session_state.bookings
                if b["bookingId"] != selected_id
            ]
            st.success("Booking cancelled successfully!")
            st.rerun()
    else:
        st.info("No bookings to cancel.")

# -------------------------------
# LOGOUT
# -------------------------------
elif menu == "Logout":
    if st.button("Confirm Logout"):
        st.session_state.logged_in = False
        st.session_state.username = None
        st.session_state.full_name = None
        st.session_state.menu = "Login"
        st.success("Logged out successfully!")
        st.rerun()