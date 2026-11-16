# account-service – Spring Boot + PostgreSQL (Docker Compose)

## Chạy
```bash
docker compose up -d --build --remove-orphans
docker compose logs -f account-service
```

| Service | Image | Port |
|---|---|---|
| core-db | postgres:16-alpine | 5432:5432 (DB `banking_db`, user/pass `postgres`/`postgres`) |
| account-service | build từ `Dockerfile` | 8080:8080 |

## API
| Method | URL | Mô tả |
|---|---|---|
| POST | `/api/accounts` | Tạo tài khoản (201 Created) |
| GET | `/api/accounts` | Danh sách tài khoản |
| GET | `/api/accounts/{id}` | Truy vấn theo id |
| GET | `/api/accounts/number/{accountNumber}` | Truy vấn theo số tài khoản |

Body tạo tài khoản:
```json
{
  "ownerName": "Nguyen Van A",
  "email": "nguyenvana@example.com",
  "phone": "0901234567",
  "accountType": "SAVINGS",
  "initialDeposit": 5000000,
  "currency": "VND"
}
```
`accountType`: `SAVINGS` | `CHECKING`. Import sẵn: `postman/account-service.postman_collection.json`.

## Kiểm tra dữ liệu
```bash
docker exec -it core-db psql -U postgres -d banking_db -c "SELECT * FROM accounts;"
```
DBeaver: PostgreSQL – host `localhost`, port `5432`, database `banking_db`, user `postgres`, password `postgres`.
