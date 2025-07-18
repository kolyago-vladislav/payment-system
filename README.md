# Payment System

## Getting Started

### Prerequisites

Make sure you have the following tools installed:

- [Docker](https://docs.docker.com/get-docker/)
- [Postman](https://www.postman.com/) (for API testing, optional)

### Set up .env file

- create .env file in root project and place there
    - NEXUS_USERNAME=admin
    - NEXUS_PASSWORD=admin
    - NEXUS_URL=http://localhost:8081/repository/maven-snapshots/

### Build & Run the Project

To build the Docker image and start the service:

```bash
make all
