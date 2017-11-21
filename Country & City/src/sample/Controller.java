package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Controller {
    public TextField txtPais;
    public ListView<String> lvPaises;
    public ListView<String> lvCiudades;
    public Label lbTitulo;
    public Label lbError;
    public Label lblID;
    public Label lblNombre;
    public Label lblCodPais;
    public Label lblDistrict;
    public Label lblPopulation;
    private ObservableList listaPaises = FXCollections.observableArrayList();
    private ObservableList listaCiudades = FXCollections.observableArrayList();

    private String paisSeleccionado;
    private String ciudadSeleccionado;

    @FXML
    public void initialize(){
        lvPaises.setItems(listaPaises);
        lvCiudades.setItems(listaCiudades);
        lvPaises.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                paisSeleccionado = newValue;
                listaCiudades.clear();
                try{
                    Connection con = Main.getConexion();
                    Statement stmt = con.createStatement();
                    String sql = "SELECT Name FROM city WHERE countrycode=(SELECT Code FROM country WHERE Name='"+paisSeleccionado+"')";
                    ResultSet resultado = stmt.executeQuery(sql);
                    while (resultado.next()){
                        listaCiudades.add(resultado.getString("Name"));
                    }
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
            }

        });
        lvCiudades.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ciudadSeleccionado = newValue;

                try{
                    Connection con = Main.getConexion();
                    Statement stmt = con.createStatement();
                    String sql = "SELECT ID,Name,CountryCode,District,Population FROM city WHERE ID=(SELECT ID FROM city WHERE Name='"+ciudadSeleccionado+"')";
                    ResultSet resultado = stmt.executeQuery(sql);
                    while (resultado.next()){
                        lblID.setText(String.valueOf(resultado.getInt("ID")));
                        lblNombre.setText(resultado.getString("Name"));
                        lblCodPais.setText(resultado.getString("CountryCode"));
                        lblDistrict.setText(resultado.getString("District"));
                        lblPopulation.setText(String.valueOf(resultado.getInt("Population")));
                    }
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
            }

        });

    }


    public void buscarPais(KeyEvent keyEvent){
        listaPaises.clear();
        String nombreBusqueda = txtPais.getText().trim();
        if (nombreBusqueda.length() >= 1){
            Connection con = Main.getConexion();
            try {
                Statement stnt = con.createStatement();
                String sql = "SELECT Name FROM country WHERE Name LIKE '"+ nombreBusqueda+ "%'";
                ResultSet resultado = stnt.executeQuery(sql);
                while (resultado.next()){
                    listaPaises.add(resultado.getString("Name"));
                }
                resultado.close();
            } catch (SQLException e) {
                lbError.setText(e.getMessage());
            }
        }
    }

}