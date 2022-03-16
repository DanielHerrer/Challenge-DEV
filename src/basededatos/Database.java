package basededatos;
import basededatos.clases.Municipalidad;
import basededatos.clases.QueryPromedio;

import java.sql.*;
import java.util.ArrayList;

public class Database {

    private static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/basededatos";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static Connection con;

    public static void main(String[] args) {

        try {
            Class.forName(DATABASE_DRIVER);
            con = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            System.out.println("Conexion realizada.");

            //insertMunicipalidad("Municipalidad Alzeimer");

            //insertEmpleado("Julian","Ricky","6942021345","julianricky@123",75000,11,"Analizador de datos",1);

            ArrayList<QueryPromedio> lista = listaPromedio();
            for(int i=0;i<lista.size();i++){
                System.out.println(lista.get(i));
            }



            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Municipalidades");
            while(rs.next()){
                int id_muni = rs.getInt("id_muni");
                String nombreM = rs.getString("nombreM");
                System.out.println("MUNICIPALIDAD ID: "+id_muni);
                System.out.println("Nombre de la municipalidad: "+nombreM);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void insertMunicipalidad(String nombreM) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO municipalidades(nombreM) values(?)");
        ps.setString(1,nombreM);
        ps.execute();
        ps.close();
    }

    private static void insertEmpleado(String nombre, String apellido, String telefono, String email, int salario, int antiguedad, String puesto,int id_municipalidad) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO empleados(nombre,apellido,telefono,email,salario, antiguedad, puesto, id_municipalidad) values(?,?,?,?,?,?,?,?)");
        ps.setString(1,nombre);
        ps.setString(2,apellido);
        ps.setString(3,telefono);
        ps.setString(4,email);
        ps.setInt(5,salario);
        ps.setInt(6,antiguedad);
        ps.setString(7,puesto);
        ps.setInt(8,id_municipalidad);
        ps.execute();
        ps.close();
    }

    private static ArrayList<QueryPromedio> listaPromedio() throws SQLException{
        ArrayList<QueryPromedio> lista = new ArrayList<>();
        PreparedStatement ps = con.prepareStatement("SELECT emp.nombre, emp.apellido, emp.telefono, emp.email, emp.puesto, m.nombreM\n" +
                "FROM empleados AS emp JOIN municipalidades AS m\n" +
                "ON emp.id_municipalidad = m.id_muni\n" +
                "WHERE emp.salario>=? AND emp.antiguedad<=? && emp.antiguedad>=?;");
        ps.setInt(1,70000);
        ps.setInt(2,15);
        ps.setInt(3,10);

        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            lista.add(new QueryPromedio(rs.getString("nombre"),rs.getString("apellido"),rs.getString("telefono"),rs.getString("email"),rs.getString("puesto"),rs.getString("nombreM")));
        }
        return lista;
    }

}
