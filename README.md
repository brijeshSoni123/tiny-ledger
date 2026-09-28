# Getting Started

## Intent
Build a tiny ledger !

## Exposed Endpoints:
* [Ability to record money movements](#ability-to-record-money-movements)
* [View current balance](#view-current-balance)
* [View transaction history](#view-transaction-history)

## Assumptions
1. All transactions are in GBP
2. Users are authenticated and authorized was out of scope.
3. User running the machine has java 25 installed or has internet access to download the jdk.
4. Only basic tests were needed to validate.

### How to start app

Simply run below command using shell:
```bash
./gradlew bootRun
```
The API starts on http://localhost:8080

To run the tests:

```bash
./gradlew test
```

## API Examples:

### Ability to record money movements
* Record a Transaction - DEPOSIT

Request:
```bash
curl -X POST http://localhost:8080/ledger/api/v1/transaction \
  -H "Content-Type: application/json" \
  -d '{"type": "DEPOSIT", "amount": 150.00}'
```
Response:
```json
{
  "message":"Operation Completed Successfully.",
  "id":"6d199089-965c-4518-b4d5-30e03d21e537",
  "type":"DEPOSIT",
  "inputAmount":150.00,
  "currentBalance":150.00,
  "transactionTimestamp":"2026-09-28T16:42:59.9576182+01:00"
}
```

* Record a Transaction - WITHDRAWAL

Request:
```bash
curl -X POST http://localhost:8080/ledger/api/v1/transaction \
  -H "Content-Type: application/json" \
  -d '{"type": "WITHDRAWAL", "amount": 50.00}'
```
Response:
```json
{
  "message":"Operation Completed Successfully.",
  "id":"6d199089-965c-4518-b4d5-30e03d21e537",
  "type":"WITHDRAWAL",
  "inputAmount":50.00,
  "currentBalance":100.00,
  "transactionTimestamp":"2026-09-28T16:42:59.9576182+01:00"
}
```

###  View current balance

Request:
```bash
curl -X GET http://localhost:8080/ledger/api/v1/balance \
  -H "Content-Type: application/json"
```
Response:
```json
{
  "message" : "Operation Completed Successfully.",
  "id" : "8cdf2459-7220-42f7-9744-6aa6cb70d732",
  "currentBalance" : 100.0,
  "transactionTimestamp" : "2026-09-28T16:46:52.125768+01:00"
}
```

### View transaction history
  Request:
```bash
curl -X GET http://localhost:8080/ledger/api/v1/transactions \
  -H "Content-Type: application/json"
```
Response:
```json
[
  {
    "message": "Operation Completed Successfully.",
    "id": "de41ab19-b914-48d4-af36-e6931b9ec272",
    "type": "WITHDRAWAL",
    "inputAmount": 50.00,
    "currentBalance": 100.00,
    "transactionTimestamp": "2026-09-28T16:53:09.8792636+01:00"
  },
  {
    "message": "Operation Completed Successfully.",
    "id": "3336e57c-d162-4a77-8426-2ff8f889f656",
    "type": "DEPOSIT",
    "inputAmount": 150.00,
    "currentBalance": 150.00,
    "transactionTimestamp": "2026-09-28T16:53:04.6554242+01:00"
  }
]
```