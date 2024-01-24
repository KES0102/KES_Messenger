package org.example.springclasses.dao;

import org.example.springclasses.models.Person1Person2MSG;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.validation.groups.Default;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class PersonDAO {
    static int idd = 1;

    JdbcTemplate jdbcTemplate;

    public PersonDAO(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Person1Person2MSG> show() {

        return jdbcTemplate.query("Select * From temp",
                new BeanPropertyRowMapper<>(Person1Person2MSG.class));
    }

    public void save(Person1Person2MSG person1Person2MSG) {
        jdbcTemplate.update("INSERT INTO temp VALUES (" + ++idd + ", ?, ?)",
                person1Person2MSG.getEmail(), person1Person2MSG.getPassword());
    }

    // Метод ищет id по email и password для авторизации одного человека
    public List<Person1Person2MSG> search(final String email, String password) {

        return jdbcTemplate.query("SELECT id, name FROM \"authorization\" WHERE email!=? and password!=?;", new Object[]{email, password},
                new RowMapper<Person1Person2MSG>() {
                    @Override
                    public Person1Person2MSG mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Person1Person2MSG person1Person2MSG = new Person1Person2MSG();
                        person1Person2MSG.setIdTwo(rs.getInt(1));
                        person1Person2MSG.setNameTwo(rs.getString(2));

                        // добавление id 1-го в результат
                        person1Person2MSG.setIdOne(jdbcTemplate.query("SELECT id FROM \"authorization\"WHERE email=?;", new Object[]{email},
                                new RowMapper<Person1Person2MSG>() {
                                    @Override
                                    public Person1Person2MSG mapRow(ResultSet rs, int rowNum) throws SQLException {
                                        Person1Person2MSG person1Person2MSG = new Person1Person2MSG();
                                        person1Person2MSG.setIdOne(rs.getInt(1));
                                        return person1Person2MSG;
                                    }
                                }).get(0).getIdOne());

                        return person1Person2MSG;
                    }
                });

    }

    //------------------------------------------------------------------------------------------------------------------
    public void saveMessage(Person1Person2MSG person) {
        String tableName;
        if (person.getIdOne() > person.getIdTwo()) {
            tableName = "" + person.getIdTwo() + person.getIdOne();
        } else tableName = "" + person.getIdOne() + person.getIdTwo();

        //System.out.println(tableName);
        String s = Character.toString('"');

        jdbcTemplate.update("INSERT INTO " + s + tableName + s + " (message, idone, idtwo) VALUES ( ?, ?, ?)",
                person.getMessage(), person.getIdOne(), person.getIdTwo());
    }

    //------------------------------------------------------------------------------------------Читает базу - 10 записей
    public List<Person1Person2MSG> readTableForMessages(Person1Person2MSG person) {
        String tableName;
        if (person.getIdOne() > person.getIdTwo()) {
            tableName = "" + person.getIdTwo() + person.getIdOne();
        } else tableName = "" + person.getIdOne() + person.getIdTwo();
        String s = Character.toString('"');

        //System.out.println("Метод read: "+tableName);

        return  jdbcTemplate.query("SELECT idone, message FROM " +s+tableName+s+
                        " ORDER BY id DESC LIMIT 10;", new Object[]{},


                       /* "(SELECT max(id) FROM " +s+tableName+s+") "+
                        "AND ( SELECT max(id)-5 FROM  "+s+tableName+s+") ;", new Object[]{},*/
                new RowMapper<Person1Person2MSG>() {
                    @Override
                    public Person1Person2MSG mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Person1Person2MSG personMes = new Person1Person2MSG();
                        personMes.setIdOne(rs.getInt(1));
                        personMes.setMessage(rs.getString(2));

                        //System.out.println(personMes.getMessage());
                        return personMes;
                    }
                } );
    }

    //-------------------------------------------------------------------------------------
    public void registration(Person1Person2MSG person){
        jdbcTemplate.update("INSERT INTO \"authorization\" (email, password, name) VALUES (" + " ?, ?, ?)",
                person.getEmail(), person.getPassword(), person.getNameOne());
    }
}
