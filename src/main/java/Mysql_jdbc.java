import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Mysql_jdbc {

    public static void main(String[] args) throws SQLException {
        //mysql-connector-java-8.0.28.jar 파일을 다운받고, 에디터에 별도 설정을 해야 아래 구문 실행 가능
        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        //접속 URL, 호스트명, 비밀번호 입력
        String url = "jdbc:mysql://localhost:3306/codelatte2";
        String user = "root";
        String password = "12341234";
        //세션 생성 (주석처리한 코드를 같이 실행하면, 세션이 2개가 생성됨)
        Connection connection = DriverManager.getConnection(url, user, password);
        Connection connection2 = DriverManager.getConnection(url, user, password);
        //update 쿼리문("UPDATE GOODS SET GD_STOCK = GD_STOCK 3 WHERE GD_ID = 1" 실행 예정)
        PreparedStatement statement = connection.prepareStatement("UPDATE GOODS SET GD_STOCK = GD_STOCK - ? WHERE GD_ID = ?");
        statement.setInt(1,3);
        statement.setInt(2,1);
        //쿼리문을 mysql 서버에 전송. 결과값은 영향을 미친 row의 갯수를 의미
        int result = statement.executeUpdate();

        //insert 쿼리문 2번 실행
        String sql2 = "INSERT INTO GOODS (GD_NAME, GD_COST, GD_STOCK) VALUES ('상품1', 2000, 10)";
        PreparedStatement statement2 = connection.prepareStatement(sql2);
        statement2.executeUpdate();
        String sql3 = "INSERT INTO GOODS (GD_NAME, GD_COST, GD_STOCK) VALUES ('상품2', 1000, 8)";
        PreparedStatement statement3 = connection.prepareStatement(sql3);
        statement3.executeUpdate();

        //select 쿼리문 실행
        String sql4 = "SELECT * FROM GOODS";
        PreparedStatement statement4 = connection.prepareStatement(sql4);
        //select 쿼리문 실행 결과 데이터 저장 및 resultSet의 각 노드의 결과믈을 저장할 자료형(ArrayList) 선언
        ResultSet resultSet = statement4.executeQuery();
        List<Goods> list = new ArrayList<>(); //list의 맨 마지막에 goods 객체를 저장할 것이므로, ArrayList로 선언함.
        //쿼리문 실행 결과의 데이터 세부분석 및 세부 데이터 저장
        while(resultSet.next()) {
            Goods goods = new Goods(); //여기서 Goods class를 정의해야 함을 자연스럽게 알 수 있다.
            goods.id = resultSet.getInt("GD_ID");
            goods.name = resultSet.getString("GD_NAME");
            goods.cost = resultSet.getInt("GD_COST");
            goods.stock = resultSet.getInt("GD_STOCK");
            list.add(goods);
        }
        //아래는 콘솔에서 goods의 세부 정보를 출력하기 위한 코드
        StringBuilder builder = new StringBuilder();
        for(Goods goods : list) {
            builder.append(String.format("%5d %5s %5d %5d", goods.id, goods.name, goods.cost, goods.stock));
        }
        System.out.println(builder);

        //연결 종료하기
        try(connection;statement;statement2;statement3;statement4;resultSet) {}
        catch(SQLException e) { e.printStackTrace();}
    }
}
