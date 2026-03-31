# Quick Reference Card

## Startup (One Command)
```bash
.\start-all-services.ps1
```

## Service URLs
| Service | URL | Status Check |
|---------|-----|--------------|
| Config Server | http://localhost:8888 | `curl http://localhost:8888` |
| Eureka | http://localhost:8761 | Dashboard in browser |
| Product Service | http://localhost:8050 | Check Eureka dashboard |
| Customer Service | http://localhost:8081 | Check Eureka dashboard |
| Zipkin | http://localhost:9411 | Dashboard in browser |
| PgAdmin | http://localhost:5050 | `admin` / `admin` |
| Mongo Express | http://localhost:8081 | Dashboard in browser |

## Ports in Use
```
8888   → Config Server
8761   → Eureka Discovery
8050   → Product Service
8081   → Customer Service
5432   → PostgreSQL
27017  → MongoDB
9411   → Zipkin
5050   → PgAdmin
9092   → Kafka
2181   → Zookeeper
1080   → Mail Dev
1025   → Mail Dev SMTP
```

## Essential Commands

### Docker
```bash
# Check containers
docker ps

# View logs
docker logs -f container_name

# Start infrastructure
docker-compose up -d

# Stop all
docker-compose down

# Reset (remove data)
docker-compose down -v
```

### Maven
```bash
# Run tests
mvn clean test

# Build all
mvn clean install

# Skip tests
mvn clean install -DskipTests

# Run service
mvn spring-boot:run

# Compile only
mvn clean compile
```

### Git
```bash
# Manual push
git add .
git commit -m "message"
git push origin main

# Or use script
.\git-push.ps1
```

## Startup Order (If Not Using Script)
```bash
# 1. Start Docker
docker-compose up -d

# 2. Wait 30 seconds, then start Config Server
cd services/config-server
mvn spring-boot:run
# Wait 10 seconds

# 3. Start Discovery Service (new terminal)
cd services/discovery
mvn spring-boot:run
# Wait 10 seconds

# 4. Start microservices (new terminals)
cd services/product
mvn spring-boot:run

cd services/customer
mvn spring-boot:run
```

## Test Commands
```bash
# Test specific service
cd services/product && mvn clean test

# Test all
mvn clean test -f services/pom.xml

# Test with coverage
mvn clean test jacoco:report

# Single test class
mvn test -Dtest=ProductApplicationTests
```

## Troubleshooting Quick Fixes

### Service won't start
1. Check dependencies: `docker ps`
2. View logs: `docker logs container_name`
3. Check ports: `netstat -ano | findstr :PORT`
4. Kill process: `taskkill /PID id /F`

### Test failures
1. Use test profile: `mvn test -Dspring.profiles.active=test`
2. Skip tests: `mvn clean install -DskipTests`
3. Clear cache: `mvn clean`

### Connection errors
1. Verify PostgreSQL: `docker logs ms_pg_sql`
2. Check config server: `curl http://localhost:8888`
3. Check Eureka: http://localhost:8761

### Port conflicts
```bash
# Find what's using port
netstat -ano | findstr :8888

# Kill process
taskkill /PID process_id /F
```

## Important Files

| Purpose | Location |
|---------|----------|
| Startup Instructions | STARTUP_GUIDE.md |
| Troubleshooting | TROUBLESHOOTING.md |
| Changes Made | CHANGES_SUMMARY.md |
| Startup Script | start-all-services.ps1 |
| Git Push Script | git-push.ps1 |
| Product Service Config | services/config-server/configurations/product-service.yml |
| Customer Service Config | services/config-server/configurations/customer-service.yml |
| Product Test Config | services/product/src/test/resources/application-test.yml |
| Docker Config | docker-compose.yml |

## Database Access

### PostgreSQL
```
Host: localhost
Port: 5432
User: alibou
Password: alibou
Database: product
```

Use PgAdmin: http://localhost:5050
Email: `pgadmin4@pgadmin.org` / Password: `admin`

### MongoDB
```
Host: localhost
Port: 27017
User: alibou
Password: alibou
```

Use Mongo Express: http://localhost:8081

## Verify All Systems Green

✅ Docker: `docker ps` shows all containers running
✅ Config Server: `curl http://localhost:8888` returns config
✅ Eureka: http://localhost:8761 shows services registered
✅ PostgreSQL: Can connect to localhost:5432
✅ MongoDB: Can connect to localhost:27017
✅ Tests: `mvn test` passes

## Common Error Messages

| Error | Quick Fix |
|-------|-----------|
| "Connection refused" | Start dependencies first |
| "Address already in use" | Kill process or change port |
| "Cannot find symbol EnableConfigServer" | Clean cache, rebuild |
| "Failed to configure DataSource" | Check PostgreSQL is running |
| "Unable to find @SpringBootConfiguration" | Use `@ActiveProfiles("test")` |
| "password authentication failed" | Check credentials in docker-compose |
| "Cannot execute request on any known server" | Start Eureka server first |

## Key Concepts

**Config Server:** Central configuration management (port 8888)
**Eureka:** Service discovery and registration (port 8761)
**Flyway:** Database schema migrations (auto-managed)
**PostgreSQL:** Product service database
**MongoDB:** Customer/Order service database
**Kafka:** Message broker for async communication
**Zipkin:** Distributed tracing for monitoring

## Quick Startup Example

```bash
# Terminal 1: Start infrastructure
docker-compose up -d

# Terminal 2: Config Server
cd services/config-server && mvn spring-boot:run
# Wait 10 seconds

# Terminal 3: Discovery Service
cd services/discovery && mvn spring-boot:run
# Wait 10 seconds

# Terminal 4: Product Service
cd services/product && mvn spring-boot:run

# Terminal 5: Run tests (or same as terminal 4 after service starts)
cd services/product && mvn clean test
```

## Git Workflow

```bash
# View changes
git status

# Push changes
.\git-push.ps1

# View recent commits
git log --oneline -10

# View specific file changes
git diff path/to/file

# Undo recent commit (be careful!)
git reset --soft HEAD~1
```

## IDE Setup

**IntelliJ IDEA:**
1. File → Settings → Build Tools → Maven
2. Set JDK to Java 21
3. Enable Annotation Processors
4. Rebuild project

**VS Code:**
1. Install Java Extension Pack
2. Install Spring Boot Extension Pack
3. Configure Maven

## Windows-Specific Commands

```bash
# Check if service running
tasklist | findstr java

# Kill Java process
taskkill /IM java.exe /F

# Check port usage
netstat -ano | findstr :PORT_NUMBER

# Set environment variable
set VAR_NAME=value

# PowerShell set environment
$env:VAR_NAME="value"
```

## Performance Notes

- First run: 3-5 minutes for all services
- Maven downloads: ~500MB
- Docker images: ~2GB
- H2 tests: Fast (in-memory)
- PostgreSQL: Persistent (reset with `docker-compose down -v`)

---

**For detailed information, see:**
- STARTUP_GUIDE.md
- TROUBLESHOOTING.md
- CHANGES_SUMMARY.md

