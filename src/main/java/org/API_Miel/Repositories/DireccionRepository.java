package org.API_Miel.Repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.API_Miel.ConfigDB.ConfigDB;
import org.API_Miel.Models.Direccion;

public class DireccionRepository {
    private final DataSource ds = ConfigDB.getDataSource();

    public List<Direccion> listByUsuario(long idUsuario) {
        List<Direccion> list = new ArrayList<>();
        try (Connection c = ds.getConnection();
                PreparedStatement ps = c.prepareStatement(
                        "SELECT ID_Direccion, ID_Usuario, Calle, Colonia, Codigo_Postal, Ciudad, Estado " +
                                "FROM Direccion WHERE ID_Usuario = ? ORDER BY ID_Direccion DESC")) {
            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(mapDireccion(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error listando direcciones: " + e.getMessage(), e);
        }
    }

    public long create(Direccion d) {
        try (Connection c = ds.getConnection();
                PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO Direccion (ID_Usuario, Calle, Colonia, Codigo_Postal, Ciudad, Estado) " +
                                "VALUES (?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, d.getIdUsuario());
            ps.setString(2, d.getCalle());
            ps.setString(3, d.getColonia());
            ps.setString(4, d.getCodigoPostal());
            ps.setString(5, d.getCiudad());
            ps.setString(6, d.getEstado());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next())
                return rs.getLong(1);
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error creando dirección: " + e.getMessage(), e);
        }
    }

    public Direccion getById(long idDireccion) {
        try (Connection c = ds.getConnection();
                PreparedStatement ps = c.prepareStatement(
                        "SELECT ID_Direccion, ID_Usuario, Calle, Colonia, Codigo_Postal, Ciudad, Estado " +
                                "FROM Direccion WHERE ID_Direccion = ?")) {
            ps.setLong(1, idDireccion);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapDireccion(rs);
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo dirección: " + e.getMessage(), e);
        }
    }

    public boolean existsById(long idDireccion) {
        try (Connection c = ds.getConnection();
                PreparedStatement ps = c.prepareStatement(
                        "SELECT 1 FROM Direccion WHERE ID_Direccion = ?")) {
            ps.setLong(1, idDireccion);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error verificando dirección: " + e.getMessage(), e);
        }
    }

    public boolean delete(long idDireccion) {
        try (Connection c = ds.getConnection()) {
            try (PreparedStatement chk = c.prepareStatement("SELECT 1 FROM Pedido WHERE ID_Direccion=? LIMIT 1")) {
                chk.setLong(1, idDireccion);
                ResultSet rs = chk.executeQuery();
                if (rs.next())
                    throw new RuntimeException("Direccion en uso");
            }
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM Direccion WHERE ID_Direccion = ?")) {
                ps.setLong(1, idDireccion);
                int n = ps.executeUpdate();
                return n > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando dirección: " + e.getMessage(), e);
        }
    }

    private Direccion mapDireccion(ResultSet rs) throws SQLException {
        Direccion d = new Direccion();
        d.setIdDireccion(rs.getLong("ID_Direccion"));
        d.setIdUsuario(rs.getLong("ID_Usuario"));
        d.setCalle(rs.getString("Calle"));
        d.setColonia(rs.getString("Colonia"));
        d.setCodigoPostal(rs.getString("Codigo_Postal"));
        d.setCiudad(rs.getString("Ciudad"));
        d.setEstado(rs.getString("Estado"));
        return d;
    }
}
