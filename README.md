### Cách chạy project

1. Clone project từ git về
2. Mở project bằng intellij
3. Chuyển sang tab terminal
4. Run `mvn clean`
5. Run `mvn install` (chạy tiếp step 6)
6. Lấy file create-user.sql trong project vừa clone về, mở sql plus lên, copy câu lệnh trong file create-user.sql rồi chạy
7. Sau khi step 5 thành công, thì run `mvn flyway:migrate`
8. Nhấn nút chạy project  (góc trên phải hình tam giác)
9. Mở trình duyệt, nhập vào địa chỉ này: `localhost:8080/api`

### Notes:

1. SuperAdmin account: 6051071126@st.utc2.edu.vn / 123456
2. User account thì call API: `POST /api/client/user/register`, sau đó vào email lấy verify token(lấy bằng cách nhấn vào nút verify), sau đó dùng token đó call API: `POST /api/client/user/verify`
3. Để call API secure (API có hình ổ khoá thì trước tiên đăng nhập, rồi lấy access token, sau đó thêm bearer: `Bearer {accessToken}`