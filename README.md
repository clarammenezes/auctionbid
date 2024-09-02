# Project overview

**Project Idea: Online Auction Platform**

**Overview:** Developing an online auction platform where users can list items for auction, place bids, and track auction outcomes in real-time. The platform should support notifications to users about bid updates, auction results, and item availability using Kafka’s messaging capabilities. The project will also include complex features like user roles, real-time updates, and possibly integration with third-party services.

**Core Features:**

1. **User Management:**
    - Implement different user roles such as buyers, sellers, and administrators.
    - Use Spring Security for authentication and authorization.
2. **Auction Listings:**
    - Sellers can create auction listings with details like starting price, auction duration, and item description.
    - Listings will be stored in MongoDB Atlas and Compass, with information about bids, seller, and auction status.
3. **Bidding System:**
    - Buyers can place bids on items. The system should handle the logic for outbidding, and determining the winning bid.
    - Kafka will be used to notify all subscribers (e.g., bidders) of new bids, updates, or auction endings.
4. **Real-Time Notifications:**
    - Implement Kafka to push real-time notifications to users who have subscribed to specific auctions or sellers. This will notify them about new bids, auction end times, or if they’ve been outbid.
5. **Search and Filtering:**
    - Provide search functionality that allows users to find auctions based on categories, keywords, price range, and auction status (e.g., live, ended, won).
6. **Watchlist/Subscriptions:**
    - Users can add items or sellers to their watchlist, subscribing to updates via Kafka. For example, they might get a message when an auction they’re watching is about to end, or when a seller they follow lists a new item.
7. **Auction Closing:**
    - Handle auction closures, determining the winner and notifying both the seller and the highest bidder.

**Technical Integration:**

1. **Spring Boot:**
    - Microservices for user management, auction listings, bidding, and notifications.
    - Use Spring Data MongoDB Atlas and Compass to interact with the database.
2. **Kafka:**
    - **Producer:** The auction service will act as a producer, sending messages to Kafka when bids are placed, auctions are created or closed, etc.
    - **Consumers:** Users who subscribe to specific auctions, items, or sellers will be consumers, receiving real-time updates about the events they’re interested in.
3. **Docker and Kubernetes:**
    - Containerize microservices with Docker, and use Kubernetes for orchestration, ensuring the platform scales based on user activity.
    - Use Kubernetes to manage Kafka and ZooKeeper clusters, ensuring reliable messaging.
4. **MongoDB**:
    - Store user data, auction listings, bids, and notifications. 
    - Use MongoDB Atlas for cloud-based storage and MongoDB Compass for data visualization.
5. **UML:**





