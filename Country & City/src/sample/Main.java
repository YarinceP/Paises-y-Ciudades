package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {
    private static final String CONNECTION_STRING="jdbc:mysql://127.0.0.1:3306/world";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";
    private static Connection conexion;


    @Override
    public void start(Stage primaryStage) throws Exception{
        abrirConexion();
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 550));
        primaryStage.show();
    }

    private static void abrirConexion(){
        try {
            conexion = DriverManager.getConnection(CONNECTION_STRING,USUARIO,CLAVE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }

    public static Connection getConexion(){
        return conexion;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
