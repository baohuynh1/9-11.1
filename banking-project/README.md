# account-service – Spring Boot + PostgreSQL (Docker Compose)

## Chạy
```bash
docker compose up -d --build --remove-orphans
docker compose logs -f account-service
```

| Service | Image | Port |
|---|---|---|
| core-db | postgres:16-alpine | không mở ra host (chỉ trong `banking-network`), dữ liệu lưu ở volume `pg-data` |
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
Port 5432 không còn mở ra host, nên truy vấn DB qua `docker exec` như trên.

## transaction-service (port 8081)
Gọi nội bộ `http://account-service:8080` (tên container trong `banking-network`) để kiểm tra tài khoản trước khi xử lý chuyển tiền.

| Method | URL | Mô tả |
|---|---|---|
| POST | `/api/transactions/transfer` | Chuyển tiền (kiểm tra TK nguồn, đích, số dư qua account-service) |
| GET | `/api/transactions` | Danh sách giao dịch |
| GET | `/api/transactions/{id}` | Chi tiết giao dịch |

```json
{ "fromAccountNumber": "9704xxxxxxxx", "toAccountNumber": "9704yyyyyyyy", "amount": 100000, "description": "Chuyen tien" }
```
Code gọi liên service: `transaction-service/src/main/java/com/bank/transaction/client/AccountClient.java`.
