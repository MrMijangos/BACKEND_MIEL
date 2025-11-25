package org.API_Miel.Repositories;

import org.API_Miel.ConfigDB.ConfigDB;
import org.API_Miel.Models.Resena;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ResenaRepository {
    private final DataSource ds = ConfigDB.getDataSource();

    public List<Resena> listByProducto(long idProducto) {
        List<Resena> list = new ArrayList<>();
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT ID_Resena, ID_Producto, ID_Usuario, Calificacion, Comentario, Fecha FROM Resena WHERE ID_Producto=?")) {
            ps.setLong(1, idProducto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public long create(Resena r) {
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO Resena(ID_Producto,ID_Usuario,Calificacion,Comentario,Fecha) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, r.getIdProducto());
            ps.setLong(2, r.getIdUsuario());
            ps.setInt(3, r.getCalificacion());
            ps.setString(4, r.getComentario());
            ps.setTimestamp(5, Timestamp.from(Instant.now()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getLong(1);
            return 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void delete(long idResena) {
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Resena WHERE ID_Resena=?")) {
            ps.setLong(1, idResena);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private Resena map(ResultSet rs) throws SQLException {
        Resena r = new Resena();
        r.setIdResena(rs.getLong(1));
        r.setIdProducto(rs.getLong(2));
        r.setIdUsuario(rs.getLong(3));
        r.setCalificacion(rs.getInt(4));
        r.setComentario(rs.getString(5));
        r.setFecha(rs.getTimestamp(6).toInstant());
        return r;
    }
}
