
package studentbooking.applications;

import studentbooking.db.DBHelper;
import studentbooking.bean.OperatorEntity;
import studentbooking.bean.StudentEntity;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static javafx.geometry.HPos.LEFT;

/**
 * Created by leiYang on 2016/11/27.
 */
public class Login extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("学生火车票订票系统");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 25, 40, 25));

        Text scenetitle = new Text("欢迎使用！");
        scenetitle.setFont(Font.font(22));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("   用户名:");
        grid.add(userName, 0, 2);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 2);

        Label pw = new Label("      密码:");
        grid.add(pw, 0, 3);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 3);

        Label lt = new Label("登陆选项:");
        grid.add(lt, 0, 4);

        ChoiceBox choiceBox = new ChoiceBox(FXCollections.observableArrayList("普通用户","操作员"));
        choiceBox.setValue("普通用户");
        grid.add(choiceBox,1,4);


        Button btn = new Button("  登陆  ");
        btn.getStyleClass().add("button5");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 6);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 0, 6);
        grid.setColumnSpan(actiontarget, 2);
        grid.setHalignment(actiontarget, LEFT);
        actiontarget.setId("actiontarget");

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                String account = userTextField.getText().toString();
                String password = pwBox.getText().toString();
                String choice = choiceBox.getValue().toString();
                System.out.println(choice);
                DBHelper dbHelper = new DBHelper();

                if (choice.equals("普通用户")){

                    dbHelper.executeSQL("SELECT * FROM Student where(name='"+account+"' and password='"+password+"')");
                    ResultSet resultSet = dbHelper.getResultSet();
                    DBHelper dbHelper1 = new DBHelper();
                    dbHelper1.executeSQL("SELECT * FROM Student where(name='"+account+"' and password='"+password+"')");
                    ResultSet resultSet1 = dbHelper1.getResultSet();
                    try {
                        if (!resultSet1.next()){
                            actiontarget.setFill(Color.FIREBRICK);
                            actiontarget.setText("    账号或密码错误！");
                            return;
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    StudentEntity studentEntity = new StudentEntity();
                    try {
                        while (resultSet.next()){
                            System.out.println(resultSet.getString("address"));

                            studentEntity.setAddress(resultSet.getString("address"));
                            studentEntity.setAge(resultSet.getInt("age"));
                            studentEntity.setFaculty(resultSet.getString("faculty"));
                            studentEntity.setName(resultSet.getString("name"));
                            studentEntity.setPassword(resultSet.getString("password"));
                            studentEntity.setPhoneNum(resultSet.getString("phone_num"));
                            studentEntity.setProfession(resultSet.getString("profession"));
                            studentEntity.setSex(resultSet.getString("sex"));
                            studentEntity.setStudentId(resultSet.getString("student_id"));
                            studentEntity.setUniversity(resultSet.getString("university"));

                            System.out.println(studentEntity.getPhoneNum());
                        }


                        primaryStage.close();

                        newState(primaryStage,studentEntity);

                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }else if (choice.equals("操作员")){
                    dbHelper.executeSQL("SELECT * FROM Operator where(account='"+account+"' and password='"+password+"')");
                    ResultSet resultSet = dbHelper.getResultSet();
                    DBHelper dbHelper1 = new DBHelper();
                    dbHelper1.executeSQL("SELECT * FROM Operator where(account='"+account+"' and password='"+password+"')");
                    ResultSet resultSet1 = dbHelper1.getResultSet();
                    try {
                        if (!resultSet1.next()){
                            actiontarget.setFill(Color.FIREBRICK);
                            actiontarget.setText("    工号或密码错误！");
                            return;
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    OperatorEntity operatorEntity = new OperatorEntity();
                    try {
                        while (resultSet.next()){
                            operatorEntity.setName(resultSet.getString("name"));
                            operatorEntity.setPassword(resultSet.getString("password"));
                            operatorEntity.setPhoneNum(resultSet.getString("phone_num"));
                            operatorEntity.setSex(resultSet.getString("sex"));
                            operatorEntity.setAccount(resultSet.getInt("account"));
                        }

                        primaryStage.close();

                        newOperatorState(primaryStage,operatorEntity);

                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

        });

        Scene scene = new Scene(grid, 350, 290);
        scene.getStylesheets().add("studentbooking/css/button.css");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    private void newOperatorState(Stage primaryStage, OperatorEntity operatorEntity) throws IOException {
        SelectTicketForOperator selectTicketForOperator = new SelectTicketForOperator(operatorEntity);
        selectTicketForOperator.start(primaryStage);
    }

    public void newState(Stage primaryStage,StudentEntity studentEntity) throws IOException {

//        primaryStage.setTitle("FXML TableView Example");
//        Pane myPane = (Pane) FXMLLoader.load(getClass().getResource("fxml_tableview.fxml"));
//        Scene myScene = new Scene(myPane);
//        primaryStage.setScene(myScene);
//        primaryStage.show();
//        primaryStage.close();
        SelectTicket selectTicket = new SelectTicket(studentEntity);
        selectTicket.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
