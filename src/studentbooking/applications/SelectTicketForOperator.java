
package studentbooking.applications;

import javafx.scene.shape.Circle;
import studentbooking.db.DBHelper;
import studentbooking.bean.OperatorEntity;
import studentbooking.bean.OrdersEntity;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Reflection;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static javafx.geometry.Pos.CENTER;
import static javafx.geometry.Pos.CENTER_RIGHT;

/**
 * Created by leiYang on 2016/11/27.
 */

public class SelectTicketForOperator extends Application {


    OperatorEntity operatorEntity ;
    TableView tableView = new TableView();
    String mStart = "出发地";
    String mEnd = "目的地";

    Label label = new Label();

    ObservableList<OrdersEntity> data1 = FXCollections.observableArrayList();


    public static void main(String[] args) {
        launch(SelectTicketForOperator.class, args);
    }

    public SelectTicketForOperator(OperatorEntity operatorEntity) {
        this.operatorEntity = operatorEntity;
    }

    @Override
    public void start(Stage stage) throws IOException {

        BorderPane border = new BorderPane();
        HBox hbox = addHBox();

        border.setTop(hbox);
        border.setLeft(addAnchorPane(addGridPane()));

        addStackPane(hbox);

      border.setCenter(addCenterPane());

        Scene scene = new Scene(border);
        scene.getStylesheets().add("studentbooking/css/button.css");
        stage.setScene(scene);
        stage.setTitle("学生火车票订票系统");
        stage.show();
    }
//    Button submit = new Button("预定");
    Button cancel = new Button("退票");

    private GridPane addCenterPane() {
        GridPane  centerGridPane = new GridPane();
        centerGridPane.setAlignment(CENTER);
        centerGridPane.setHgap(10.0);
        centerGridPane.setVgap(10.0);
        centerGridPane.setPadding(new Insets(10.0,10.0,10.0,10.0));
        label = new Label("所有用户");
        label.getStyleClass().add("label1");
        centerGridPane.add(label,0,0);

//        submit.getStyleClass().add("button3");
//        centerGridPane.add(submit,1,0);
//        submit.setVisible(false);

        cancel.getStyleClass().add("button3");
        centerGridPane.add(cancel,2,0);
        cancel.setVisible(false);

        tableView = new TableView();
        tableView.setPrefHeight(600);
        tableView.setId("tableView");
        tableView.setEditable(true);

        TableColumn checkBoxColumn = new TableColumn("勾选");   //选中框
        checkBoxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkBoxColumn));
        checkBoxColumn.setCellValueFactory(new PropertyValueFactory<>("isSelected"));

        checkBoxColumn.setEditable(true);

        TableColumn orderNameCol = new TableColumn("订单号");
        orderNameCol.setCellValueFactory(new PropertyValueFactory<>("orderNum"));
        orderNameCol.setPrefWidth(150);
        TableColumn nameCol = new TableColumn("姓名");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn timeCol = new TableColumn("下单时间");
        timeCol.setPrefWidth(160);
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableColumn isPaidCol = new TableColumn("是否支付");
        isPaidCol.setCellValueFactory(new PropertyValueFactory<>("ispaid"));
        TableColumn trainNameCol = new TableColumn("车次");
        trainNameCol.setCellValueFactory(new PropertyValueFactory<>("trainName"));
        TableColumn startNameCol = new TableColumn("起点");
        startNameCol.setCellValueFactory(new PropertyValueFactory<>("startPlace"));
        TableColumn endNameCol = new TableColumn("终点");
        endNameCol.setCellValueFactory(new PropertyValueFactory<>("endPlace"));
        TableColumn startTimeCol = new TableColumn("发车时间");
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        TableColumn endTimeCol = new TableColumn("到达时间");
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        TableColumn ticketTypeCol = new TableColumn("车票类型");
        ticketTypeCol.setCellValueFactory(new PropertyValueFactory<>("ticketType"));
        TableColumn remainTicketsCol = new TableColumn("库存");
        remainTicketsCol.setCellValueFactory(new PropertyValueFactory<>("remainTickets"));
        TableColumn fareCol = new TableColumn("票价");
        fareCol.setCellValueFactory(new PropertyValueFactory<>("fare"));

        tableView.getColumns().addAll(checkBoxColumn,orderNameCol,nameCol,timeCol,isPaidCol,trainNameCol,startNameCol,endNameCol,startTimeCol,endTimeCol,ticketTypeCol,remainTicketsCol,fareCol);

        centerGridPane.add(tableView,0,1,3,1);


        cancel.setOnMouseClicked((MouseEvent t)->{
            ObservableList<OrdersEntity> mResult = tableView.getItems();
            ArrayList<Boolean> mResultCopy = new ArrayList<Boolean>();
            int size = mResult.size();
            for (int i = 0; i < size; i++) {
                mResultCopy.add(mResult.get(i).getIsIsSelected());
            }
            System.out.println("mResult.size()" + mResult.size());
            for (int i = 0; i < size; i++) {
                if (mResultCopy.get(i)){
                    System.out.println("结果集合："+"被点击了"+mResult.get(i).getStartTime());
                    mResult.get(i).setRemainTickets(mResult.get(i).getRemainTickets()+1);
                    removeFromOrders(mResult.get(i));
                    saveInfo.remove(i);
                    data1.remove(i);
                }
            }
        });

        return centerGridPane;
    }

    private void removeFromOrders(OrdersEntity ticketInfoEntity) {
        String sqlstr0 = "SELECT * FROM Orders WHERE (train_name = '"+ticketInfoEntity.getTrainName()+"' AND start_time = '"+ticketInfoEntity.getStartTime()+"' AND end_time = '"+ticketInfoEntity.getEndTime()+"')";

        DBHelper dbHelper = new DBHelper();
        dbHelper.executeSQL(sqlstr0);
        ResultSet resultSet = dbHelper.getResultSet();
        String strNum = "201612151212";
        try {
            if (resultSet.next()){
                strNum = resultSet.getString("order_num");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sqlstr = "DELETE FROM Orders WHERE order_num = '"+strNum+"'";
        dbHelper.executeSQL(sqlstr);
        String sqlstrChangeRemain = "UPDATE Orders SET remain_tickets = "+ticketInfoEntity.getRemainTickets()+" WHERE ( train_name = '"+ticketInfoEntity.getTrainName()+"'AND start_place = '"+ticketInfoEntity.getStartPlace()+"' AND end_place = '"+ticketInfoEntity.getEndPlace()+"')";
        dbHelper.executeSQL(sqlstrChangeRemain);


    }

    ArrayList<OrdersEntity> saveInfo = new ArrayList<>();



    private HBox addHBox() {
 
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10, 20, 35, 20));
        hbox.setSpacing(10);   // Gap between nodes
        hbox.setStyle("-fx-background-color: #f0f0f0;");

//        Text text = new Text("  学生火车票订票系统");
//        text.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
//        text.setFill(Color.valueOf("#0795F4"));
//
        Text t = new Text();
        t.setX(10.0f);
        t.setY(50.0f);
        t.setCache(true);
        t.setText("学生火车票查询系统");
//        t.setFill(Color.RED);
        t.setFill(Color.valueOf("#FF9913"));
        t.setFont(Font.font( 35));

        Reflection r = new Reflection();
        r.setFraction(0.7f);

        t.setEffect(r);

//        t.setTranslateY(400);

        hbox.getChildren().add(t);


        return hbox;
    }


    private void addStackPane(HBox hb) {
 
        StackPane stack = new StackPane();
        Circle helpIcon = new Circle(14.5);
        helpIcon.setFill(new LinearGradient(0,0,0,1, true, CycleMethod.NO_CYCLE,
            new Stop[]{
            new Stop(0,Color.web("#0078D7")),
            new Stop(0.5, Color.web("#0078D7")),
            new Stop(1,Color.web("#0078D7")),}));
        helpIcon.setStroke(Color.web("#0078D7"));
        
        Text helpText = new Text("?");
        helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        helpText.setFill(Color.WHITE);
        helpText.setStroke(Color.web("#7080A0")); 
        
        stack.getChildren().addAll(helpIcon, helpText);
        stack.setAlignment(CENTER_RIGHT);
        StackPane.setMargin(helpText, new Insets(0, 10, 0, 0));
        
        hb.getChildren().add(stack);
        HBox.setHgrow(stack, Priority.ALWAYS);


        final Boolean[] flag = {false};
        stack.setOnMouseClicked((MouseEvent t)->{
            if (!flag[0]){
                helpText.setText("制作人：雷阳      !");
                flag[0] = true;
            } else {
                helpText.setText("?");
                flag[0] = false;
            }
        });
                
    }
 

    private GridPane addGridPane() {
 
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 20, 0, 10));

        Text category = new Text("工号："+operatorEntity.getAccount());
        category.setFont(Font.font( 20));
        grid.setMargin(category,new Insets(10,0,0,0));
        grid.add(category, 1,0);

        Text school = new Text("姓名："+operatorEntity.getName());
        school.setFont(Font.font(20));
        grid.add(school, 1, 1);



//
//        TextField endCity = new TextField();
//        endCity.setPromptText("到达城市");
//        grid.setMargin(endCity,new Insets(5,0,10,0));
//        GridPane.setConstraints(endCity, 1, 3);
//        grid.getChildren().add(endCity);


        Text tip1 = new Text("您可以选择");
        tip1.setFont(Font.font("Arial", 12));
        tip1.setFill(Color.web("#A2A2A2"));
        grid.add(tip1,1,3);


        TextField nameSpace = new TextField();
        nameSpace.setPromptText("输入姓名");
        nameSpace.setFocusTraversable(false);
        grid.setMargin(nameSpace,new Insets(10,0,0,0));
        GridPane.setConstraints(nameSpace, 1, 4);
        grid.getChildren().add(nameSpace);


        Button searchTicket = new Button();
        searchTicket.setText("按照姓名搜索");
        searchTicket.getStyleClass().add("button1");
        grid.setMargin(searchTicket,new Insets(0,0,10,0));
        grid.add(searchTicket,1,5);

        Text tip2 = new Text("或者是");
        tip2.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        tip2.setFill(Color.web("#A2A2A2"));
        grid.add(tip2,1,6);

        Button searchOrders = new Button();
        searchOrders.setText("查看全部订单");
        searchOrders.getStyleClass().add("button2");
        grid.add(searchOrders,1,7);

        searchTicket.setOnMouseClicked((MouseEvent t)->{
            label.setText(nameSpace.getText());
            String nameSpaceText = nameSpace.getText();
            System.out.println(nameSpaceText);
            if (nameSpaceText==null || nameSpaceText.equals("")||nameSpaceText.equals(" ")){
                data1.clear();
                tableView.setItems(data1);
            }else{
                String sqlstr = "SELECT * FROM Orders WHERE name = '"+nameSpaceText+"'";
                addNewLines(sqlstr);
            }
        });

        searchOrders.setOnMouseClicked((MouseEvent t)->{
            String sqlstr =  "SELECT * FROM Orders";
            addNewLines(sqlstr);
        });

        return grid;
    }


    final Boolean[] firstTime = {true};
    private void addNewLines(String sqlstr) {
        data1.clear();
        saveInfo.clear();
            DBHelper dbHelper = new DBHelper();
            dbHelper.executeSQL(sqlstr);
            ResultSet resultSet = dbHelper.getResultSet();
            try {
                while (resultSet.next()){
                    OrdersEntity ordersEntity = new OrdersEntity();
                    ordersEntity.setRemainTickets(resultSet.getInt("remain_tickets"));
                    ordersEntity.setIsSelected(false);
                    ordersEntity.setFare(resultSet.getFloat("fare"));
                    ordersEntity.setEndPlace(resultSet.getString("end_place"));
                    ordersEntity.setEndTime(resultSet.getString("end_time"));
                    ordersEntity.setStartPlace(resultSet.getString("start_place"));
                    ordersEntity.setStartTime(resultSet.getString("start_time"));
                    ordersEntity.setTicketType(resultSet.getString("ticket_type"));
                    ordersEntity.setTrainName(resultSet.getString("train_name"));
                    ordersEntity.setIspaid(resultSet.getString("ispaid"));
                    ordersEntity.setOrderNum(resultSet.getString("order_num"));
                    ordersEntity.setName(resultSet.getString("name"));
                    ordersEntity.setTime(resultSet.getString("time"));

                    saveInfo.add(ordersEntity);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        for (int i = 0; i < saveInfo.size(); i++) {
            data1.add(saveInfo.get(i));
        }
        tableView.setItems(data1);
        cancel.setVisible(true);

    }


    private AnchorPane addAnchorPane(GridPane grid) {
 
        AnchorPane anchorpane = new AnchorPane();

        HBox hb = new HBox();
        hb.setPadding(new Insets(0, 10, 10, 10));
        hb.setSpacing(10);

        anchorpane.getChildren().addAll(grid,hb);
        AnchorPane.setBottomAnchor(hb, 8.0);
        AnchorPane.setRightAnchor(hb, 5.0);
        AnchorPane.setTopAnchor(grid, 10.0);
 
        return anchorpane;
    }
}