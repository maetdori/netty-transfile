# netty-transfile
๐ ๋คํฐ ํ์ผ์ ์ก ๋ฏธ๋ํ๋ก์ ํธ

## ํ๋กํ ์ฝ

### 1. DATA(FILE) ๋ฐ์ก ๊ตฌ์กฐ 
|Length (8 bytes)|Opcode (2 bytes)|Data|File|
|---|---|---|---|
* Length: 2 bytes (Opcode) + Data ๊ธธ์ด
* Opcode: `FI` (FI๋ก ๊ณ ์ )
* Data ๊ตฌ์กฐ(์์)
  ```
  CMD::=C\r\n
  FILENAME::=test.txt\r\n
  FILESIZE::=1234\r\n
  DIR::=/home/service/file/\r\n
  
  // <Key, Value>์ Delimiter๋ "::="์ ์ฌ์ฉํ๋ฉฐ, ๊ตฌ๋ถ์ "\r\n"(CRLF)๋ฅผ ์ฌ์ฉํ๋ค.
* File์ ์ค์  ํ์ผ ์ ์ก
* CMD ์ฌ์ฉ
  * ์์ฑ: `CMD::=C`
  * ์ญ์ : `CMD::=D`
  
### 2. Result ๊ตฌ์กฐ
|Length (8 bytes)|Opcode (2 bytes)|Data|
|---|---|---|
* Length: 2 bytes (Opcode) + Data ๊ธธ์ด
* Opcode: `FI` (FI๋ก ๊ณ ์ )
* Data
  * `8000`: ์ฑ๊ณต
  * `8001`: ์ ๋ฌธ ํ์ ์๋ฌ
  * `8002`: ์๋ชป๋ CMD
  * `8003`: ํ์ผ ์์ฑ ์คํจ
  * `8004`: ํ์ผ ์ญ์  ์คํจ
  * `9999`: ๋ด๋ถ ์๋ฒ ์๋ฌ
