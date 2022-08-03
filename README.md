# netty-transfile
🌐 네티 파일전송 미니프로젝트

## 프로토콜

### 1. DATA(FILE) 발송 구조 
|Length (8 bytes)|Opcode (2 bytes)|Data|File|
|---|---|---|---|
* Length: 2 bytes (Opcode) + Data 길이
* Opcode: `FI` (FI로 고정)
* Data 구조(예시)
  ```
  CMD::=C\r\n
  FILENAME::=test.txt\r\n
  FILESIZE::=1234\r\n
  DIR::=/home/service/file/\r\n
  
  // <Key, Value>의 Delimiter는 "::="을 사용하며, 구분은 "\r\n"(CRLF)를 사용한다.
* File은 실제 파일 전송
* CMD 사용
  * 생성: `CMD::=C`
  * 삭제: `CMD::=D`
  
### 2. Result 구조
|Length (8 bytes)|Opcode (2 bytes)|Data|
|---|---|---|
* Length: 2 bytes (Opcode) + Data 길이
* Opcode: `FI` (FI로 고정)
* Data
  * `8000`: 성공
  * `8001`: 전문 형식 에러
  * `8002`: 잘못된 CMD
  * `8003`: 파일 생성 실패
  * `8004`: 파일 삭제 실패
  * `9999`: 내부 서버 에러
