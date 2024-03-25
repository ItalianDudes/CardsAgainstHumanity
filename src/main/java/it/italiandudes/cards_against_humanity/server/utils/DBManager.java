package it.italiandudes.cards_against_humanity.server.utils;

import it.italiandudes.cards_against_humanity.utils.Defs;
import it.italiandudes.idl.common.InfoFlags;
import it.italiandudes.idl.common.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlDialectInspection", "DuplicatedCode"})
public final class DBManager {

    // Attributes
    private static Connection dbConnection = null;
    private static final String DB_PREFIX = "jdbc:sqlite:";

    // Generic SQLite Connection Initializer
    private static void setConnection(@NotNull final String DB_ABSOLUTE_PATH) throws SQLException {
        dbConnection = DriverManager.getConnection(DB_PREFIX + DB_ABSOLUTE_PATH);
        dbConnection.setAutoCommit(true);
        Statement st = dbConnection.createStatement();
        st.execute("PRAGMA foreign_keys = ON;");
        st.close();
    }

    // Methods
    public static void connectToDB(@NotNull final File DB_PATH) throws IOException, SQLException {
        if (!DB_PATH.exists() || DB_PATH.isDirectory()) throw new IOException("This db doesn't exist");
        setConnection(DB_PATH.getAbsolutePath());
    }
    public static void closeConnection() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            }catch (Exception ignored){}
        }
    }
    public static PreparedStatement preparedStatement(@NotNull final String query) throws SQLException {
        if (dbConnection != null) {
            //noinspection SqlSourceToSinkFlow
            return dbConnection.prepareStatement(query);
        }
        return null;
    }

    // DB Creator
    public static void createDB(@NotNull final String DB_PATH) throws SQLException {
        setConnection(DB_PATH);
        Scanner reader = new Scanner(Defs.Resources.getAsStream(Defs.Resources.SQL.SQL_CARDS), Defs.DEFAULT_CHARSET);
        StringBuilder queryReader = new StringBuilder();
        String query;
        String buffer;

        while (reader.hasNext()) {
            buffer = reader.nextLine();
            queryReader.append(buffer);
            if (buffer.endsWith(";")) {
                query = queryReader.toString();
                PreparedStatement ps = dbConnection.prepareStatement(query);
                ps.execute();
                ps.close();
                queryReader = new StringBuilder();
            } else {
                queryReader.append('\n');
            }
        }
        reader.close();
        writeKeyParameter(KeyParameters.DB_VERSION, Defs.DB_VERSION);
    }
    private static boolean isKeyParameterPresent(@NotNull final String KEY) throws SQLException {
        String query = "SELECT * FROM key_parameters WHERE param_key=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The database connection doesn't exist");
        ps.setString(1, KEY);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            ps.close();
            return true;
        } else {
            ps.close();
            return false;
        }
    }
    public static void writeKeyParameter(@NotNull final String KEY, @NotNull final String VALUE) {
        String query;
        PreparedStatement ps = null;
        try {
            if (isKeyParameterPresent(KEY)) { // Update
                query = "UPDATE key_parameters SET param_value=? WHERE param_key=?;";
                ps = DBManager.preparedStatement(query);
                if (ps == null) throw new SQLException("The database connection doesn't exist");
                ps.setString(1, VALUE);
                ps.setString(2, KEY);
            } else { // Insert
                query = "INSERT OR REPLACE INTO key_parameters (param_key, param_value) VALUES (?, ?);";
                ps = DBManager.preparedStatement(query);
                if (ps == null) throw new SQLException("The database connection doesn't exist");
                ps.setString(1, KEY);
                ps.setString(2, VALUE);
            }
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            try {
                if (ps != null) ps.close();
            } catch (SQLException ignored) {}
            Logger.log("Si e' verificato un errore durante la scrittura di un parametro.\nKEY: "+KEY+"\nVALUE: "+VALUE, new InfoFlags(true, true));
            Logger.log(e);
            DBManager.closeConnection();
        }
    }
    public static String readKeyParameter(@NotNull final String KEY) {
        PreparedStatement ps = null;
        try {
            String query = "SELECT param_value FROM key_parameters WHERE param_key=?;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database connection doesn't exist");
            ps.setString(1, KEY);
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                String value = result.getString("param_value");
                ps.close();
                return value;
            } else {
                ps.close();
                return null;
            }
        } catch (SQLException e) {
            try {
                if (ps != null) ps.close();
            } catch (SQLException ignored) {}
            Logger.log("Si e' verificato un errore durante la lettura del parametro: \"" + KEY + "\"", new InfoFlags(true, true));
            Logger.log(e);
            DBManager.closeConnection();
            return null;
        }
    }
}
