# Banking App – Spring Boot + PostgreSQL (Docker Compose)

## Chạy
```bash
docker compose up -d --build
docker compose logs -f banking-app     # xem log HikariCP kết nối banking_db
curl localhost:8080/api/health         # {"status":"UP","database":"banking_db"}
```

## Thành phần
| Service | Image | Port | Ghi chú |
|---|---|---|---|
| core-db | postgres:16-alpine | 5432:5432 | `POSTGRES_DB=banking_db` |
| banking-app | build từ `Dockerfile` | 8080:8080 | `SPRING_DATASOURCE_URL=jdbc:postgresql://core-db:5432/banking_db` |

Dừng: `docker compose down` (thêm `-v` để xoá dữ liệu DB).
